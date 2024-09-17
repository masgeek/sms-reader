package com.munywele.sms

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import android.Manifest.permission
import android.database.Cursor
import android.net.Uri
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.munywele.sms.adapter.SmsAdapter
import com.munywele.sms.database.entities.SmsEntity
import com.munywele.sms.databinding.ActivityMainBinding
import com.munywele.sms.utils.NumberUtils
import com.munywele.sms.utils.StringUtils
import com.munywele.sms.view.SmsViewModel
import com.munywele.sms.view.SmsViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var smsAdapter: SmsAdapter

    private val smsViewModel: SmsViewModel by viewModels {
        SmsViewModelFactory((application as SmsReader).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        smsAdapter = SmsAdapter(emptyList())
        binding.smsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = smsAdapter
        }

        // Check for SMS permission
        if (ContextCompat.checkSelfPermission(
                this,
                permission.READ_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestSmsPermission()
        } else {
            readSmsMessages()
        }

        observeMessages()
    }


    private fun requestSmsPermission() {
        // Request permission to read SMS
        val smsPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                readSmsMessages()
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
        smsPermissionLauncher.launch(permission.READ_SMS)
    }

    private fun readSmsMessages() {
        val uri: Uri = Uri.parse("content://sms/inbox")
        val projection = arrayOf("_id", "address", "date", "body")
        val cursor: Cursor? = contentResolver.query(uri, projection, null, null, "date DESC")

        cursor?.use {
            while (it.moveToNext()) {
                val send = it.getString(it.getColumnIndexOrThrow("address"))
                val date = it.getLong(it.getColumnIndexOrThrow("date"))
                val body = it.getString(it.getColumnIndexOrThrow("body"))
                val amount = NumberUtils.extractFirstAmountAsInt(body)

                val hash = StringUtils.generateMessageHash(body)
                val smsEntity = SmsEntity(
                    id = hash,
                    sender = send,
                    body = body,
                    amount = amount,
                    date = date
                )

                lifecycleScope.launch {
                    smsViewModel.insertSms(smsEntity)
                }
            }
        } ?: Toast.makeText(this, "No SMS found", Toast.LENGTH_SHORT).show()
    }

    private fun observeMessages() {

        // Define the parameters
        val sender = "MPESA"
        val searchString = "NAIROBI SMALL AND COMPANION ANIMAL HOSPITAL LIMITED"
        val minAmount = 5000.00

        lifecycleScope.launch {
            smsViewModel.getFilteredSms(sender, searchString, minAmount).collect { smsList ->
                smsAdapter.updateSmsList(newSmsList = smsList)
            }
        }
    }
}