package com.example.sqlite

import android.os.Bundle
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
import com.example.sqlite.ui.theme.SQLiteTheme

class MainActivity : ComponentActivity() {
    private lateinit var etName: EditText
    private lateinit var etPhone: EditText
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var btnAdd: Button
    private lateinit var btnShow: Button
    private lateinit var btnDelete: Button
    private lateinit var btnEdit: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DatabaseHelper(this)
        etName = findViewById(R.id.et_name)
        etPhone = findViewById(R.id.et_phone)

        btnAdd = findViewById(R.id.btn_add)
        btnShow = findViewById(R.id.btn_show)
        btnDelete = findViewById(R.id.btn_delete)
        btnEdit = findViewById(R.id.btn_edit)

        btnAdd.setOnClickListener {
            val name = etName.text.toString()
            val phone = etPhone.text.toString()
            if (name.isNotEmpty() && phone.isNotEmpty()) {
                dbHelper.addContact(name, phone)
                Toast.makeText(this, "Contact added successfully", Toast.LENGTH_SHORT).show()
                clearFields()
            } else {
                Toast.makeText(this, "Please enter name and phone", Toast.LENGTH_SHORT).show()
            }
        }

        btnEdit.setOnClickListener {
            val name = etName.text.toString()
            val phone = etPhone.text.toString()
            if (name.isNotEmpty() && phone.isNotEmpty()) {
                val contacts = dbHelper.getAllContacts()
                if (contacts.isNotEmpty()) {
                    val updatedRows = dbHelper.updateContact(contacts[0].first, name, phone)
                    Toast.makeText(this, "Contact updated successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "No contacts found", Toast.LENGTH_SHORT).show()
                }
                clearFields()
            } else {
                Toast.makeText(this, "Please enter name and phone", Toast.LENGTH_SHORT).show()
            }
        }

        btnDelete.setOnClickListener {
            val contacts = dbHelper.getAllContacts()
            if (contacts.isNotEmpty()) {
                val deletedRows = dbHelper.deleteContact(contacts[0].first)
                Toast.makeText(this, "Contact deleted successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No contacts found", Toast.LENGTH_SHORT).show()
            }
            clearFields()
        }

        btnShow.setOnClickListener {
            val contacts = dbHelper.getAllContacts()
            if (contacts.isNotEmpty()) {
                val contact = contacts[0]
                etName.setText(contact.second.split(" - ")[0])
                etPhone.setText(contact.second.split(" - ")[1])
            } else {
                Toast.makeText(this, "No contacts found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun clearFields() {
        etName.text.clear()
        etPhone.text.clear()
    }
}
