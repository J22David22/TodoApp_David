package com.example.projectc4g5

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Pair
import android.util.Patterns
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.projectc4g5.room_database.ToDo
import com.example.projectc4g5.room_database.ToDoDatabase
import com.example.projectc4g5.room_database.User
import com.example.projectc4g5.room_database.UserDatabase
import com.example.projectc4g5.room_database.repository.ToDoRepository
import com.example.projectc4g5.room_database.repository.UserRepository
import com.example.projectc4g5.room_database.viewmodel.ToDoViewModel
import com.example.projectc4g5.room_database.viewmodel.UserViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    lateinit var labelRegistrar: TextView
    lateinit var labelBienvenida: TextView
    lateinit var buttonLogin: Button
    lateinit var imageLogin: ImageView

    private var myUsersIds: ArrayList<Int> = ArrayList()
    private var myUsersUsernames: ArrayList<String> = ArrayList()
    private var myUsersEmails: ArrayList<String> = ArrayList()
    private var myUsersPasswords: ArrayList<String> = ArrayList()

    lateinit var txtInputUsuario:  TextInputEditText
    lateinit var txtInputPassword:  TextInputEditText

    lateinit var image_google: ImageView
    lateinit var image_facebook: ImageView


    private lateinit var userViewModel: UserViewModel
    private lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        labelRegistrar = findViewById(R.id.labelRegistrar)
        labelBienvenida= findViewById(R.id.labelBienvenida)
        imageLogin = findViewById(R.id.imageLogin)
        buttonLogin = findViewById(R.id.buttonLogin)

        txtInputUsuario = findViewById(R.id.txtInputUsuario)
        txtInputPassword = findViewById(R.id.txtInputPassword)

        image_google=findViewById(R.id.image_google)
        image_facebook=findViewById(R.id.image_facebook)

        val db= UserDatabase.getDatabase(this)
        val userDAO=db.userDao()

        // Nuevas cosas para que funcione con firebase

        //val dbFirebase=FirebaseFirestore.getInstance()
        val dbFirebase= Firebase.firestore

        userRepository= UserRepository(userDAO)
        userViewModel= UserViewModel(userRepository)
        var result = userViewModel.getAllUsers()
        result.invokeOnCompletion {
            var theUsers = userViewModel.getTheUsers()
            var users = mutableListOf<User>()

            if(theUsers!!.size!=0){
                var i=0
                myUsersIds.clear()
                myUsersUsernames.clear()
                myUsersEmails.clear()
                myUsersPasswords.clear()
                while(i<theUsers!!.size) {
                    myUsersIds.add(theUsers[i].id)
                    myUsersUsernames.add(theUsers[i].username!!)
                    myUsersEmails.add(theUsers[i].email!!)
                    myUsersPasswords.add(theUsers[i].password.toString())
                    i++
                }
            }
            else{

                dbFirebase.collection("User").get().addOnSuccessListener {
                    var docs=it.documents
                    if(docs.size !=0){
                        var i=0
                        while(i<docs.size) {
                            myUsersIds.add(docs[i].id.toInt())
                            myUsersUsernames.add(docs[i].get("username") as String)
                            myUsersEmails.add(docs[i].get("email") as String)
                            myUsersPasswords.add(docs[i].get("password") as String)
                            users.add(User(myUsersIds[i],
                                myUsersUsernames[i],
                                null,
                                null,
                                null,
                                myUsersEmails[i],
                                null,
                                myUsersPasswords[i]
                            ))
                            i++
                        }

                        userViewModel.insertUsers(users)
                    }
                }

            }
            Toast.makeText(this,"User: "+ myUsersEmails.getOrNull(6),Toast.LENGTH_LONG).show()
            //Toast.makeText(this,"User: s"+theUsers[0].email.toString(),Toast.LENGTH_LONG).show()

        }

        labelRegistrar.setOnClickListener{
            //Toast.makeText(this,"abrir el registro", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, RegisterActivity::class.java)

            val options = ActivityOptions.makeSceneTransitionAnimation(this,
                Pair.create(imageLogin, "logoImageTrans"),
                Pair.create(labelBienvenida, "labelBienvenidaTrans"),
                Pair.create(labelRegistrar, "labelClickable"),
                Pair.create(buttonLogin, "buttonRegisTrans"))

            startActivity(intent, options.toBundle())
        }

        image_google.setOnClickListener{
            val intent=Intent(this,HomeActivity::class.java)
            startActivity(intent)
            Toast.makeText(this,resources.getString(R.string.text_login_google), Toast.LENGTH_LONG).show()
        }

        image_facebook.setOnClickListener{
            val intent=Intent(this,HomeActivity::class.java)
            startActivity(intent)
            Toast.makeText(this,resources.getString(R.string.text_login_facebook), Toast.LENGTH_LONG).show()
        }

        buttonLogin.setOnClickListener{

            validateLogin()

            //val intent=Intent(this,HomeActivity::class.java)
            //startActivity(intent)


        }
    }

    public fun validateLogin(){

        var email:String = txtInputUsuario.text.toString().trim()
        var password:String = txtInputPassword.text.toString().trim()

        if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            txtInputUsuario.setError(resources.getString(R.string.text_invalid_email))
            return
        }
        else
        {
            txtInputUsuario.setError(null)

            if(password.isEmpty())
            {
                txtInputPassword.setError(resources.getString(R.string.text_empty_field))
                return
            }else{


                if (email in myUsersEmails){
                    var emailPosition=myUsersEmails.indexOf(email)
                    var passwordfirebase=myUsersPasswords[emailPosition]
                    Toast.makeText(this,"position: "+emailPosition, Toast.LENGTH_LONG).show()
                    Toast.makeText(this,"position: "+passwordfirebase, Toast.LENGTH_LONG).show()

                    if (password==passwordfirebase){
                        val intent=Intent(this,HomeActivity::class.java)
                        startActivity(intent)
                        Toast.makeText(this,resources.getString(R.string.text_perfect), Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(this,resources.getString(R.string.text_wrong_password),Toast.LENGTH_SHORT).show()
                    }

                }else{
                    Toast.makeText(this,resources.getString(R.string.text_wrong_user),Toast.LENGTH_SHORT).show()
                }

            }
        }


    }

}