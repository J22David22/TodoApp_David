package com.example.projectc4g5

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.room.Database
import com.example.projectc4g5.room_database.Service
import com.example.projectc4g5.room_database.ServiceDatabase
import com.example.projectc4g5.room_database.ToDo
import com.example.projectc4g5.room_database.ToDoDatabase
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.net.URI
import java.util.Objects

class NewServiceActivity : AppCompatActivity() {

    lateinit var text_service_title: TextView
    lateinit var text_service_encargado: TextView
    lateinit var text_service_precio: TextView
    lateinit var label_upload_image: TextView
    lateinit var image_service: ImageView
    lateinit var button_save_service: Button
    lateinit var imageUri: Uri
    lateinit var dataBase: FirebaseDatabase
    lateinit var dataRef: DatabaseReference
    lateinit var dataStorage: FirebaseStorage

    lateinit var download_uri:String

    lateinit var idd: String

    lateinit var progressDialog: ProgressDialog

    private var storageReference = Firebase.storage

    private var Gallery_code: Int = 1
    val storage_path: String ="ServiceImage/*"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //binding =ActivityNewServiceBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_new_service)

        storageReference=FirebaseStorage.getInstance()

        dataBase= FirebaseDatabase.getInstance()
        dataRef=dataBase.getReference().child("ServiceImages")
        dataStorage=FirebaseStorage.getInstance()

        label_upload_image=findViewById(R.id.label_upload_image)
        image_service=findViewById(R.id.image_service)





