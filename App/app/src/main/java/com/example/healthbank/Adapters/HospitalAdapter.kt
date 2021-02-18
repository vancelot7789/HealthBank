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

class HospitalAdapter(val context: Context, val products: Array<DataService.hospitalInfo>) : RecyclerView.Adapter<HospitalAdapter.ProductHolder>() {
    inner class ProductHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        val inHospitalDate = itemView?.findViewById<TextView>(R.id.inHospitalDate)
        val outHospitalDate = itemView?.findViewById<TextView>(R.id.outHospitalDate)
        val diseaseName = itemView?.findViewById<TextView>(R.id.diseaseName)
        val operationName = itemView?.findViewById<TextView>(R.id.operationName)
        val hospitalName = itemView?.findViewById<TextView>(R.id.hospitalName)

        fun bindProduct(product: DataService.hospitalInfo, context: Context){
//            inHospitalDate?.text = "住院日期： "
//            inHospitalDate.append(product.inHospitalDate)
            inHospitalDate?.text = product.inHospitalDate

//            outHospitalDate?.text = "出院日期： "
//            outHospitalDate.append(product.outHospitalDate)
            outHospitalDate?.text = product.outHospitalDate

//            diseaseName?.text = "疾病名稱： "
//            diseaseName.append(product.diseaseName)
            diseaseName?.text = product.diseaseName

//            operationName?.text = "手術名稱： "
//            operationName.append(product.operationName)
            operationName?.text = product.operationName

//            hospitalName?.text = "醫療院所： "
//            hospitalName.append(product.hospitalName)
            hospitalName?.text = product.hospitalName


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.hospital_list_item,parent,false)
        return ProductHolder(view)
    }

    override fun getItemCount(): Int {
        return products.count()
    }



    override fun onBindViewHolder(holder: ProductHolder, position: Int) {
        holder?.bindProduct(products[position],context)
    }
}