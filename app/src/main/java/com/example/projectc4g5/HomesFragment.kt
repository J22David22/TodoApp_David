package com.example.projectc4g5

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.projectc4g5.room_database.ToDo
import com.example.projectc4g5.room_database.ToDoDatabase
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class HomesFragment : Fragment() {
    private var myTaskIds: ArrayList<Int> = ArrayList()
    private var myTaskTitles: ArrayList<String> = ArrayList()
    private var myTaskTimes: ArrayList<String> = ArrayList()
    private var myTaskPlaces: ArrayList<String> = ArrayList()
    private lateinit var listRecyclerView:RecyclerView
    private lateinit var myAdapter:RecyclerView.Adapter<TareasAdapter.MyViewHolder>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_homes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val fab: View = requireActivity().findViewById(R.id.fab_tarea)

        fab.setOnClickListener{
            val intent = Intent(activity, NuevaTareaActivity::class.java)
            var requestCode= 0;
            startActivityForResult(intent, requestCode)
        }
       /* var myTaskTitles: ArrayList<String> = ArrayList()
        var myTaskTimes: ArrayList<String> = ArrayList()
        var myTaskPlaces: ArrayList<String> = ArrayList()*/

        /*var myTaskTitles: ArrayList<String> = ArrayList()

        //myTaskTitles.add("Ir de shopping")
        myTaskTitles.add(resources.getString(R.string.text_task1))
        myTaskTitles.add(resources.getString(R.string.text_task2))
        myTaskTitles.add(resources.getString(R.string.text_task3))

        var myTaskTimes: ArrayList<String> = ArrayList()

        myTaskTimes.add(resources.getString(R.string.text_task1_time))
        myTaskTimes.add(resources.getString(R.string.text_task2_time))
        myTaskTimes.add(resources.getString(R.string.text_task3_time))

        var myTaskPlaces: ArrayList<String> = ArrayList()

        myTaskPlaces.add(resources.getString(R.string.text_task1_place))
        myTaskPlaces.add(resources.getString(R.string.text_task2_place))
        myTaskPlaces.add(resources.getString(R.string.text_task3_place))

        var info: Bundle= Bundle()*/

        var info: Bundle= Bundle()

        info.putIntegerArrayList("ids", myTaskIds)
        info.putStringArrayList("titles", myTaskTitles)
        info.putStringArrayList("times", myTaskTimes)
        info.putStringArrayList("places", myTaskPlaces)

        listRecyclerView = requireView().findViewById(R.id.recycler_Tareas)
        myAdapter = TareasAdapter(activity as AppCompatActivity, info)

        listRecyclerView.setHasFixedSize(true)
        listRecyclerView.adapter=myAdapter
        listRecyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))


        updateList()


    }

    fun updateList(){
        val db=ToDoDatabase.getDatabase(requireActivity())
        val todoDAO=db.todoDao()

        runBlocking{
            launch {
                var result=todoDAO.getAllTasks()
                var i=0
                myTaskTitles.clear()
                myTaskTimes.clear()
                myTaskPlaces.clear()
                myTaskIds.clear()
                while(i<result.size) {
                    myTaskIds.add(result[i].id)
                    myTaskTitles.add(result[i].title.toString())
                    myTaskTimes.add(result[i].time.toString())
                    myTaskPlaces.add(result[i].place.toString())
                    i++
                }
                myAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(requestCode==0){
            if(resultCode==Activity.RESULT_OK){
                updateList()
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }


}