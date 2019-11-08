package pe.edu.upc.tp1dataset

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import dmax.dialog.SpotsDialog
import io.fotoapparat.Fotoapparat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity() {

    lateinit var storage: FirebaseStorage

    val permissions = arrayOf(
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    )

    override fun onStart() {
        super.onStart()
        if (hasNoPermissions()) {
            requestPermission()
        }
    }

    private fun hasNoPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) != PackageManager.PERMISSION_GRANTED
    }

    fun requestPermission() {
        ActivityCompat.requestPermissions(this, permissions, 0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        btn_upload.setOnClickListener {
            if (!hasNoPermissions()) {
                val cameraIntent = Intent(this, CameraActivity::class.java)
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(cameraIntent, 1)
            } else {
                requestPermission()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        var filename = data?.getStringExtra("filename")



        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        var file = Uri.fromFile((File(filename)))

        val riversRef = storageRef.child("images/${file.lastPathSegment}")

        Toast.makeText(this@MainActivity, riversRef.toString(), Toast.LENGTH_SHORT).show()


        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, file.toString(), Toast.LENGTH_LONG).show()

                val uploadTask = riversRef.putFile(file)
                val task = uploadTask.continueWithTask { task ->
                    if (!task.isSuccessful) {
                        print("ERROR AQUI"+ task.exception)
                        Toast.makeText(this@MainActivity, "Failed", Toast.LENGTH_SHORT).show()
                    }
                    storageRef!!.downloadUrl
                }.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUri = task.result
                        val url = downloadUri!!.toString()
                            .substring(0, downloadUri.toString().indexOf("&token"))
                        Log.d("DIRECTLINK", url)
                        Picasso.get().load(url).into(image_view)
                    }
                }

            }
        }
    }
}


