package com.example.a1010.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.example.a1010.R
import com.example.a1010.viewmodel.TaskViewModel

class AddTaskDialog: DialogFragment() {

    private lateinit var model: TaskViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.add_dialog,container,false)
        val btn_save: Button = view.findViewById(R.id.btn_save)
        val btn_cancel: Button = view.findViewById(R.id.btn_cancel)
        val et_add_task: EditText = view.findViewById(R.id.et_add_task)
        model = ViewModelProviders.of(this).get(TaskViewModel::class.java)

        //Кнопка выключения диалога
        btn_cancel.setOnClickListener {
            dialog?.cancel()
        }

        btn_save.setOnClickListener {
            model.insert(et_add_task.text.toString(),0 )
            dialog?.cancel()

        }


        return view
    }
}
