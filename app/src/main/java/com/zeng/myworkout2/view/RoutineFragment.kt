package com.zeng.myworkout2.view

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.zeng.myworkout2.R
import com.zeng.myworkout2.databinding.DialogRoutineFormBinding
import com.zeng.myworkout2.databinding.FragmentRoutineBinding
import com.zeng.myworkout2.model.Routine
import com.zeng.myworkout2.util.InjectorUtils
import com.zeng.myworkout2.view.adapter.RoutineAdapter
import com.zeng.myworkout2.viewmodel.RoutineViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch

class RoutineFragment : Fragment() {

    private lateinit var binding: FragmentRoutineBinding

    private val viewModel: RoutineViewModel by viewModels {
        InjectorUtils.provideRoutineViewModelFactory(requireContext())
    }

    private val handleRoutineDetail = fun (routine: Routine, isNew: Boolean) {
        val intent = Intent(activity, RoutineDetailActivity::class.java)
        intent.putExtra(resources.getString(R.string.intent_routine), routine.id)
        intent.putExtra(resources.getString(R.string.intent_new_routine), isNew)
        activity?.startActivity(intent)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRoutineBinding.inflate(inflater, container, false)

        val adapter = RoutineAdapter(viewModel, handleRoutineDetail)
//        adapter.disableSwipe()
        binding.list.adapter = adapter
//        val helper = ItemTouchHelper(adapter.callback)
//        helper.attachToRecyclerView(binding.list)

        subscribeUi(adapter)
        binding.fab.setOnClickListener{
            showNewRoutineDialog(inflater)
        }

        return binding.root
    }

    private fun subscribeUi(adapter: RoutineAdapter) {
        viewModel.routines.observe(viewLifecycleOwner, Observer { routines ->
            adapter.submitList(routines)
        })
    }

    private fun showNewRoutineDialog(inflater: LayoutInflater) {
        val formDialog = DialogRoutineFormBinding.inflate(inflater, container, false)
        val dialog = AlertDialog.Builder(context)
            .setMessage("New routine")
            .setPositiveButton("CREATE") { _, _ ->
                val routine = Routine(
                    emptyList(),
                    formDialog.name.text.toString(),
                    formDialog.description.text.toString()
                )
                viewModel.viewModelScope.launch {
                    viewModel.addRoutine(routine)
                    handleRoutineDetail(routine, true)
                }
            }
            .setNegativeButton("CANCEL") {  _, _ ->  }
            .setView(formDialog.root)
            .create()

        dialog.show()
    }

}