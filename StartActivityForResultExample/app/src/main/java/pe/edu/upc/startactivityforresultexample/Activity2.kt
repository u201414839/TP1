package pe.edu.upc.startactivityforresultexample

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_2.*


class Activity2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_2)

        setTitle("Activity 2")

        val intent = getIntent()
        val number1 = intent.getIntExtra("number1", 0)
        val number2 = intent.getIntExtra("number2", 0)

        text_view_numbers.text = "Numbers: " + number1 + ", " + number2

        button_add.setOnClickListener {
            val result = number1 + number2

            val resultIntent = Intent()
            resultIntent.putExtra("result", result)

            setResult(RESULT_OK, resultIntent);
            finish();
        }

        button_subtract.setOnClickListener {
            val result = number1 - number2

            val resultIntent = Intent()
            resultIntent.putExtra("result", result)

            setResult(RESULT_OK, resultIntent);
            finish();
        }


    }
}
