package com.example.healthbank.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import com.example.healthbank.R

class ColumnSelectWestern : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_column_select_western)
    }
    fun lastStepButtonOnClicked(view: View){
        val lastStepIntent = Intent(this, SelfSharingDisplayMedicineActivity::class.java)
        startActivity(lastStepIntent)
    }

    fun nextButtonOnClicked(view: View){



        val nextStepIntent = Intent(this, SelfSharingSelectWesternActivity::class.java)
        startActivity(nextStepIntent)

    }
}
