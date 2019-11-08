package pe.edu.upc.sharedpreferences

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_show.*

class ShowActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        val sharedPreferences = getSharedPreferences("SP_INFO", Context.MODE_PRIVATE)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show)

        val name = sharedPreferences.getString("NAME", "")
        val age = sharedPreferences.getInt("AGE", 0)
        val experience = sharedPreferences.getBoolean("EXPERIENCED", false)

        infoTv.text = "Name: $name \nAge: $age \nExperience: $experience"

        backBtn.setOnClickListener { finish() }
    }
}
