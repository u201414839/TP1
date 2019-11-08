package pe.edu.upc.sharedpreferences

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPreferences = getSharedPreferences("SP_INFO", Context.MODE_PRIVATE)

        saveBtn.setOnClickListener {
            val name = nameEt.text.toString().trim()
            val age = Integer.parseInt(ageEt.text.toString().trim())
            val experience = switchh.isChecked

            val editor = sharedPreferences.edit()
            editor.putString("NAME", name)
            editor.putInt("AGE", age)
            editor.putBoolean("EXPERIENCED", experience)
            editor.apply()
        }

        showInfoBtn.setOnClickListener {
            val intent = Intent(this,ShowActivity::class.java)

            startActivity(intent)


        }
    }
}
