package com.yvs.movy.sign.signup

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.yvs.movy.home.HomeActivity
import com.yvs.movy.R
import com.yvs.movy.utils.Preferences
import kotlinx.android.synthetic.main.activity_sign_up_photo_screen.*
import java.util.*

class SignUpPhotoScreenActivity : AppCompatActivity(), PermissionListener {

    val REQUEST_IMAGE_CAPTURE =1
    var statusAdd:Boolean = false
    lateinit var filePath: Uri

    lateinit var storage : FirebaseStorage
    lateinit var storageReference: StorageReference
    lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_photo_screen)

        preferences = Preferences(this)
        storage = FirebaseStorage.getInstance()
        storageReference = storage.getReference()

        tv_hello.text = "Selamat Datang,\n" + intent.getStringExtra("nama")

        iv_add.setOnClickListener {
            if (statusAdd) {
                statusAdd = false
                btn_save.visibility = View.VISIBLE
                iv_add.setImageResource(R.drawable.ic_add)
                iv_profile.setImageResource(R.drawable.ic_profile)
            } else {
                Dexter.withActivity(this)
                    .withPermission(Manifest.permission.CAMERA)
                    .withListener(this)
                    .check()
            }
        }

        btn_home.setOnClickListener {
            var intent = Intent(this@SignUpPhotoScreenActivity, HomeActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }

        btn_save.setOnClickListener {
            if (filePath != null) {
                var progressDialog = ProgressDialog(this)
                progressDialog.setTitle("Uploading...")
                progressDialog.show()

                var ref = storageReference.child("images/" + UUID.randomUUID().toString())
                ref.putFile(filePath)
                    .addOnSuccessListener {
                        progressDialog.dismiss()
                        Toast.makeText(
                            this@SignUpPhotoScreenActivity,
                            "Uploaded",
                            Toast.LENGTH_LONG
                        ).show()

                        ref.downloadUrl.addOnSuccessListener {
                            preferences.setValues("url", it.toString())
                        }

                        finishAffinity()

                        var intent =
                            Intent(this@SignUpPhotoScreenActivity, HomeActivity::class.java)
                        startActivity(intent)
                    }
                    .addOnFailureListener {
                        progressDialog.dismiss()
                        Toast.makeText(this@SignUpPhotoScreenActivity, "Failed", Toast.LENGTH_LONG)
                            .show()
                    }
                    .addOnProgressListener { taskSnapshot ->
                        var progress =
                            100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                        progressDialog.setMessage("Upload " + progress.toInt() + " %")
                    }

            } else {

            }
        }
    }

        override fun onPermissionGranted(response: PermissionGrantedResponse?) {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                takePictureIntent.resolveActivity(packageManager)?.also {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }

        override fun onPermissionDenied(response: PermissionDeniedResponse?) {
            Toast.makeText(
                this@SignUpPhotoScreenActivity,
                "Anda Tidak Bisa Menambahkan Foto Profile",
                Toast.LENGTH_LONG
            ).show()
        }

        override fun onPermissionRationaleShouldBeShown(
            permission: PermissionRequest?,
            token: PermissionToken?
        ) {
            TODO("Not yet implemented")
        }

        override fun onBackPressed() {
            Toast.makeText(
                this@SignUpPhotoScreenActivity,
                "Tergesa-gesa? Klik Tombol Upload Nanti Aja",
                Toast.LENGTH_LONG
            ).show()
        }

        @SuppressLint("MissingSuperCall")
        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
                var bitmap = data?.extras?.get("data") as Bitmap
                statusAdd = true

                filePath = data.getData()!!
                val iv_profile = findViewById<ImageView>(R.id.iv_profile)
                val iv_add = findViewById<ImageView>(R.id.iv_add)
                val btn_save = findViewById<Button>(R.id.btn_save)
                Glide.with(this)
                    .load(bitmap)
                    .apply(RequestOptions.circleCropTransform())
                    .into(iv_profile)

                btn_save.visibility = View.VISIBLE
                iv_add.setImageResource(R.drawable.ic_delete)
            }
        }
    }
