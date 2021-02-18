package com.example.healthbank.Activity

import android.Manifest.permission.CAMERA
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.SparseArray
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.healthbank.DataAssets.*
import com.example.healthbank.DataAssets.userAccountName
import com.example.healthbank.DataClass.CheckClass
import com.example.healthbank.R
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.zxing.Result
import kotlinx.android.synthetic.main.activity_menu.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import com.karumi.dexter.Dexter
import com.karumi.dexter.DexterBuilder
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_third_party.*
import me.dm7.barcodescanner.zxing.ZXingScannerView
import java.util.jar.Manifest

class ThirdPartyActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third_party)
        checkOutUrl.visibility = View.INVISIBLE



        Dexter.withContext(this).withPermission(android.Manifest.permission.CAMERA).withListener(object:PermissionListener{
            override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                scanView.setResultHandler(this@ThirdPartyActivity)
                scanView.startCamera()
            }

            override fun onPermissionRationaleShouldBeShown(
                p0: PermissionRequest?,
                p1: PermissionToken?
            ) {
                TODO("Not yet implemented")
            }

            override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                Toast.makeText(this@ThirdPartyActivity,"You should enable this permission",Toast.LENGTH_SHORT).show()
            }

        }).check()




    }

    fun returnMenuOnClicked(view: View){
        val returnMenuIntent = Intent(this,MenuActivity::class.java)
        startActivity(returnMenuIntent)


    }

    override fun handleResult(rawResult: Result?) {

//        resultTextView.text = rawResult!!.text
        checkOutUrl.visibility = View.VISIBLE
        eventUrl = rawResult!!.text.toString()
        getEvent(eventUrl)

        checkOutUrl.setOnClickListener {
            val findDetailIntent = Intent(this, FindDetailActivity::class.java)
            startActivity(findDetailIntent)
        }

    }

    fun getEvent(eventUrl:String){
        val event = eventUrl.substringAfterLast("?")

        val getDetailUrl = "http://$currentIp:8080/event?${event}"
        val request = Request.Builder().url(getDetailUrl).get().build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(object: Callback {

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call?, response: Response?) {
                val rawData = response?.body()!!.string()
                val jsonData = JSONObject(
                    rawData.substring(
                        rawData.indexOf("{"),
                        rawData.lastIndexOf("}") + 1
                    )
                ).getJSONObject("data")

                println("result ${jsonData}")
                eventData = jsonData

                eventType = (jsonData.getString("EventType"))
                eventName = (jsonData.getString("eventName"))
                eventInfo = (jsonData.getString("eventInfo"))
                eventRequired = (jsonData.getString("eventRequired"))
                eventRequiredDetail = (jsonData.getString("eventRequiredDetail"))


            }
            override fun onFailure(call: Call?, e: IOException?){
                if (e != null) {
                    println(e.message)
                }
            }
        })
    }
}

