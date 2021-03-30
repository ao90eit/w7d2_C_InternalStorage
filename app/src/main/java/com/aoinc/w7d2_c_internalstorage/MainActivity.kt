package com.aoinc.w7d2_c_internalstorage

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.widget.Button
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import java.io.*
import java.util.*

class MainActivity : AppCompatActivity() {

    val filename = "internal.html"
    private lateinit var file: File

    private lateinit var editText: EditText
    private lateinit var submitButton: Button
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editText = findViewById(R.id.edittext)
        submitButton = findViewById(R.id.button)
        webView = findViewById(R.id.webview)

        file = File(filename)

        submitButton.setOnClickListener {
            writeToInternalStorage()
            showNotification()
        }

        try {
            readFromInternalStorage()
        } catch (e: Exception) {
            Log.d("TAG_X", "error -> ${e.toString()}")
        }
    }

    fun writeToInternalStorage() {

        val fileOutput = openFileOutput(filename, Context.MODE_PRIVATE)
        val text = editText.text.toString()
        editText.text.clear()

//        bufferedOutputStream.write(text.toByteArray())
//        bufferedOutputStream.close()
        fileOutput.write(text.toByteArray())
        fileOutput.close()

        try {
            readFromInternalStorage()
        } catch (e: Exception) {
            Log.d("TAG_X", "error -> ${e.toString()}")
        }
    }

    @Throws(IOException::class)
    fun readFromInternalStorage() {
        webView.loadUrl("file:///$filesDir/$file")

        val fileReader = FileReader("$filesDir/$filename")
        val bufferedReader = BufferedReader(fileReader)

        val stringBuffer = StringBuffer()
        var input: String? = null
        while ({input = bufferedReader.readLine(); input}() != null) {
            stringBuffer.append(input)
//            stringBuffer.append(" ${Date().time} ")
        }

        Log.d("TAG_X", stringBuffer.toString())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun showNotification() {

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationChannel = NotificationChannel(
            "111", "Main Notification Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(notificationChannel)

        val notification = NotificationCompat
            .Builder(this, notificationChannel.id)
            .setPriority(notificationChannel.importance)
            .setContentTitle("Content Title")
            .setContentText("Content Text")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()

        notificationManager.notify(111, notification)
    }
}