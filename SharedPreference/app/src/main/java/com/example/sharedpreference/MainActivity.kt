package com.example.sharedpreference

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.sharedpreference.ui.theme.SharedPreferenceTheme

class MainActivity : ComponentActivity() {
    private lateinit var editUsername : EditText
    private lateinit var editPassword : EditText
    private lateinit var btnSave : Button
    private lateinit var btnClear : Button
    private lateinit var btnShow : Button
    private lateinit var txtUsername : TextView
    private lateinit var txtPassword : TextView
    private lateinit var preferenceHelper: PreferenceHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Tham chieu phan tu thuc te
        editUsername = findViewById(R.id.et_username)
        editPassword = findViewById(R.id.et_password)
        btnSave = findViewById(R.id.btn_save)
        btnClear = findViewById(R.id.btn_clear)
        btnShow = findViewById(R.id.btn_show)
        txtUsername = findViewById(R.id.txtUsername)
        txtPassword = findViewById(R.id.txtPassword)
        preferenceHelper = PreferenceHelper(this)

        btnSave.setOnClickListener {
            val username = editUsername.text.toString()
            val password = editPassword.text.toString()
            if (username.isNotEmpty() || password.isNotEmpty()) {
                preferenceHelper.saveCredentials(username, password)
                editUsername.text.clear()
                editPassword.text.clear()
                Toast.makeText(this,"Đã lưu thông tin", Toast.LENGTH_SHORT).show()
            }

        }

        btnShow.setOnClickListener {
            val username = preferenceHelper.getUsername() ?: "Chưa có thông tin"
            val password = preferenceHelper.getPassword() ?: "Chưa có thông tin"
            txtUsername.text = username
            txtPassword.text = password
        }

        btnClear.setOnClickListener {
            preferenceHelper.clearCredentials()
            txtUsername.text = ""
            txtPassword.text = ""
            Toast.makeText(this,"Đã xóa thông tin", Toast.LENGTH_SHORT).show()
        }
    }
}
