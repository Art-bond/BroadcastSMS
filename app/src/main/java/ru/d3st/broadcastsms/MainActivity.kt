package ru.d3st.broadcastsms

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts

import androidx.core.content.ContextCompat

import android.content.IntentFilter
import android.provider.Telephony
import android.text.Layout
import android.widget.Button
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {

    private lateinit var smsBroadcastReceiver: SmsBroadcastReceiver
    private var codeEditTextView: EditText? = null
    private var enterBtn: Button? = null

    /**
     * Проверяем есть ли необходимые разрешения
     */
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Permission is granted. Continue the action or workflow in your
                // app.
                Log.i(javaClass.name, "Permission is granted.")
            } else {
                // Explain to the user that the feature is unavailable because the
                // features requires a permission that the user has denied. At the
                // same time, respect the user's decision. Don't link to system
                // settings in an effort to convince the user to change their
                // decision.
                Log.i(javaClass.name, "Permission has no been granted")
                showInContextUI("Permission has no been granted")
            }
        }

    override fun onStart() {
        super.onStart()
        setupBroadcastReceiver()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //инициализируем элементы экрана
        codeEditTextView = findViewById(R.id.et_code)
        enterBtn = findViewById<Button>(R.id.btn_submit).apply {
            setOnClickListener {
                val messageUi =
                    if (codeEditTextView?.text.isNullOrEmpty()) "Код не принят"
                    else "Код принят"
                showInContextUI(messageUi)
            }
        }






        requestReadAndSendSmsPermission()

    }

    override fun onDestroy() {
        super.onDestroy()
        destroyBroadCastReceiver()
    }


    /**
     * Request runtime SMS permission
     */
    private fun requestReadAndSendSmsPermission() {
        when {
            ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.RECEIVE_SMS
            ) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permission.
                showInContextUI("All permission has been taken")

            }
            shouldShowRequestPermissionRationale("Need sms permission") -> {
                // In an educational UI, explain to the user why your app requires this
                // permission for a specific feature to behave as expected. In this UI,
                // include a "cancel" or "no thanks" button that allows the user to
                // continue using your app without granting the permission.
                showInContextUI("Need sms permission")
            }
            else -> {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                requestPermissionLauncher.launch(
                    Manifest.permission.RECEIVE_SMS
                )
            }
        }
    }

    private fun showInContextUI(message: String) {
        Log.i(javaClass.name, message)
        Snackbar.make(
            this.findViewById<ConstraintLayout>(R.id.root_layout),
            message,
            Snackbar.LENGTH_LONG
        ).show();
    }

    /**
     * Запускаем BroadCastReceiver
     */
    private fun setupBroadcastReceiver() {
        smsBroadcastReceiver = SmsBroadcastReceiver().apply {
            setListener(
                object : SmsBroadcastReceiver.SmsListener {
                    override fun onTextReceived(code: String) {
                        Log.i(localClassName, "code : $code")
                        codeEditTextView?.setText(code)
                    }

                })

        }
        val filter = IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)
        registerReceiver(smsBroadcastReceiver, filter)


    }

    /**
     * Выключаем BroadCastReceiver
     */
    private fun destroyBroadCastReceiver() {
        unregisterReceiver(smsBroadcastReceiver)
    }
}
