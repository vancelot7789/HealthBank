package com.example.healthbank.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.healthbank.DataClass.DataService
import com.example.healthbank.R

internal class SelectMedicineAdapter(var c: Context, var medicines: ArrayList<DataService.medicineUsage>):
    RecyclerView.Adapter<SelectMedicineAdapter.MyHolder>(){

    private var checkallFlag = true
    var selectedMedicine = ArrayList<DataService.medicineUsage>()

    @SuppressLint("ResourceType")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectMedicineAdapter.MyHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.medicine_usage_checkbox_list, null)
        return MyHolder(v)
    }

    override fun onBindViewHolder(holder: SelectMedicineAdapter.MyHolder, position: Int) {
        val medicine = medicines[position]
        holder.medName.text = medicine.medName
        holder.amount.text = medicine.amount
        holder.days.text = medicine.days
        holder.medicalDate.text = medicine.medicalDate
        holder.checkBox.isChecked = medicine.isSelected

        holder.setItemClickListener(object : MyHolder.ItemClickListener {
            override fun onItemClick(view: View, pos: Int){
                val myCheckBox = view as CheckBox
                val currentMedicine = medicines[pos]

                if (myCheckBox.isChecked){
                    currentMedicine.isSelected = true
                    selectedMedicine.add(currentMedicine)
                }else if(!myCheckBox.isChecked){
                    currentMedicine.isSelected = false
                    selectedMedicine.remove(currentMedicine)
                }
            }
        })
    }

    fun checkAll(){
        if (!checkallFlag) {
            for (m in medicines) {
                m.isSelected = true
                selectedMedicine.add(m)
            }
        }else{
            for (m in medicines) {
                m.isSelected = false
                selectedMedicine.remove(m)
            }
        }
        checkallFlag = !checkallFlag
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return medicines.size
    }

    internal class MyHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{
        var medName: TextView
        var amount: TextView
        var days: TextView
        var medicalDate: TextView
        var checkBox: CheckBox

        lateinit var myItemClickListener: ItemClickListener
        init {
            medName = itemView.findViewById(R.id.medName)
            amount = itemView.findViewById(R.id.amount)
            days = itemView.findViewById(R.id.days)
            medicalDate = itemView.findViewById(R.id.medicalDate)
            checkBox = itemView.findViewById(R.id.checkBox)

            checkBox.setOnClickListener(this)
        }

        fun setItemClickListener(ic: ItemClickListener){
            this.myItemClickListener = ic

        }

        override fun onClick(view: View){
            this.myItemClickListener.onItemClick(view, layoutPosition)
        }

        internal interface ItemClickListener{

            fun onItemClick(view: View, pos: Int){

            }
        }
    }
}