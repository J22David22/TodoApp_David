package com.example.projectc4g5

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


class TareaFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val fragmento=inflater.inflate(R.layout.fragment_tarea, container, false)

        var task=requireArguments().getString("task")
        var time=requireArguments().getString("time")
        var place=requireArguments().getString("place")

        var textViewTarea: TextView = fragmento.findViewById(R.id.label_tarea_title)
        var textViewHora: TextView = fragmento.findViewById(R.id.label_tarea_time)
        var textViewLugar: TextView = fragmento.findViewById(R.id.label_tarea_place)

        textViewTarea.text=task
        textViewHora.text=time
        textViewLugar.text=place

        return fragmento
    }


}