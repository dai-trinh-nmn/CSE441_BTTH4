package com.example.firebase

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
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
import com.example.firebase.ui.theme.FirebaseTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var buttonRegister: Button
    private lateinit var buttonLogin: Button
    private lateinit var buttonShowData: Button
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    val databaseurl = "https://testproject-b6f19-default-rtdb.asia-southeast1.firebasedatabase.app/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()

        buttonRegister = findViewById(R.id.btnRegister)
        buttonLogin = findViewById(R.id.btnLogin)
        buttonShowData = findViewById(R.id.btnShowData)
        edtEmail = findViewById(R.id.etEmail)
        edtPassword = findViewById(R.id.etPassword)

        buttonRegister.setOnClickListener {
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show()
                    } else {
                        val errorCode = (task.exception as? FirebaseAuthException)?.errorCode
                        val errorMessage = when (errorCode) {
                            "ERROR_WEAK_PASSWORD" -> "Mật khẩu quá yếu!"
                            "ERROR_EMAIL_ALREADY_IN_USE" -> "Email đã được sử dụng!"
                            "ERROR_INVALID_EMAIL" -> "Email không hợp lệ!"
                            else -> "Đăng ký thất bại: ${task.exception?.message}"
                        }
                        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }

            clearFields()
        }

        buttonLogin.setOnClickListener {
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        clearFields()
                        val user = auth.currentUser

                        if (user != null) {
                            val database = FirebaseDatabase.getInstance(databaseurl)
                            val userRef = database.getReference("users/${user.uid}")

                            val userData = hashMapOf(
                                "email" to user.email,
                                "lastLogin" to ServerValue.TIMESTAMP
                            )

                            userRef.setValue(userData)
                        }

                        Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Đăng nhập thất bại!", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        buttonShowData.setOnClickListener {
            val database = FirebaseDatabase.getInstance(databaseurl)
            val userRef = database.getReference("users")

            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val users = snapshot.children.joinToString("\n") { child ->
                            "User: ${child.child("email").value}"
                        }

                        if (users.isEmpty()) {
                            Toast.makeText(this@MainActivity, "Không có dữ liệu người dùng!", Toast.LENGTH_SHORT).show()
                        } else {
                            AlertDialog.Builder(this@MainActivity)
                                .setTitle("Dữ liệu người dùng")
                                .setMessage(users)
                                .show()
                        }
                    } else {
                        Toast.makeText(this@MainActivity, "Không có dữ liệu người dùng!", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@MainActivity, "Lỗi khi đọc dữ liệu!", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    fun clearFields() {
        edtEmail.text.clear()
        edtPassword.text.clear()
    }
}

