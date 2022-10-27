package com.example.projectc4g5

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.projectc4g5.room_database.ToDoDatabase
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

class TareasAdapter (context: AppCompatActivity,
                     val info: Bundle) : RecyclerView.Adapter<TareasAdapter.MyViewHolder>() {

    class MyViewHolder(val layout: View):RecyclerView.ViewHolder(layout)

    private var context:AppCompatActivity=context

    var myTaskIds: ArrayList<Int> = info.getIntegerArrayList("ids") as ArrayList<Int>
    var myTaskTitles: ArrayList<String> = info.getStringArrayList("titles") as ArrayList<String>
    var myTaskTimes: ArrayList<String> = info.getStringArrayList("times") as ArrayList<String>
    var myTaskPlaces: ArrayList<String> = info.getStringArrayList("places") as ArrayList<String>
    private lateinit var myAdapter:RecyclerView.Adapter<TareasAdapter.MyViewHolder>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.design_tarea, parent,false)



        /*
        Para generar color aleatorio al background del vector de la tarea
        val rnd = Random
        val color: Int = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))*//*
        icon_view_card.setBackgroundColor(color)*/
        return  MyViewHolder(layout)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        var icon_edit=holder.layout.findViewById<ImageView>(R.id.icon_edit)
        var icon_delete=holder.layout.findViewById<ImageView>(R.id.icon_delete)



        var textViewTask=holder.layout.findViewById<TextView>(R.id.textViewTask)
        textViewTask.text=myTaskTitles[position]

        var textViewTime=holder.layout.findViewById<TextView>(R.id.textViewTime)
        textViewTime.text=myTaskTimes[position]

        icon_edit.setOnClickListener{
            val intento = Intent(holder.itemView.context, NuevaTareaActivity::class.java)
            val datos=Bundle()
            datos.putInt("id",myTaskIds[position])
            datos.putString("task",textViewTask.text as String)
            datos.putString("time",textViewTime.text as String)
            datos.putString("place",myTaskPlaces[position])
            intento.putExtras(datos)
            context.startActivity(intento)
        }



        holder.layout.setOnClickListener{
            Toast.makeText(holder.itemView.context,textViewTask.text, Toast.LENGTH_LONG).show()
            val datos=Bundle()
            datos.putString("task",textViewTask.text as String)
            datos.putString("time",textViewTime.text as String)
            datos.putString("place",myTaskPlaces[position])

            context.getSupportFragmentManager()?.beginTransaction()
                ?.setReorderingAllowed(true)
                ?.replace(R.id.fragment_container_view_home, TareaFragment::class.java, datos,"detail")
                ?.addToBackStack("")
                ?.commit()
        }
    }

    override fun getItemCount(): Int {
        return myTaskTitles.size
    }

}