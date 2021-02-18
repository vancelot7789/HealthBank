package com.example.healthbank.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import com.example.healthbank.R

class ColumnSelectMedicine : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_column_select_medicine)
    }
    fun lastStepButtonOnClicked(view: View){
        val lastStepIntent = Intent(this,FindDetailActivity::class.java)
        startActivity(lastStepIntent)
    }


    fun nextButtonOnClicked(view: View){



        val nextIntent = Intent(this, SelfSharingSelectMedicineActivity::class.java)
        startActivity(nextIntent)


    }
}
