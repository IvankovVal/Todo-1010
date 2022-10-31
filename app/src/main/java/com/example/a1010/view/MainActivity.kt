package com.example.a1010.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a1010.R
import com.example.a1010.model.TaskModel
import com.example.a1010.viewmodel.FilterType
import com.example.a1010.viewmodel.TaskViewModel

class MainActivity : AppCompatActivity(), RecyclerViewAdapter.OnItemClickListener {

    private lateinit var model: TaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get the view model
        model = ViewModelProviders.of(this).get(TaskViewModel::class.java)

        // Specify layout for recycler view
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        val linearLayoutManager = LinearLayoutManager(
            this, RecyclerView.VERTICAL, false
        )
        recyclerView.layoutManager = linearLayoutManager

        //Текст радио-кнопок
        val all: RadioButton = findViewById(R.id.btn_all)
        val complete: RadioButton = findViewById(R.id.btn_complete)
        val active: RadioButton = findViewById(R.id.btn_active)



        model.tasks.observe(this, Observer { tasks ->
            // Data bind the recycler view
            recyclerView.adapter = RecyclerViewAdapter(tasks, this)
        })
        val rb_group: RadioGroup = findViewById(R.id.fild_for_btns)

        rb_group.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.btn_all -> {
                    model.allTasks()
                    model.setFilterType(FilterType.ALL)

                    all.setText("Все ${model.counterAll.size}")
                }
                R.id.btn_complete -> {
                    model.completeTasks()
                    model.setFilterType(FilterType.COMPLETE)
                    complete.setText("Готово ${model.counterComplete.size}")
                }
                R.id.btn_active -> {
                    model.activeTasks()
                    model.setFilterType(FilterType.ACTIVE)
                    active.setText("В работе ${model.counterActive.size}")
                }
            }
        }

        //  var sizeAll =     //model.tasks.value?:0
//
//        all.setText("Все $sizeAll")

        // Кнопка добавления задания
        val btn: Button = findViewById(R.id.btn)
        btn.setOnClickListener {
            val add_dialog = AddTaskDialog()
            val manager = supportFragmentManager
            add_dialog.show(manager, "add_dialog")
        }
    }

    override fun onItemClick(position: Int) {
        Toast.makeText(this, "Пункт $position нажат", Toast.LENGTH_LONG).show()
        val ditaile_dialog = DetailsDialog(position)
        val manager = supportFragmentManager
        ditaile_dialog.show(manager, "add_dialog")
    }

    override fun onCheckBoxClick(task: TaskModel, isChecked: Int) {
        model.onTaskCheckedChange(task, isChecked)
    }
}