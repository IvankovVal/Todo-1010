package com.example.a1010.view


import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.a1010.R
import com.example.a1010.model.TaskModel
import com.example.a1010.viewmodel.TaskViewModel

class RecyclerViewAdapter(
    val tasks: List<TaskModel>,
    private val listener: OnItemClickListener
)
    : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>(){

    var dubList = tasks

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : ViewHolder {
        val v: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_view,parent,false)
        return ViewHolder(v)
    }
    //________________________________________________________________________
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.id.text = tasks[position].id.toString()
        holder.name.text = tasks[position].name
        holder.chekbox.isChecked = if(tasks[position].status == 1) true else false
        if (holder.chekbox.isChecked)holder.listCard.setCardBackgroundColor(Color.GRAY)
        else holder.listCard.setCardBackgroundColor(Color.WHITE)

        holder.chekbox.setOnCheckedChangeListener {buttonView, isChecked ->
            if (isChecked){
            listener.onCheckBoxClick(tasks[position], isChecked = 1)
                holder.name.setTypeface(null, Typeface.ITALIC)
                holder.listCard.setCardBackgroundColor(Color.GRAY)
        }
           else{
                listener.onCheckBoxClick(tasks[position], isChecked = 0)
                holder.name.setTypeface(null, Typeface.BOLD)
                holder.listCard.setCardBackgroundColor(Color.WHITE)
           }
        }}




    //________________________________________________________________________
    override  fun getItemCount(): Int {
        return tasks.size
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView),
        View.OnClickListener{
        val id: TextView = itemView.findViewById(R.id.tvId)
        val name: TextView = itemView.findViewById(R.id.tvName)
        val chekbox: CheckBox = itemView.findViewById(R.id.cbStatus)
        val listCard:CardView = itemView.findViewById(R.id.list_card)

        init {
            itemView.setOnClickListener(this)
        }


        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION){
                listener.onItemClick(position)}
        }

    }
    interface OnItemClickListener{
        fun onItemClick(position: Int)
        fun onCheckBoxClick(task: TaskModel,isChecked:Int)
//        fun refresh()
    }
}