package com.whoisridze.lab3

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.whoisridze.lab3.databinding.ActivityAddContactBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class AddContactActivity : AppCompatActivity() {

    private lateinit var b: ActivityAddContactBinding
    private var photoUri: Uri? = null

    private val requestCameraPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            launchCamera()
        } else {
            Toast.makeText(this,
                "Без дозволу камери зробити фото неможливо",
                Toast.LENGTH_SHORT).show()
        }
    }

    private val takePhotoLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            b.imgPreview.setImageURI(photoUri)
            b.imgPreview.visibility = View.VISIBLE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityAddContactBinding.inflate(layoutInflater)
        setContentView(b.root)

        b.btnTakePhoto.setOnClickListener {
            when {
                ContextCompat.checkSelfPermission(
                    this, Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED -> {
                    launchCamera()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                    Toast.makeText(this,
                        "Нам потрібна камера, щоб сфотографувати контакт",
                        Toast.LENGTH_SHORT).show()
                    requestCameraPermission.launch(Manifest.permission.CAMERA)
                }
                else -> {
                    requestCameraPermission.launch(Manifest.permission.CAMERA)
                }
            }
        }

        b.btnAdd.setOnClickListener {
            val validators = listOf(
                Triple(b.etName,  b.tilName,  "Введіть ім’я"),
                Triple(b.etEmail, b.tilEmail, "Введіть email"),
                Triple(b.etPhone, b.tilPhone, "Введіть телефон")
            )

            val allValid = validators.map { (edit, layout, msg) ->
                val ok = edit.text.toString().isNotBlank()
                layout.error = if (ok) null else msg
                ok
            }.all { it }

            if (!allValid) {
                Toast.makeText(this,
                    "Заповніть всі обов’язкові поля",
                    Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val contact = Contact(
                b.etName.text.toString().trim(),
                b.etEmail.text.toString().trim(),
                b.etPhone.text.toString().trim(),
                photoUri
            )
            setResult(Activity.RESULT_OK,
                Intent().putExtra("contact", contact))
            finish()
        }
    }

    private fun launchCamera() {
        val photoFile = createImageFile()
        photoUri = FileProvider.getUriForFile(
            this,
            "${packageName}.fileprovider",
            photoFile
        )

        val camIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri)
            addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        }
        takePhotoLauncher.launch(camIntent)
    }

    private fun createImageFile(): File {
        val ts = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
            .format(Date())
        return File.createTempFile("JPEG_${ts}_", ".jpg", cacheDir)
    }
}
