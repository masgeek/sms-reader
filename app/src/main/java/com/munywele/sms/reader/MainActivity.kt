package com.munywele.sms.reader

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.Manifest.permission
import android.database.Cursor
import android.net.Uri
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.munywele.sms.reader.database.entities.SmsEntity
import com.munywele.sms.reader.view.SmsViewModel
import com.munywele.sms.reader.view.SmsViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val smsViewModel: SmsViewModel by viewModels {
        SmsViewModelFactory((application as SmsReader).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
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
                val address = it.getString(it.getColumnIndexOrThrow("address"))
                val date = it.getLong(it.getColumnIndexOrThrow("date"))
                val body = it.getString(it.getColumnIndexOrThrow("body"))
                val smsEntity = SmsEntity(
                    address = address,
                    body = body,
                    date = date
                )
                lifecycleScope.launch {
                    smsViewModel.insertSms(smsEntity)
                }
            }
        } ?: Toast.makeText(this, "No SMS found", Toast.LENGTH_SHORT).show()
    }


}