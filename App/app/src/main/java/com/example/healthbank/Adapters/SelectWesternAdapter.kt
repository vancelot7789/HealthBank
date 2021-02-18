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

internal class SelectWesternAdapter(var c: Context, var westerns: ArrayList<DataService.WesternMedicine>):
    RecyclerView.Adapter<SelectWesternAdapter.MyHolder>(){

    var selectedWestern = ArrayList<DataService.WesternMedicine>()
    var checkallFlag = true

    @SuppressLint("ResourceType")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.western_checkbox_list, null)
        return MyHolder(v)
    }

    override fun onBindViewHolder(holder: SelectWesternAdapter.MyHolder, position: Int) {
        val western = westerns[position]
        holder.diseaseName.text = western.diseaseName
        holder.hospitalName.text = western.hospitalName
        holder.medicalDate.text = western.medicalDate
        holder.checkBox.isChecked = western.isSelected

        holder.setItemClickListener(object : MyHolder.ItemClickListener {
            override fun onItemClick(view: View, pos: Int){
                val myCheckBox = view as CheckBox
                val currentWestern = westerns[pos]

                if (myCheckBox.isChecked){
                    currentWestern.isSelected = true
                    selectedWestern.add(currentWestern)
                }else if(!myCheckBox.isChecked){
                    currentWestern.isSelected = false
                    selectedWestern.remove(currentWestern)
                }
            }
        })
    }

    fun checkAll(){
        if (!checkallFlag) {
            for (w in westerns) {
                w.isSelected = true
                selectedWestern.add(w)
            }
        }else{
            for (w in westerns) {
                w.isSelected = false
                selectedWestern.remove(w)
            }
        }
        checkallFlag = !checkallFlag
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return westerns.size
    }

    internal class MyHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{
        var diseaseName: TextView
        var hospitalName: TextView
        var medicalDate: TextView
        var checkBox: CheckBox

        lateinit var myItemClickListener: ItemClickListener
        init {
            diseaseName = itemView.findViewById(R.id.diseaseName)
            hospitalName = itemView.findViewById(R.id.hospitalName)
            medicalDate = itemView.findViewById(R.id.medicalDate)
            checkBox = itemView.findViewById(R.id.westernRoundCheckBox)
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