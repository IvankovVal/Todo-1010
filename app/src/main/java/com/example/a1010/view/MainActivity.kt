package com.example.a1010.view

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a1010.R
import com.example.a1010.databinding.ActivityMainBinding
import com.example.a1010.viewmodel.FilterType
import com.example.a1010.viewmodel.TaskViewModel

class MainActivity : AppCompatActivity(), RecyclerViewAdapter.OnItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var model: TaskViewModel
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        // Получаем viewModel
        model = ViewModelProviders.of(this).get(TaskViewModel::class.java)
        // Настраиваем макет recycler view
        recyclerView = findViewById(R.id.recyclerView)
        //обновление RV при достижении конца списка
        val currentActivity = this

        val linearLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = linearLayoutManager

        // Радио группа для фильтрации списка задач
        val rbGroup: RadioGroup = findViewById(R.id.fild_for_btns)
        rbGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.btn_all -> {
                    model.setFilterType(FilterType.ALL)
                }
                R.id.btn_complete -> {
                    model.setFilterType(FilterType.COMPLETE)
                }
                R.id.btn_active -> {
                    model.setFilterType(FilterType.ACTIVE)
                }
            }
        }

        //Радио кнопки
        val rbAll:RadioButton = findViewById(R.id.btn_all)
        val rbComplete:RadioButton = findViewById(R.id.btn_complete)
        val rbActive:RadioButton = findViewById(R.id.btn_active)

        val tvPageInfo:TextView = findViewById(R.id.tv_page_info)


        model.tasks.observe(this, Observer { tasks ->
            // Привязываем список задач к recycler view
           recyclerView.adapter = RecyclerViewAdapter(tasks, this)
            rbAll.setText("Все ${model.allCount }")
            rbComplete.setText("Готово ${model.completeCount}")
            rbActive.setText("В работе ${model.activeCount}")

            tvPageInfo.setText("Страница ${model.page} из ${(Math.ceil((model.allCount/8.0)).toInt())}")

            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                        model.page = model.page + 1
                        if((Math.ceil((model.allCount/8.0)).toInt() == model.page) || (Math.ceil((model.allCount/8.0)).toInt() > model.page)){
                            model.getAllTasks()
                            rbAll.isChecked = true
                           // Toast.makeText(currentActivity, "На стр.${model.page}", Toast.LENGTH_LONG).show()
                        }
                        else {model.page = model.page - 1
                           // Toast.makeText(currentActivity, "Конец стр.${model.page}", Toast.LENGTH_LONG).show()
                        }
                    }
                    if(!recyclerView.canScrollVertically(-1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                        model.page = model.page - 1
                        if(model.page !== 0){
                            model.getAllTasks()
                            rbAll.isChecked = true
                            //Toast.makeText(currentActivity, "На стр.${model.page}", Toast.LENGTH_LONG).show()
                        }
                        else {model.page = model.page + 1
                          //  Toast.makeText(currentActivity, "Начало стр.${model.page}", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            })
        })



        // Кнопка добавления задания
        val btn: Button = findViewById(R.id.btn)
        btn.setOnClickListener {

            val addDialog = AddTaskDialog()
            val manager = supportFragmentManager
            addDialog.show(manager, "add_dialog")
        }
        // Кнопка удаления выполненных заданий
        val btnDelCom: Button = findViewById(R.id.btn_del_compl)
        btnDelCom.setOnClickListener {

            model.delComTasks()
//            model.setFilterType(to = FilterType.ALL)
            changeToAll(rbGroup)

        }

    }


    override fun onItemClick(position: Int) {
        Toast.makeText(this, "Пункт $position нажат", Toast.LENGTH_LONG).show()
        val detailDialog = DetailsDialog(position)
        val manager = supportFragmentManager
        detailDialog.show(manager, "add_dialog")
    }

    override fun onCheckBoxClick(id: Int?, isChecked: Boolean) {

        model.onTaskCheckedChange(id, isChecked)
    }

    fun changeToAll(rb_group: RadioGroup) {
        rb_group.clearCheck()
        rb_group.check(R.id.btn_all)
    }

}