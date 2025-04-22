package com.whoisridze.lab3

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.whoisridze.lab3.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var b: ActivityMainBinding
    private val contacts = mutableListOf<Contact>()
    private lateinit var adapter: ContactAdapter

    private val addContactLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.getParcelableExtra<Contact>("contact")?.let {
                adapter.add(it)
                toggleEmptyView()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)

        adapter = ContactAdapter(contacts) { contact ->
            adapter.remove(contact)
            toggleEmptyView()
        }
        b.recyclerView.layoutManager = LinearLayoutManager(this)
        b.recyclerView.adapter = adapter

        b.fabAdd.setOnClickListener {
            val intent = Intent(this, AddContactActivity::class.java)
            addContactLauncher.launch(intent)
        }

        toggleEmptyView()
    }

    private fun toggleEmptyView() {
        if (contacts.isEmpty()) {
            b.emptyView.visibility = View.VISIBLE
            b.recyclerView.visibility = View.GONE
        } else {
            b.emptyView.visibility = View.GONE
            b.recyclerView.visibility = View.VISIBLE
        }
    }
}
