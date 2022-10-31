package com.example.projectc4g5

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.projectc4g5.room_database.ToDo
import com.example.projectc4g5.room_database.ToDoDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

// Prueba de commits en Development
//Prueba de pullrequest
/*Cambios al codifo==go
para verificadr
que al hacer push en development
al final
pueda devolverme a trabajar en el main para
presentar el lunes
 */

class NuevaTareaActivity : AppCompatActivity() {


    lateinit var text_task_title:TextView
    lateinit var text_task_time:TextView
    lateinit var text_task_place:TextView
    lateinit var button_save_task:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_nueva_tarea)
        text_task_title=findViewById(R.id.text_task_title)
        text_task_time=findViewById(R.id.text_task_time)
        text_task_place=findViewById(R.id.text_task_place)
        button_save_task=findViewById(R.id.button_save_task)
        var idTarea= intent.getIntExtra("id",0)
        Toast.makeText(this,"id traidp"+idTarea,Toast.LENGTH_SHORT).show()
        var task= intent.getStringExtra("task").toString()
        var time: String  = intent.getStringExtra("time").toString()
        text_task_place.text = intent.getStringExtra("place").toString()
        text_task_title.text=task
        text_task_time.text=time
        Toast.makeText(this,"Datos: "+task,Toast.LENGTH_SHORT).show()

        if(intent.getStringExtra("task").isNullOrEmpty()){
            Toast.makeText(this,"Problemas"+task,Toast.LENGTH_SHORT).show()

            text_task_title.text=""
            text_task_time.text=""
            text_task_place.text=""
        }
        else{
            Toast.makeText(this,"Peor"+task,Toast.LENGTH_SHORT).show()
        }


        button_save_task.setOnClickListener{
            /*
            Crear o modificar tareas con room
            val db= ToDoDatabase.getDatabase(this)

            var updateOrCreate: Number

            val todoDAO=db.todoDao()

            if (intent.getStringExtra("time").isNullOrEmpty()){
                updateOrCreate=0

            }else{
                updateOrCreate=1

            }

            runBlocking {
                launch {
                    if(updateOrCreate==0)
                    {
                        val task=ToDo(0,text_task_title.text.toString(),text_task_time.text.toString(),text_task_place.text.toString())
                        var result =todoDAO.insertTask(task)
                        if(result!=-1L){
                            setResult(Activity.RESULT_OK)
                            finish()
                        }
                        Toast.makeText(this@NuevaTareaActivity,"Deberia crear",Toast.LENGTH_LONG).show()
                    }
                    else{
                        var idTarea= intent.getIntExtra("id",0)
                        Toast.makeText(this@NuevaTareaActivity,"id traidp"+idTarea,Toast.LENGTH_SHORT).show()
                        var nuwTask= text_task_title.text.toString()
                        val task=ToDo(idTarea,text_task_title.text.toString(),text_task_time.text.toString(),text_task_place.text.toString())
                        var result =todoDAO.updateTask(task)
                        Toast.makeText(this@NuevaTareaActivity,"Deberia actualizar "+nuwTask,Toast.LENGTH_LONG).show()
                        setResult(Activity.RESULT_OK)
                        var intento=Intent(this@NuevaTareaActivity,HomeActivity::class.java)
                        startActivity(intento)

                    }


                }
            }*/

            // Crear o modificar tareas con Firebase

            val db= ToDoDatabase.getDatabase(this)
            val dbFirebase = FirebaseFirestore.getInstance()
            var updateOrCreate: Number
            val todoDAO=db.todoDao()

            if (intent.getStringExtra("time").isNullOrEmpty()){
                updateOrCreate=0

            }else{
                updateOrCreate=1

            }

            runBlocking {
                launch {
                    if (updateOrCreate == 0) {
                        val task = ToDo(
                            0,
                            text_task_title.text.toString(),
                            text_task_time.text.toString(),
                            text_task_place.text.toString()
                        )
                        var result = todoDAO.insertTask(task)
                        if (result != -1L) {
                            dbFirebase.collection("ToDo").document(result.toString()).set(
                                hashMapOf(
                                    "title" to text_task_title.text.toString(),
                                    "time" to text_task_title.text.toString(),
                                    "place" to text_task_title.text.toString()
                                )
                            )
                        }
                        setResult(Activity.RESULT_OK)
                        finish()
                        Toast.makeText(this@NuevaTareaActivity, "Deberia crear", Toast.LENGTH_LONG)
                            .show()
                    } else {
                        var idTarea = intent.getIntExtra("id", 0)
                        Toast.makeText(
                            this@NuevaTareaActivity,
                            "id traidp" + idTarea,
                            Toast.LENGTH_SHORT
                        ).show()
                        var nuwTask = text_task_title.text.toString()
                        val task = ToDo(
                            idTarea,
                            text_task_title.text.toString(),
                            text_task_time.text.toString(),
                            text_task_place.text.toString()
                        )
                        var result = todoDAO.updateTask(task)
                        Toast.makeText(
                            this@NuevaTareaActivity,
                            "Deberia actualizar " + nuwTask,
                            Toast.LENGTH_LONG
                        ).show()
                        setResult(Activity.RESULT_OK)
                        var intento = Intent(this@NuevaTareaActivity, HomeActivity::class.java)
                        startActivity(intento)

                    }


                }

            }

        }
    }

}