        val galleryImage=registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback{
            image_service.setImageURI(it)
                if (it != null) {
                    imageUri = it
                }
        }
        )


        label_upload_image.setOnClickListener{

            val intent = Intent()
            intent.action=Intent.ACTION_GET_CONTENT
            intent.type="image/*"
            startActivityForResult(intent, Gallery_code)


            //galleryImage.launch("image/*")

        }


        text_service_title=findViewById(R.id.text_service_title)
        text_service_encargado=findViewById(R.id.text_service_encargado)
        text_service_precio=findViewById(R.id.text_service_precio)
        image_service=findViewById(R.id.image_service)
        button_save_service=findViewById(R.id.button_save_service)


        var idService= intent.getIntExtra("id",0)
        Toast.makeText(this,"id traidp"+idService, Toast.LENGTH_SHORT).show()
        var service= intent.getStringExtra("nombre").toString()
        var encargado: String  = intent.getStringExtra("encargado").toString()
        var urlImagenServicio: String=intent.getStringExtra("imagen").toString()
        text_service_precio.text = intent.getStringExtra("precio").toString()
        text_service_title.text=service
        text_service_encargado.text=encargado

        if(urlImagenServicio!!.contains("firebase")){
            Picasso.get().load(urlImagenServicio).into(image_service)
        }
        Toast.makeText(this,"Datos: "+service, Toast.LENGTH_SHORT).show()


        if(intent.getStringExtra("nombre").isNullOrEmpty()){

            text_service_title.text=""
            text_service_encargado.text=""
            text_service_precio.text=""
        }


        button_save_service.setOnClickListener{

            val db= ServiceDatabase.getDatabase(this)
            //val dbFirebase = FirebaseFirestore.getInstance()
            val dbFirebase = Firebase.firestore
            var updateOrCreate: Number
            val serviceDAO=db.serviceDao()

            if (intent.getStringExtra("precio").isNullOrEmpty()){
                updateOrCreate=0
                Toast.makeText(this@NewServiceActivity, "entro en crear", Toast.LENGTH_LONG).show()

            }else{
                updateOrCreate=1
                Toast.makeText(this@NewServiceActivity, "entro en actualizar", Toast.LENGTH_LONG).show()
            }

            runBlocking {
                launch {
                    if (updateOrCreate == 0) {


                        val service = Service(
                            0,
                            text_service_title.text.toString(),
                            text_service_encargado.text.toString(),
                            text_service_precio.text.toString(),
                            download_uri
                        )
                        var result = serviceDAO.insertService(service)
                        if (result != -1L) {
                            //subirImagen()
                            idd=result.toString()
                            var ruta_imagen: String = storage_path+""+idd
                            var storeRef:StorageReference =storageReference.reference.child(ruta_imagen)
                            storeRef.putFile(imageUri).addOnSuccessListener {task ->
                                task.storage.downloadUrl.addOnSuccessListener {
                                    download_uri = it.toString()



                                    /*val imageMap = hashMapOf<String, String>()
                                    imageMap.put("photo",download_uri)*/
                                    dbFirebase.collection("Services").document(idd).set(
                                        hashMapOf(
                                            //"ids" to result.toInt(),
                                            "nombre" to text_service_title.text.toString(),
                                            "encargado" to text_service_encargado.text.toString(),
                                            "precio" to text_service_precio.text.toString(),
                                            "imagen" to download_uri
                                        )
                                    )
                                }
                            }
                            /*dbFirebase.collection("Services").document(result.toString()).set(
                                hashMapOf(
                                    //"ids" to result.toInt(),
                                    "nombre" to text_service_title.text.toString(),
                                    "encargado" to text_service_encargado.text.toString(),
                                    "precio" to text_service_precio.text.toString(),
                                    "imagen" to "url imagen 1"
                                )
                            )*/


                            val dbprueba = dbFirebase.collection("Services").document(result.toString()).id
                            val imageMap = mapOf(
                                "url" to imageUri
                            )
                          //  storageReference.getReference("Images").child(System.currentTimeMillis().toString())
                          //      .putFile(imageUri)
                                /*.addOnSuccessListener { task ->
                                    task.metadata!!.reference!!.downloadUrl
                                        .addOnSuccessListener {
                                            val databaseReference=FirebaseDatabase.getInstance().getReference("userImages")
                                            databaseReference.child(dbprueba).setValue(imageMap)
                                                .addOnSuccessListener {
                                                    Toast.makeText(this@NewServiceActivity,"Succesful",Toast.LENGTH_SHORT).show()
                                                }
                                        }
                                }*/


                            // *** CODIGO NUEVO PARA SUBIR IMAGENES Y OBTENER URL

                            var filepath:StorageReference = dataStorage.getReference().child("imagePost").child(imageUri.lastPathSegment.toString()
                            )
                            filepath.putFile(imageUri).addOnSuccessListener {task ->
                                Toast.makeText(this@NewServiceActivity, task.toString(), Toast.LENGTH_LONG)
                                task.storage.downloadUrl.addOnCompleteListener {task ->

                                    var ta: String = task.result.toString()

                                    var newPost: DatabaseReference = dataRef.push()

                                    newPost.child("nombre").setValue(text_service_title.text.toString())
                                    newPost.child("encargado").setValue(text_service_encargado.text.toString())
                                    newPost.child("precio").setValue(text_service_precio.text.toString())
                                    newPost.child("imagen").setValue(task.result.toString())
                                }

                            }



                            setResult(RESULT_OK)
                            finish()
                            Toast.makeText(this@NewServiceActivity, "Deberia crear", Toast.LENGTH_LONG)
                                .show()
                        }

                    } else {
                        var idService = intent.getIntExtra("id", 0)
                        Toast.makeText(
                            this@NewServiceActivity,
                            "id traidp" + idService,
                            Toast.LENGTH_SHORT
                        ).show()
                        var newService = text_service_title.text.toString()
                        val service = Service(
                            idService,
                            text_service_title.text.toString(),
                            text_service_encargado.text.toString(),
                            text_service_precio.text.toString(),
                            download_uri
                        )
                        var result = serviceDAO.updateService(service)
                        dbFirebase.collection("Services").document(idService.toString()).set(
                            hashMapOf(
                                //"ids" to result.toInt(),
                                "nombre" to text_service_title.text.toString(),
                                "encargado" to text_service_encargado.text.toString(),
                                "precio" to text_service_precio.text.toString(),
                                "imagen" to download_uri
                            )
                        )
                        Toast.makeText(
                            this@NewServiceActivity,
                            "Deberia actualizar " + newService,
                            Toast.LENGTH_LONG
                        ).show()
                        setResult(RESULT_OK)
                        /*var intento = Intent(this@NewServiceActivity, HomeActivity::class.java)
                        startActivity(intento)*/
                        getSupportFragmentManager()?.beginTransaction()
                            ?.setReorderingAllowed(true)
                            ?.replace(R.id.fragment_container_view_home, TareaFragment::class.java, null,"detail")
                            ?.addToBackStack("")
                            ?.commit()

                    }


                }

            }

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==Gallery_code && resultCode== RESULT_OK){

            imageUri=data?.data!!


            image_service.setImageURI(imageUri)


            var ruta_imagen: String = storage_path
            var storeRef:StorageReference =storageReference.reference.child(ruta_imagen)
            storeRef.putFile(imageUri).addOnSuccessListener {task ->

                task.storage.downloadUrl.addOnSuccessListener {
                    download_uri = it.toString()
                    Toast.makeText(this,"Success "+download_uri,Toast.LENGTH_LONG).show()
                }}
            //subirImagen()
        }
    }



}