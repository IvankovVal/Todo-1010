package com.example.a1010.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.example.a1010.databinding.DetaileDialogBinding
import com.example.a1010.viewmodel.TaskViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DetailsDialog(var itemPosition: Int) : BottomSheetDialogFragment() {

    private var binding: DetaileDialogBinding? = null
    private lateinit var model: TaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DetaileDialogBinding.inflate(inflater, container, false)


        model = ViewModelProviders.of(requireActivity()).get(TaskViewModel::class.java)

        //положили в переменную конкретный пункт списка с которым будем работать
        val task = model.tasks.value!![itemPosition]  ////value!!


        binding?.etNametaskDetails?.setText("${task.name}")

        //Кнопка выключения диалога
        binding?.btnCancel?.setOnClickListener {
            dialog?.cancel()
        }

        //Кнопка удаления задачи
        binding?.btnDelete?.setOnClickListener {

            model.delete_task(task.id!!.toInt())
            model.getAllTasks()
            dialog?.cancel()
        }

        //Кнопка редактирования задачи
        binding?.btnEdit?.setOnClickListener {
            if(binding?.etNametaskDetails?.text.toString() == "" && binding?.etNametaskDetails?.text!!.length < 3 && binding?.etNametaskDetails?.text!!.length > 50){
                Toast.makeText(context,"Не верный формат ввода", Toast.LENGTH_LONG)
            }
            else{

            //в таск кладём id этого task и имя задачи из edittext
            model.update_task(
                task.id!!,
                    binding?.etNametaskDetails?.text.toString(),
                    task.status
            )
            model.getAllTasks()

            dialog?.cancel()}
        }


        return binding?.root
    }
}