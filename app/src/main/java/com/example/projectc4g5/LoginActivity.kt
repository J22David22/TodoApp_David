package com.example.projectc4g5

import android.app.Activity
import android.app.ActivityOptions
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Pair
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.projectc4g5.room_database.Service
import com.example.projectc4g5.room_database.ServiceDatabase
import com.example.projectc4g5.room_database.repository.ServiceRepository
import com.example.projectc4g5.room_database.viewmodel.ServiceViewModel
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
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

    private lateinit var auth: FirebaseAuth


    private lateinit var userViewModel: ServiceViewModel
    private lateinit var userRepository: ServiceRepository

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var signInRequest: BeginSignInRequest

    // Para crear intent con opciones de inicio firebase
    /*private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }*/



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        auth = Firebase.auth

        val googleSignInOptions=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient=GoogleSignIn.getClient(this,googleSignInOptions)

        // Para crear intent con opciones de inicio firebase

        // Choose authentication providers
        /*val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build())*/

// Create and launch sign-in intent
        /*val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
        signInLauncher.launch(signInIntent)*/



        labelRegistrar = findViewById(R.id.labelRegistrar)
        labelBienvenida= findViewById(R.id.labelBienvenida)
        imageLogin = findViewById(R.id.imageLogin)
        buttonLogin = findViewById(R.id.buttonLogin)

        val labelOlvidePsw:TextView = findViewById(R.id.labelOlvidePsw)

        txtInputUsuario = findViewById(R.id.txtInputUsuario)
        txtInputPassword = findViewById(R.id.txtInputPassword)

        image_google=findViewById(R.id.image_google)
        image_facebook=findViewById(R.id.image_facebook)


        // Nuevas cosas para que funcione con firebase

        //val dbFirebase=FirebaseFirestore.getInstance()


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

        labelOlvidePsw.setOnClickListener{
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setTitle("Enviar correo de recuperación")
            val view: View = layoutInflater.inflate(R.layout.reset_pass,null)
            val emailsend:EditText =view.findViewById<EditText>(R.id.text_reset_pass)
            builder.setView(view)

            builder.setPositiveButton("Enviar", DialogInterface.OnClickListener { _, _ ->
                recuperarPassword(emailsend)
            })

            builder.setNegativeButton("Cancel",DialogInterface.OnClickListener{_,_-> Toast.makeText(this,"canceld",Toast.LENGTH_LONG).show()})
            builder.show()
        }

        image_google.setOnClickListener{
            signInGoogle()
            /*val intent=Intent(this,HomeActivity::class.java)
            startActivity(intent)*/
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

    private fun recuperarPassword(emailsend: EditText) {
        Toast.makeText(this,"entra funcion",Toast.LENGTH_SHORT).show()
        if (emailsend.text.toString().isEmpty()){
            Toast.makeText(this,"Sending Cancelled",Toast.LENGTH_SHORT).show()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emailsend.text.toString()).matches()){
            Toast.makeText(this,"Invalid Email",Toast.LENGTH_SHORT).show()
            return
        }

        val emailAddress = emailsend.text.toString()

        auth.sendPasswordResetEmail(emailAddress)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this,"email sent",Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this,"user does not exist",Toast.LENGTH_SHORT).show()
                }
            }
    }



    private fun signInGoogle() {

        val signInIntent=googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private val launcher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result ->
        if(result.resultCode==Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleResults(task)
        }
    }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if(task.isSuccessful){
            val account: GoogleSignInAccount?=task.result
            if(account!=null){
                updateUI2(account)
            }
        }else{
            Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI2(account: GoogleSignInAccount) {
        val credential=GoogleAuthProvider.getCredential(account.idToken,null)
        auth.signInWithCredential(credential).addOnCompleteListener{
            if(it.isSuccessful){
                val intent = Intent(this,HomeActivity::class.java)
                startActivity(intent)
            }else{
                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            // Successfully signed in
            val user = FirebaseAuth.getInstance().currentUser
            // ...
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
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
                signIn(email, password)

                /*Con inicio de sesión manual

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
                }*/

            }
        }


    }


    private fun signIn(email:String, password:String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    //Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                    val intent=Intent(this,HomeActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(this,resources.getString(R.string.text_perfect), Toast.LENGTH_LONG).show()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {

    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            Toast.makeText(this ,"On Start: "+currentUser.email,Toast.LENGTH_LONG).show()
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }

    private fun reload() {
        TODO("Not yet implemented")
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }


}