package com.whoisridze.lab3

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Contact(
    val name: String,
    val email: String,
    val phone: String,
    val photoUri: Uri?
) : Parcelable
