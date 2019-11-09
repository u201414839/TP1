package pe.edu.upc.tp1dataset

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Parcelable
import kotlinx.android.synthetic.main.activity_image_view.*
import java.io.File
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.net.Uri
import com.google.firebase.storage.FirebaseStorage


class ImageViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_view)


        val intent = getIntent()

        val filename = intent.getStringExtra("Filename")

        var imgfile = File("/storage/emulated/0/Download/"+filename.toString())

        //Toast.makeText(this, "/storage/emulated/0/"+filename.toString(), Toast.LENGTH_LONG).show()

        if(imgfile.exists()){

            Toast.makeText(this, "Si existe", Toast.LENGTH_LONG).show()
            val myBitmap = BitmapFactory.decodeFile(imgfile.getAbsolutePath())


           var degrees: Float = (90).toFloat()
            val matrix = Matrix()
            matrix.postRotate(degrees);

            val rotatedBitmap = Bitmap.createBitmap(
                myBitmap,
                0,
                0,
                myBitmap.getWidth(),
                myBitmap.getHeight(),
                matrix,
                true
            )

            imageView.setImageBitmap(rotatedBitmap)


            var storage = FirebaseStorage.getInstance()

            var storageRef = storage.getReference()


            var file = Uri.fromFile(imgfile)
            val imageRef = storageRef.child("Download/${file.lastPathSegment}")
            var uploadTask = imageRef.putFile(file)

// Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener {
                // Handle unsuccessful uploads
                Toast.makeText(this, "Fallo", Toast.LENGTH_LONG).show()
            }.addOnSuccessListener {
                // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
                // ...
                Toast.makeText(this, "Exito", Toast.LENGTH_LONG).show()
            }

        }


    }
}
