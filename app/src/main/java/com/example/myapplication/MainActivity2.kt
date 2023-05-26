package com.example.myapplication

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import android.widget.VideoView

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        val videoView = findViewById<VideoView>(R.id.videoView)

        val videoUri = Uri.parse("android.resource://" + packageName + "/" + R.raw.trivia)
        videoView.setVideoURI(videoUri)
        videoView.setOnPreparedListener { mediaPlayer ->
            mediaPlayer.isLooping = true // Set to true for video looping
            mediaPlayer.start()
        }

        videoView.start()
        var ithcategory=0
        var ithtype=0
        var ithdiff=0
        val category=findViewById<Spinner>(R.id.spinner)
        category.onItemSelectedListener=object :AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(
                adapterView: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
ithcategory=position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        val type=findViewById<Spinner>(R.id.spinner2)
        type.onItemSelectedListener=object :AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(
                adapterView: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
ithtype=position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        val difficulty=findViewById<Spinner>(R.id.spinner3)
        difficulty.onItemSelectedListener=object :AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(
                adapterView: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
ithdiff=position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }





        }


        val numberOfQ=findViewById<EditText>(R.id.editTextText)

       val clickme=findViewById<Button>(R.id.click)
        clickme.isEnabled = false

        numberOfQ.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clickme.isEnabled = s?.isNotEmpty() == true
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        clickme.setOnClickListener{
val nq=numberOfQ.text.toString().toIntOrNull()

            Intent( this,MainActivity::class.java).also()
{
    it.putExtra("Extra_Category",ithcategory.toString())
    it.putExtra("Extra_Type",ithtype.toString())
    it.putExtra("Extra_diff",ithdiff.toString())
    it.putExtra("Extra_ques",nq.toString())
    startActivity(it)

}
}


    }
}