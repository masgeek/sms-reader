package com.munywele.sms

import android.Manifest
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.munywele.sms.adapter.SmsAdapter
import com.munywele.sms.database.entities.SmsEntity
import com.munywele.sms.databinding.ActivityMainBinding
import com.munywele.sms.utils.NumberUtils
import com.munywele.sms.utils.StringUtils
import com.munywele.sms.view.SmsViewModel
import com.munywele.sms.view.SmsViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var smsAdapter: SmsAdapter

    private val smsViewModel: SmsViewModel by viewModels {
        SmsViewModelFactory((application as SmsReader).repository)
    }

    private val smsPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            readSmsMessages()
        } else {
            Toast.makeText(this, "SMS permission is required to read messages", Toast.LENGTH_LONG)
                .show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupFilterButton()
        observeSmsMessages()
        checkSmsPermission()
    }

    private fun setupRecyclerView() {
        smsAdapter = SmsAdapter()
        binding.smsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = smsAdapter
        }
    }

    private fun setupFilterButton() {
        binding.filterButton.setOnClickListener {
            val minAmount = binding.filterAmountEditText.text.toString().toDoubleOrNull()
            val sender = binding.filterSenderEditText.text.toString().takeIf { it.isNotBlank() }
            val searchString =
                binding.filterContentEditText.text.toString().takeIf { it.isNotBlank() }

            smsViewModel.filterMessages(
                minAmount = minAmount,
                sender = sender,
                searchString = searchString
            )
        }
    }

    private fun observeSmsMessages() {
        smsViewModel.allSms.observe(this) { smsList ->
            smsAdapter.updateSmsList(smsList)
        }
        smsViewModel.filteredSms.observe(this) { messages ->
            smsAdapter.updateSmsList(messages)
        }
    }

    private fun checkSmsPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_SMS
            ) == PackageManager.PERMISSION_GRANTED -> {
                readSmsMessages()
            }

            shouldShowRequestPermissionRationale(Manifest.permission.READ_SMS) -> {
                Toast.makeText(
                    this,
                    "SMS permission is required to read messages",
                    Toast.LENGTH_LONG
                ).show()
                smsPermissionLauncher.launch(Manifest.permission.READ_SMS)
            }

            else -> {
                smsPermissionLauncher.launch(Manifest.permission.READ_SMS)
            }
        }
    }

    private fun readSmsMessages() {
        lifecycleScope.launch {
            val smsMessages = withContext(Dispatchers.IO) {
                fetchSmsFromContentProvider()
            }
            if (smsMessages.isNotEmpty()) {
                smsViewModel.insertAllSms(smsMessages)
                Toast.makeText(this@MainActivity, "${smsMessages.size} SMS messages imported", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@MainActivity, "No new SMS messages found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchSmsFromContentProvider(): List<SmsEntity> {
        val uri: Uri = Uri.parse("content://sms/inbox")
        val projection = arrayOf("_id", "address", "date", "body")
        val smsMessages = mutableListOf<SmsEntity>()

        contentResolver.query(uri, projection, null, null, "date DESC")?.use { cursor ->
            while (cursor.moveToNext()) {
                val sms = cursor.toSmsEntity()
                smsMessages.add(sms)
            }
        }

        return smsMessages
    }

    private fun Cursor.toSmsEntity(): SmsEntity {
        val sender = getString(getColumnIndexOrThrow("address"))
        val date = getLong(getColumnIndexOrThrow("date"))
        val body = getString(getColumnIndexOrThrow("body"))
        val amount = NumberUtils.extractFirstAmountAsInt(body)
        val hash = StringUtils.generateMessageHash(body)

        return SmsEntity(
            id = hash,
            sender = sender,
            body = body,
            amount = amount,
            timestamp = date
        )
    }
}