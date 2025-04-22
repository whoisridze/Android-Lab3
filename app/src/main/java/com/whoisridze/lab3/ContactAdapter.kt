package com.whoisridze.lab3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.whoisridze.lab3.databinding.ItemContactBinding

class ContactAdapter(
    private val items: MutableList<Contact>,
    private val onDelete: (Contact) -> Unit
) : RecyclerView.Adapter<ContactAdapter.VH>() {

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        private val b = ItemContactBinding.bind(view)
        fun bind(contact: Contact) {
            b.tvName.text = contact.name
            b.tvEmail.text = contact.email
            b.tvPhone.text = contact.phone

            if (contact.photoUri != null) {
                b.imgPhoto.visibility = View.VISIBLE
                b.tvNoPhoto.visibility = View.GONE
                b.imgPhoto.setImageURI(contact.photoUri)
            } else {
                b.imgPhoto.visibility = View.GONE
                b.tvNoPhoto.visibility = View.VISIBLE
            }

            b.btnDelete.setOnClickListener {
                onDelete(contact)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_contact, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun add(contact: Contact) {
        items.add(contact)
        notifyItemInserted(items.size - 1)
    }

    fun remove(contact: Contact) {
        val idx = items.indexOf(contact)
        if (idx != -1) {
            items.removeAt(idx)
            notifyItemRemoved(idx)
        }
    }
}
