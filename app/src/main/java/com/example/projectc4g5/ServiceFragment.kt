package com.example.projectc4g5

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso


class ServiceFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val fragmento=inflater.inflate(R.layout.fragment_service, container, false)

        var service=requireArguments().getString("nombre")
        var encargado=requireArguments().getString("encargado")
        var precio=requireArguments().getString("precio")
        var imagen=requireArguments().getString("imagen")

        var textViewService: TextView = fragmento.findViewById(R.id.label_service_title)
        var textViewEncargado: TextView = fragmento.findViewById(R.id.label_service_encargado)
        var textViewPrecio: TextView = fragmento.findViewById(R.id.label_service_precio)
        var imageImagen: ImageView = fragmento.findViewById(R.id.image_detail_service)

        textViewService.text=service
        textViewEncargado.text=encargado
        textViewPrecio.text=precio


        if(imagen!!.contains("firebase")){
            Picasso.get().load(imagen).into(imageImagen)
        }

        return fragmento
    }


}