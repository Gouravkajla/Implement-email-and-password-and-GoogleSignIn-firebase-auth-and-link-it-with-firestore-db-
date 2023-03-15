package com.gaurav.storeuserdatainfirebase

import android.content.Intent
import android.media.session.MediaSession.Token
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.gaurav.storeuserdatainfirebase.databinding.ActivityMainBinding
import com.gaurav.storeuserdatainfirebase.databinding.ActivityRegPageBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    lateinit var gso:GoogleSignInOptions
     lateinit var googleSignClient: GoogleSignInClient
   var googleSigninResult= registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        var task =GoogleSignIn.getSignedInAccountFromIntent(it.data)
       try {
           val account=task.getResult(ApiException::class.java)!!
           firebaseAuthWithGoogle(account.idToken!!)
       }catch (e:ApiException){


       }
   }

     fun firebaseAuthWithGoogle(idToken: String) {
         val token = null
         val credential=GoogleAuthProvider.getCredential(token,null)
        auth.signInWithCredential(credential).addOnSuccessListener {
            Toast.makeText(this, "Successfully Registered", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this,"Not Registered",Toast.LENGTH_SHORT).show()
        }

    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvDontHaveAccount.setOnClickListener {
          var intent= Intent(this,RegPageActivity::class.java)
            startActivity(intent)

            }
       /* if(auth.currentUser != null){
            startActivity(Intent(this,RegPageActivity::class.java))
            finish()
        }*/
        binding.btnLogin.setOnClickListener {
            if (binding.etEmail.text.isEmpty()){
                binding.etEmail.error="Enter You Email"
            }else if(binding.etPassword.text.isEmpty()){
                binding.etPassword.error="Enter your password"
            }else{
                auth.signInWithEmailAndPassword(binding.etEmail.text.toString(),
                    binding.etPassword.text.toString()).addOnSuccessListener {
                    Toast.makeText(this, "Login successfully", Toast.LENGTH_LONG).show()
                }.addOnFailureListener {
                    Toast.makeText(this, "Cannot Login ${it.toString()}", Toast.LENGTH_LONG).show()
                }
            }
            gso=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("565301915967-qj4g8622lsv0am0spmpfvscl6og5nort.apps.googleusercontent.com")
                .requestEmail()
                .build()
            googleSignClient= GoogleSignIn.getClient(this,gso)
            binding.tvLoginWithGoogle.setOnClickListener {
                val signInIntent:Intent=googleSignClient.signInIntent
                googleSigninResult.launch(signInIntent)
            }


        }
        }
    }


