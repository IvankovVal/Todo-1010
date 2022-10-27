package com.example.a1010.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.example.a1010.databinding.DetaileDialogBinding
import com.example.a1010.model.TaskModel
import com.example.a1010.viewmodel.TaskViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DetailsDialog(var itemPosition: Int): BottomSheetDialogFragment() {

   private var binding:DetaileDialogBinding? = null

    private lateinit var model: TaskViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DetaileDialogBinding.inflate(inflater,container,false)


        model = ViewModelProviders.of(requireActivity()).get(TaskViewModel::class.java)

        //положили в переменную конкретный пункт списка с которым будем работать
        val task = model.db.value!![itemPosition]  ////value!!


        binding?.etNametaskDetails?.setText("${task.name}")

        //Кнопка выключения диалога
        binding?.btnCancel?.setOnClickListener {
            dialog?.cancel()
        }

        binding?.btnDelete?.setOnClickListener {

            model.delete_task(task = task)
            dialog?.cancel()
        }

        binding?.btnEdit?.setOnClickListener {

            //в таск кладём id этого task и имя задачи из edittext
            model.update_task(task = TaskModel(task.id, binding?.etNametaskDetails?.text.toString(),task.status))

            dialog?.cancel()
        }


        return binding?.root
    }
}