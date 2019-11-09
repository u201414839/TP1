package pe.edu.upc.tp1dataset

import android.content.Intent
import android.graphics.Bitmap
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.storage.FirebaseStorage
import io.fotoapparat.Fotoapparat
import io.fotoapparat.configuration.CameraConfiguration
import io.fotoapparat.log.logcat
import io.fotoapparat.log.loggers
import io.fotoapparat.parameter.ScaleType
import io.fotoapparat.selector.back
import io.fotoapparat.selector.front
import io.fotoapparat.selector.off
import io.fotoapparat.selector.torch
import io.fotoapparat.view.CameraView
import kotlinx.android.synthetic.main.activity_camera.*
import java.io.ByteArrayOutputStream
import java.io.File
import android.R.attr.bitmap
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.provider.MediaStore
import io.fotoapparat.result.BitmapPhoto


class CameraActivity : AppCompatActivity() {

    var fotoapparat: Fotoapparat? = null
    var filename = "test.png"
    val sd = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    var fotoapparatState: FotoapparatState? = null
    var cameraStatus: CameraState? = null
    var flashState: FlashState? = null

    override fun onStart() {
        super.onStart()
        fotoapparat?.start()
        fotoapparatState = FotoapparatState.ON
    }

    private fun createFotoapparat() {
        val cameraView = findViewById<CameraView>(R.id.camera_view)

        fotoapparat = Fotoapparat(
            context = this,
            view = cameraView,
            scaleType = ScaleType.CenterCrop,
            lensPosition = back(),
            logger = loggers(
                logcat()
            ),
            cameraErrorCallback = { error ->
                println("Recorder errors: $error")
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        createFotoapparat()

        cameraStatus = CameraState.BACK
        flashState = FlashState.OFF
        fotoapparatState = FotoapparatState.OFF

        fab_camera.setOnClickListener {

            if (tvCodigo.text.isEmpty()) {
                Toast.makeText(this, "Por favor ingrese su codigo", Toast.LENGTH_LONG).show()
            } else
                if (!tvCodigo.text.toString().trim().startsWith("u201")
                    || tvCodigo.text.toString().trim().length != 10
                ) {
                    Toast.makeText(this, "Ingrese su codigo de forma correcta.", Toast.LENGTH_LONG)
                        .show()
                } else {
                    filename = tvCodigo.text.toString().trim() + ".png"
                    takePhoto()
                }
        }

        fab_switch_camera.setOnClickListener {
            switchCamera()
        }

        fab_flash.setOnClickListener {
            changeFlashState()
        }

        btnSend.setOnClickListener {
            val mainIntent = Intent(this, MainActivity::class.java)
            startActivity(mainIntent)
        }
    }

    private fun takePhoto() {


        fotoapparat
            ?.takePicture()
            ?.saveToFile(File(sd, filename))?.whenAvailable {

                Toast.makeText(
                    this,
                    "/storage/emulated/0/Download/" + filename.toString(),
                    Toast.LENGTH_LONG
                ).show()

                val intent = Intent(this, ImageViewActivity::class.java)
                intent.putExtra("Filename", filename)
                startActivity(intent)


            }


        /*var storage = FirebaseStorage.getInstance()

        var storageRef = storage.getReference()

        var imagepath = (sd).toString() + "/" + filename

        var fileforupload = Uri.fromFile(File(imagepath))

        //Toast.makeText(this, (storageRef.bucket).toString(), Toast.LENGTH_LONG).show()
        //Toast.makeText(this, imagepath, Toast.LENGTH_LONG).show()
*/
/*
        val capturedphoto = fotoapparat?.takePicture()

        var bitmapfoto: Bitmap? = Bitmap.createBitmap(
            15, // Width
            15, // Height
            Bitmap.Config.ARGB_8888)

        capturedphoto
            ?.toBitmap()
            ?.whenAvailable{ bitmapPhoto ->

                var bitmapfoto2 = bitmapPhoto?.bitmap

                val intent = Intent(this, ImageViewActivity::class.java)
                intent.putExtra("bitmap",bitmapfoto2)
                startActivity(intent)

            }*/


    }


    private fun switchCamera() {
        fotoapparat?.switchTo(
            lensPosition = if (cameraStatus == CameraState.BACK) front() else back(),
            cameraConfiguration = CameraConfiguration()
        )

        if (cameraStatus == CameraState.BACK) cameraStatus = CameraState.FRONT
        else cameraStatus = CameraState.BACK
    }

    private fun changeFlashState() {
        fotoapparat?.updateConfiguration(
            CameraConfiguration(
                flashMode = if (flashState == FlashState.TORCH) off() else torch()
            )
        )

        if (flashState == FlashState.TORCH) flashState = FlashState.OFF
        else flashState = FlashState.TORCH
    }

    override fun onStop() {
        super.onStop()
        fotoapparat?.stop()
        FotoapparatState.OFF
    }

}

enum class CameraState {
    FRONT, BACK
}

enum class FlashState {
    TORCH, OFF
}

enum class FotoapparatState {
    ON, OFF
}
