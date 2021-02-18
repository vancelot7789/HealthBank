package com.example.healthbank.Adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.healthbank.DataClass.DataService
import com.example.healthbank.R

class ExamineAdapter(val context: Context, val products: Array<DataService.examineInfo>) : RecyclerView.Adapter<ExamineAdapter.ProductHolder>() {
    inner class ProductHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        val medicalDate = itemView?.findViewById<TextView>(R.id.medicalDate)
        val diseaseName = itemView?.findViewById<TextView>(R.id.diseaseName)
        val examineName = itemView?.findViewById<TextView>(R.id.examineName)
        val result = itemView?.findViewById<TextView>(R.id.result)
        val reference = itemView?.findViewById<TextView>(R.id.reference)
        val hospitalName = itemView?.findViewById<TextView>(R.id.hospitalName)

        fun bindProduct(product: DataService.examineInfo, context: Context){
//            medicalDate?.text = "檢查日期： "
//            medicalDate.append(product.medicalDate)

            medicalDate?.text = product.medicalDate

//            diseaseName?.text = "疾病名稱： "
//            diseaseName.append(product.diseaseName)
            diseaseName?.text = product.diseaseName

//            examineName?.text = "檢查項目： "
//            examineName.append(product.examineName)
            examineName?.text = product.examineName

//            result?.text = "檢查結果： "
//            result.append(product.result)
            result?.text = product.result

//            reference?.text = "參考數值： "
//            reference.append(product.reference)
            reference?.text = product.reference

//            hospitalName?.text = "醫療院所： "
////            hospitalName.append(product.hospitalName)
            hospitalName?.text = product.hospitalName


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.examine_list_item,parent,false)
        return ProductHolder(view)
    }

    override fun getItemCount(): Int {
        return products.count()
    }



    override fun onBindViewHolder(holder: ProductHolder, position: Int) {
        holder?.bindProduct(products[position],context)
    }
}


