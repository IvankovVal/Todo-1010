package com.example.a1010.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a1010.R
import com.example.a1010.databinding.ActivityMainBinding
import com.example.a1010.viewmodel.TaskViewModel

class MainActivity : AppCompatActivity(), RecyclerViewAdapter.OnItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var model: TaskViewModel
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        // Get the view model
        model = ViewModelProviders.of(this).get(TaskViewModel::class.java)

        // Specify layout for recycler view
        recyclerView = findViewById(R.id.recyclerView)

        val linearLayoutManager = LinearLayoutManager(
            this, RecyclerView.VERTICAL, false
        )
        recyclerView.layoutManager = linearLayoutManager


        //Текст радио-кнопок
        val tvCount: TextView = findViewById(R.id.tv_task_count)

        model.tasks.observe(this, Observer { tasks ->
            // Data bind the recycler view
            recyclerView.adapter = RecyclerViewAdapter(tasks, this)
            tvCount.setText("${model.tasks.value!!.count()}")


        })
        val rb_group: RadioGroup = findViewById(R.id.fild_for_btns)


        rb_group.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.btn_all -> {
                    model.tasks
                }
                R.id.btn_complete -> {
                    TODO()
                    // model.tasks.value!!.filter { it.status = true }
                }
                R.id.btn_active -> {
                    TODO()
                    // model.tasks.value!!.filter { it.status = false  }
                }
            }
        }
        // Кнопка добавления задания
        val btn: Button = findViewById(R.id.btn)
        btn.setOnClickListener {

            val add_dialog = AddTaskDialog()
            val manager = supportFragmentManager
            add_dialog.show(manager, "add_dialog")
        }
        // Кнопка удаления выполненных заданий
        val btnDelCom: Button = findViewById(R.id.btn_del_compl)
        btnDelCom.setOnClickListener {

            model.delComTasks()
//            model.setFilterType(to = FilterType.ALL)
            changeToAll(rb_group)

        }

        }


    override fun onItemClick(position: Int) {
        Toast.makeText(this, "Пункт $position нажат", Toast.LENGTH_LONG).show()
        val ditaile_dialog = DetailsDialog(position)
        val manager = supportFragmentManager
        ditaile_dialog.show(manager, "add_dialog")
    }

    override fun onCheckBoxClick(id: Int?, isChecked: Boolean) {

        model.onTaskCheckedChange(id, isChecked)
    }

    fun changeToAll(rb_group: RadioGroup) {
        rb_group.clearCheck()
        rb_group.check(R.id.btn_all)
    }

}