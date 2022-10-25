package com.example.springboot_project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.example.springboot_project.databinding.ActivityMain2Binding
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity2 : AppCompatActivity() {
    lateinit var binding: ActivityMain2Binding
    lateinit var apiService: ApiService
    var isInserting = true
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityMain2Binding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarMain2)

        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding.edtFirstName.requestFocus()



        val retrofit = Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(ApiService::class.java)

        val testMode = intent.getParcelableExtra<Student>("student")
        isInserting = (testMode == null)

        if (!isInserting) {

            binding.btnDone.text = "update"

            val dataFromIntent = intent.getParcelableExtra<Student>("student")!!
            binding.edtScore.setText(dataFromIntent.score.toString())
            binding.edtCourse.setText(dataFromIntent.course)

            val splitedName = dataFromIntent.name.split(" ")
            binding.edtFirstName.setText(splitedName[0])
            binding.edtLastName.setText(splitedName[(splitedName.size - 1)])

        }


        binding.btnDone.setOnClickListener {

            if (isInserting) {
                addNewStudent()
            } else {
                updateStudent()
            }

        }

    }



private fun updateStudent() {

    val firstName = binding.edtFirstName.text.toString()
    val lastName = binding.edtLastName.text.toString()
    val score = binding.edtScore.text.toString()
    val course = binding.edtCourse.text.toString()

    if (
        firstName.isNotEmpty() &&
        lastName.isNotEmpty() &&
        course.isNotEmpty() &&
        score.isNotEmpty()
    ) {

        val jsonObject = JsonObject()
        jsonObject.addProperty("name", firstName + " " + lastName)
        jsonObject.addProperty("course", course)
        jsonObject.addProperty("score", score.toInt())


        apiService
            .updateStudent(firstName + " " + lastName, jsonObject)
            .enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {

                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                }
            })

        Toast.makeText(this, "update finished!", Toast.LENGTH_SHORT).show()
        onBackPressed()


    } else {
        Toast.makeText(this, "لطفا اطلاعات را کامل وارد کنید", Toast.LENGTH_SHORT).show()
    }

}

private fun addNewStudent() {

    val firstName = binding.edtFirstName.text.toString()
    val lastName = binding.edtLastName.text.toString()
    val score = binding.edtScore.text.toString()
    val course = binding.edtCourse.text.toString()

    if (
        firstName.isNotEmpty() &&
        lastName.isNotEmpty() &&
        course.isNotEmpty() &&
        score.isNotEmpty()
    ) {

        val jsonObject = JsonObject()
        jsonObject.addProperty("name", firstName + " " + lastName)
        jsonObject.addProperty("course", course)
        jsonObject.addProperty("score", score.toInt())

        apiService.insertStudent(jsonObject).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {

            }

            override fun onFailure(call: Call<String>, t: Throwable) {}
        })

        Toast.makeText(this, "insert finished!", Toast.LENGTH_SHORT).show()
        onBackPressed()

    } else {
        Toast.makeText(this, "لطفا اطلاعات را کامل وارد کنید", Toast.LENGTH_SHORT).show()
    }

}

override fun onOptionsItemSelected(item: MenuItem): Boolean {

    if (item.itemId == android.R.id.home) {
        onBackPressed()
    }

    return true
}

}