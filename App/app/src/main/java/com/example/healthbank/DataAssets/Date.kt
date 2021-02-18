package com.example.healthbank.DataAssets

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.util.*
import kotlin.math.min

@RequiresApi(Build.VERSION_CODES.O)


val rawDate: String = (LocalDateTime.now()).toString()

val year = rawDate.substring(0, 4)
val month = rawDate.substring(5, 7)
val day = rawDate.substring(8, 10)
val upBound = (year + month + day).toInt()
val todayDate = (year + month + day)


@RequiresApi(Build.VERSION_CODES.O)
fun getLowBound(days:Int): Int {
    val minusDate = (LocalDateTime.now().minusDays(days.toLong())).toString()
    val year = minusDate.substring(0,4)
    val month = minusDate.substring(5,7)
    val day = minusDate.substring(8,10)
    val lowBound = (year + month + day).toInt()
    return lowBound
}




