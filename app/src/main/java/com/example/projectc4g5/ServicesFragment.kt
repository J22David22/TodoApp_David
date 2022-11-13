package com.example.projectc4g5

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.projectc4g5.room_database.Service
import com.example.projectc4g5.room_database.ServiceDatabase
import com.example.projectc4g5.room_database.ToDo
import com.example.projectc4g5.room_database.ToDoDatabase
import com.example.projectc4g5.room_database.repository.ServiceRepository
import com.example.projectc4g5.room_database.repository.ToDoRepository
import com.example.projectc4g5.room_database.viewmodel.ServiceViewModel
import com.example.projectc4g5.room_database.viewmodel.ToDoViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import okhttp3.internal.cache.DiskLruCache.Snapshot
import javax.annotation.Nonnull

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class ServicesFragment : Fragment() {

    lateinit var dataBase: FirebaseDatabase
    lateinit var dataRef: DatabaseReference
    lateinit var dataStorage: FirebaseStorage

    private var myServiceIds: ArrayList<Int> = ArrayList()
    private var myServiceTitles: ArrayList<String> = ArrayList()
    private var myServiceEncargado: ArrayList<String> = ArrayList()
    private var myServicePrecio: ArrayList<String> = ArrayList()
    private var myServiceImagenes: ArrayList<String> = ArrayList()
    private lateinit var listRecyclerView: RecyclerView
    private lateinit var myAdapter: RecyclerView.Adapter<ServicesAdapter.MyViewHolder>

    private lateinit var serviceViewModel: ServiceViewModel
    private lateinit var serviceRepository: ServiceRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_services, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataBase= FirebaseDatabase.getInstance()
        dataRef=dataBase.getReference().child("ServiceImages")
        dataStorage=FirebaseStorage.getInstance()


        val fab: View = requireActivity().findViewById(R.id.fab_service)

        fab.setOnClickListener{
            val intent = Intent(activity, NewServiceActivity::class.java)
            var requestCode= 0;
            startActivityForResult(intent, requestCode)
        }

        updateList2()

        var info: Bundle= Bundle()

        info.putIntegerArrayList("ids", myServiceIds)
        info.putStringArrayList("titles", myServiceTitles)
        info.putStringArrayList("encargados", myServiceEncargado)
        info.putStringArrayList("precios", myServicePrecio)
        info.putStringArrayList("imagenes", myServiceImagenes)

        listRecyclerView = requireView().findViewById(R.id.recycler_services)
        myAdapter = ServicesAdapter(activity as AppCompatActivity, info)

        listRecyclerView.setHasFixedSize(true)
        listRecyclerView.adapter=myAdapter
        listRecyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))


    }

    fun updateList2() {

        val db= ServiceDatabase.getDatabase(requireActivity())
        val serviceDAO=db.serviceDao()

        // Nuevas cosas para que funcione con firebase

        //val dbFirebase=FirebaseFirestore.getInstance()
        val dbFirebase= Firebase.firestore

        serviceRepository= ServiceRepository(serviceDAO)
        serviceViewModel= ServiceViewModel(serviceRepository)

        var result = serviceViewModel.getAllServices()
        result.invokeOnCompletion {
            var theServices = serviceViewModel.getTheServices()
            if(theServices!!.size!=0){
                var i=0
                myServiceIds.clear()
                myServiceTitles.clear()
                myServiceEncargado.clear()
                myServicePrecio.clear()
                myServiceImagenes.clear()

                var services = mutableListOf<Service>()
                dbFirebase.collection("Services").get().addOnSuccessListener {
                    var docs=it.documents
                    if(docs.size !=0){
                        var i=0
                        while(i<docs.size) {
                            myServiceIds.add(docs[i].id.toInt())
                            myServiceTitles.add(docs[i].get("nombre") as String)
                            myServiceEncargado.add(docs[i].get("encargado") as String)
                            myServicePrecio.add(docs[i].get("precio") as String)
                            myServiceImagenes.add(docs[i].get("imagen") as String)
                            services.add(Service(myServiceIds[i], myServiceTitles[i], myServiceEncargado[i], myServicePrecio[i], myServiceImagenes[i]))
                            i++
                        }
                        serviceViewModel.insertServices(services)
                        myAdapter.notifyDataSetChanged()
                    }
                }
            }else{
                var services = mutableListOf<Service>()
                dbFirebase.collection("Services").get().addOnSuccessListener {
                    var docs=it.documents
                    if(docs.size !=0){
                        var i=0
                        while(i<docs.size) {
                            myServiceIds.add(docs[i].id.toInt())
                            myServiceTitles.add(docs[i].get("nombre") as String)
                            myServiceEncargado.add(docs[i].get("encargado") as String)
                            myServicePrecio.add(docs[i].get("precio") as String)
                            myServiceImagenes.add(docs[i].get("imagen") as String)
                            services.add(Service(myServiceIds[i], myServiceTitles[i], myServiceEncargado[i], myServicePrecio[i], myServiceImagenes[i]))
                            i++
                        }
                        serviceViewModel.insertServices(services)
                        myAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(requestCode==0){
            if(resultCode== Activity.RESULT_OK){
                updateList2()
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

}

