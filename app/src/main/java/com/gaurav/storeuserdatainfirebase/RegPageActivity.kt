package com.gaurav.storeuserdatainfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.gaurav.storeuserdatainfirebase.databinding.ActivityMainBinding
import com.gaurav.storeuserdatainfirebase.databinding.ActivityRegPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.combine

class RegPageActivity : AppCompatActivity() {
    lateinit var binding:ActivityRegPageBinding
    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore
    var storageRef= FirebaseStorage.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityRegPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth= Firebase.auth
        binding.btnRDone.setOnClickListener {
            if (binding.etREmail.text.isEmpty()){
                binding.etREmail.error="Enter your Email"
            }else if(binding.etRPassword.text.isEmpty()){
                binding.etRPassword.error="Enter Your Password"
            }else if (binding.etRCPassword.text.isEmpty()){
                    binding.etRCPassword.error="Confrim Your Password"

            }else if (binding.etRCPassword.text.toString().equals(binding.etRPassword.text.toString())==false){
                      binding.etRCPassword.error="Please Match your Password"

            }else{
                auth.createUserWithEmailAndPassword(binding.etREmail.text.toString(),binding.etRPassword.text.toString())
                    .addOnSuccessListener { Toast.makeText(this,"Registered successfully",Toast.LENGTH_SHORT).show()
                var intent=Intent(this,MainActivity::class.java)
                startActivity(intent)
                finish()
                    }.addOnFailureListener{
                        Toast.makeText( this,"Not Registered ", Toast.LENGTH_SHORT).show()
                    }

                   var userModle=UserModle()
                userModle.Email=binding.etREmail.text.toString()
                userModle.password=binding.etRPassword.text.toString()
                db.collection("users").document(auth.currentUser?.uid?:"")
                    .set(userModle)
                    .addOnSuccessListener{documentReference ->
                        Toast.makeText(this, "done", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener{
                        Toast.makeText(this, "not done", Toast.LENGTH_SHORT).show()
                    }

            }
        }

    }
}


