package com.example.projectc4g5

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.projectc4g5.room_database.*
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

class ServicesAdapter (context: AppCompatActivity,
                       val info: Bundle) : RecyclerView.Adapter<ServicesAdapter.MyViewHolder>() {

    class MyViewHolder(val layout: View):RecyclerView.ViewHolder(layout)

    private var context:AppCompatActivity=context

    lateinit var image: ImageView

    var myServicesId: ArrayList<Int> = info.getIntegerArrayList("ids") as ArrayList<Int>
    var myServicesTitles: ArrayList<String> = info.getStringArrayList("titles") as ArrayList<String>
    var myServicesEncargados: ArrayList<String> = info.getStringArrayList("encargados") as ArrayList<String>
    var myServicesPrecios: ArrayList<String> = info.getStringArrayList("precios") as ArrayList<String>
    var myServicesImagenes: ArrayList<String> = info.getStringArrayList("imagenes") as ArrayList<String>

    private lateinit var myAdapter:RecyclerView.Adapter<ServicesAdapter.MyViewHolder>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.design_service, parent,false)



        /*
        Para generar color aleatorio al background del vector de la tarea
        val rnd = Random
        val color: Int = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))*//*
        icon_view_card.setBackgroundColor(color)*/
        return  MyViewHolder(layout)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        var icon_edit_service=holder.layout.findViewById<ImageView>(R.id.icon_edit_service)
        var icon_delete_service=holder.layout.findViewById<ImageView>(R.id.icon_delete_service)



        var imag_recycler_Service=holder.layout.findViewById<ImageView>(R.id.icon_card_view_serv)
        var urlImageRecycler:String=myServicesImagenes[position]

        /*if(urlImageRecycler!!.contains("firebase")){
            Picasso.get().load(urlImageRecycler).fit()
                .into(imag_recycler_Service)
        }*/
        Picasso.get().load(urlImageRecycler).fit()
            .into(imag_recycler_Service)



        var textViewService=holder.layout.findViewById<TextView>(R.id.textViewService)
        textViewService.text=myServicesTitles[position]

        var textViewEncargado=holder.layout.findViewById<TextView>(R.id.textViewEncargado)
        textViewEncargado.text=myServicesEncargados[position]

        icon_edit_service.setOnClickListener{

            val intento = Intent(holder.itemView.context, NewServiceActivity::class.java)
            val datos=Bundle()
            datos.putInt("id",myServicesId[position])
            datos.putString("nombre",myServicesTitles[position])
            datos.putString("encargado",myServicesEncargados[position])
            datos.putString("precio",myServicesPrecios[position])
            datos.putString("imagen",myServicesImagenes[position])

            intento.putExtras(datos)

            Toast.makeText(context,"imagencita"+myServicesImagenes[position],Toast.LENGTH_LONG).show()
            context.startActivity(intento)


        }



        holder.layout.setOnClickListener{
            Toast.makeText(holder.itemView.context,textViewService.text, Toast.LENGTH_LONG).show()
            Toast.makeText(holder.itemView.context,myServicesPrecios[position]+myServicesImagenes[position], Toast.LENGTH_LONG).show()
            val datos=Bundle()
            datos.putInt("id",myServicesId[position])
            datos.putString("nombre",myServicesTitles[position])
            datos.putString("encargado",myServicesEncargados[position])
            datos.putString("precio",myServicesPrecios[position])
            datos.putString("imagen",myServicesImagenes[position])

            context.getSupportFragmentManager()?.beginTransaction()
                ?.setReorderingAllowed(true)
                ?.replace(R.id.fragment_container_view_home, ServiceFragment::class.java, datos,"detail_services")
                ?.addToBackStack("")
                ?.commit()
        }

        icon_delete_service.setOnClickListener{
            val db= ServiceDatabase.getDatabase(HomeActivity())
            val serviceDAO=db.serviceDao()
            val dbFirebase=Firebase.firestore

            runBlocking{
                launch {
                    val datos=Bundle()
                    //datos.putInt("id",myTaskIds[position])
                    var idpro= myServicesId[position]
                    val idicon=datos.getInt("id")
                    var servicepro=myServicesTitles[position]
                    var encargadopro=myServicesEncargados[position]
                    var preciopro=myServicesPrecios[position]
                    var imagepro=myServicesImagenes[position]
                    datos.putString("encargado",textViewEncargado.text as String)
                    datos.putString("precio",myServicesPrecios[position])
                    val service= Service(idpro,servicepro,encargadopro,preciopro,imagepro)
                    var result =serviceDAO.deleteService(service)
                    //val intento = Intent(holder.itemView.context, HomeActivity::class.java)

                    Toast.makeText(context,"idpro"+idpro.toString(), Toast.LENGTH_LONG).show()
                    dbFirebase.collection("Services").document(idpro.toString())
                        .delete()
                        .addOnSuccessListener{Toast.makeText(context,"Borrado bd", Toast.LENGTH_LONG).show()}
                        .addOnFailureListener(){e->Toast.makeText(context,"error bd"+e, Toast.LENGTH_LONG).show()}

                    //context.startActivity(intento)

                    context.getSupportFragmentManager()?.beginTransaction()
                        ?.setReorderingAllowed(true)
                        ?.replace(R.id.fragment_container_view_home, ServicesFragment::class.java, null,"detail")
                        ?.addToBackStack("")
                        ?.commit()
                }
            }
        }
    }


    override fun getItemCount(): Int {
        return myServicesTitles.size
    }

}