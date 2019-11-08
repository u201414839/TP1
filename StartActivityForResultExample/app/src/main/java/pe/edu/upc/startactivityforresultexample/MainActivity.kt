package pe.edu.upc.startactivityforresultexample

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPreferences = getSharedPreferences("SP_INFO", Context.MODE_PRIVATE)

        button_open_activity2.setOnClickListener {
            if(edit_text_number1.text.toString().trim().equals("") || edit_text_number2.text.toString().trim().equals("")){
                Toast.makeText(this,"Please insert numbers",Toast.LENGTH_SHORT).show()
            } else {
                var number1 = Integer.parseInt(edit_text_number1.text.toString().trim())
                var number2 = Integer.parseInt(edit_text_number2.text.toString().trim())

                val intent = Intent(this@MainActivity, Activity2::class.java)
                intent.putExtra("number1", number1)
                intent.putExtra("number2", number2)

                startActivityForResult(intent, 1)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if( requestCode == 1){
            if(resultCode == Activity.RESULT_OK){
                var result = data?.getIntExtra("result", 0)

                text_view_result.text = "" + result
            }
            if(resultCode == Activity.RESULT_CANCELED){
                text_view_result.text = "Nothing selected"
            }
        }
    }
}
