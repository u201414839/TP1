package pe.edu.upc.kotlinfirebasestorage

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var alertDialog: AlertDialog
    lateinit var storageReference: StorageReference

    companion object{
        private val PICK_IMAGE_CODE = 1000
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
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
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Init

        alertDialog = SpotsDialog.Builder().setContext(this).build();
        storageReference = FirebaseStorage.getInstance().getReference("image_upload") //file_name

        //Event
        btn_upload.setOnClickListener {
            //Write code to pickup image
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_CODE)
        }


    }
}
