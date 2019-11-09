package pe.edu.upc.tp1dataset

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
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

class MainActivity : AppCompatActivity() {

    lateinit var alertDialog: AlertDialog
    lateinit var storageReference: StorageReference

    var filename  = ""

    companion object{
        private val PICK_IMAGE_CODE = 1000
    }

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

    private fun hasNoPermissions(): Boolean{
        return ContextCompat.checkSelfPermission(this,
            Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
            Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
    }

    fun requestPermission(){
        ActivityCompat.requestPermissions(this, permissions,0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        btn_upload.setOnClickListener{
            val cameraIntent = Intent(this, CameraActivity::class.java)

            if (!hasNoPermissions()) {
                //startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_CODE)
                startActivityForResult(cameraIntent, PICK_IMAGE_CODE)

                //alertDialog = SpotsDialog.Builder().setContext(this).build()
                //storageReference = FirebaseStorage.getInstance().getReference(filename)
            }else
            {
                requestPermission()
            }
        }
    }

    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == PICK_IMAGE_CODE)
        {
            alertDialog.show()
            val uploadTask = storageReference!!.putFile(data!!.data!!)
            val task = uploadTask.continueWithTask{
                    task ->
                if(!task.isSuccessful)
                {
                    Toast.makeText(this@MainActivity,"Failed",Toast.LENGTH_SHORT).show()
                }
                storageReference!!.downloadUrl
            }.addOnCompleteListener {task ->
                if(task.isSuccessful)
                {
                    val downloadUri = task.result
                    val url = downloadUri!!.toString().substring(0,downloadUri.toString().indexOf("&token"))
                    Log.d("DIRECTLINK",url)
                    alertDialog.dismiss()
                    Picasso.get().load(url).into(image_view)
                }

            }
        }
    }*/
}


