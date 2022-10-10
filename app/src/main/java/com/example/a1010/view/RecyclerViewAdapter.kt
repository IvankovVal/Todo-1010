package com.example.a1010.view


import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.a1010.R
import com.example.a1010.model.Task
import com.example.a1010.viewmodel.TaskViewModel

class RecyclerViewAdapter(
    val tasks: List<Task>,
    private val listener: OnItemClickListener
    //var itemPosition: Int
)
    : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>(){

    private lateinit var model: TaskViewModel
    //val task = model.allTasks.value!![itemPosition]

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
        holder.chekbox.isChecked = tasks[position].status
        holder.chekbox.setOnCheckedChangeListener {buttonView, isChecked ->
            if (isChecked){
            listener.onCheckBoxClick(tasks[position], isChecked = true)
                holder.name.setTypeface(null, Typeface.ITALIC)
        }
           else{
                listener.onCheckBoxClick(tasks[position], isChecked = false)
                holder.name.setTypeface(null, Typeface.BOLD)
           }
        }}




    //________________________________________________________________________
    override fun getItemCount(): Int {
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
        fun onCheckBoxClick(task: Task,isChecked:Boolean)
    }
}