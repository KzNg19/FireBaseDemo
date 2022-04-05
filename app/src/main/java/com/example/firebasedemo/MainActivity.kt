package com.example.firebasedemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.firebasedemo.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("myDB")

        if(auth.currentUser != null){
            binding.tvStatus.text = auth.currentUser!!.email
        }


        binding.btnRegister.setOnClickListener(){
            registerUser("nkz@tarc.edu.my","123456")
        }

        binding.btnSignin.setOnClickListener(){
            signIn("nkz@tarc.edu.my","123456")
        }

        binding.btnSignout.setOnClickListener(){
            signOut()
        }

        binding.btnInsert.setOnClickListener(){
            val newStudent = Student("W123","aasdf","RSF")
            addNewStudent(newStudent)
        }

        binding.btnRead.setOnClickListener(){
            readData("W123")
        }

        binding.btnDelete.setOnClickListener(){
            deleteData("W123")
        }
    }

    private fun deleteData(id: String) {
        database.child("Student").child(id).removeValue()
            .addOnSuccessListener {
                binding.tvStatus.text="deleted"
            }
            .addOnFailureListener{e->
                binding.tvStatus.text = e.message
            }
    }

    private fun readData(id: String) {
        database.child("Student").child(id).get()
            .addOnSuccessListener { rec  ->
                if(rec.child("id").value != null){
                    binding.tvStatus.text = rec.child("name").value.toString()
                }else{
                    binding.tvStatus.text = "Record Not Found"
                }
            }

    }

    private fun addNewStudent(student: Student) {
        database.child("Student")
            .child(student.id).setValue(student)
            .addOnSuccessListener {
                binding.tvStatus.text = "New Student Created"
            }
            .addOnFailureListener{ e->
                binding.tvStatus.text = e.message
            }
    }

    private fun signOut() {
        Firebase.auth.signOut()
        binding.tvStatus.text = "Sign Out Success"
    }

        private fun signIn(email: String, pswd: String) {
            auth.signInWithEmailAndPassword(email,pswd)
            .addOnSuccessListener {
                binding.tvStatus.text = "Login Success"

            }
            .addOnFailureListener{e ->
                binding.tvStatus.text = e.message
            }
    }

    private fun registerUser(email: String, pswd: String) {
        auth.createUserWithEmailAndPassword(email,pswd)
            .addOnSuccessListener {
                binding.tvStatus.text = "New User :${email}"

            }
            .addOnFailureListener { e ->
                binding.tvStatus.text = e.message
            }
    }
}