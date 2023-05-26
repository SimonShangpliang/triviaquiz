package com.example.myapplication
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.core.content.ContextCompat
import androidx.core.os.HandlerCompat.postDelayed
import com.example.myapplication.databinding.ActivityMainBinding
import androidx.core.text.HtmlCompat

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Compiler.disable
import java.lang.Compiler.enable
import java.sql.Time
import java.util.Locale
import java.util.concurrent.TimeUnit

var x:Int=0;
private lateinit var apiInterface: ApiInterface

const val BASE_URL="https://opentdb.com/"

var amount:Int=10
var type:String="multiple"
var category:Int=-1
var difficulty:String="easy"
 var countdownTimer: CountDownTimer? = null

class MainActivity : AppCompatActivity(){

 var index:Int=0
    lateinit var  questionsList:ArrayList<questions1>
    lateinit var questionmodel:questions1
    private lateinit var binding:ActivityMainBinding
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setVideoView()
        getDataFromActivity2()

       // binding.textView2.text= amount.toString()
        binding.rgroup.visibility=View.GONE
    questionsList= ArrayList()
        //start button
        getMyData()
        val delayMillis = 5000
loading()
        binding.start.postDelayed({ binding.start.isEnabled = true }, delayMillis.toLong())

        if(questionsList.size==0)
        { binding.start.isEnabled = false
        }
     binding.start.setOnClickListener{
         binding.question.visibility=View.VISIBLE
         countDown()
binding.time.visibility=View.VISIBLE
         binding.imageView2.visibility=View.VISIBLE
         binding.score.visibility=View.VISIBLE
binding.check.visibility=View.VISIBLE
         binding.button.visibility=View.VISIBLE
binding.start.isEnabled=false
         nextQuestion()
         binding.start.visibility=View.INVISIBLE
     }


     //   questionmodel=questionsList[index]
     //   setAllQuestions()
//firstquestion()
        binding.check.isEnabled=false
       binding.check.setOnClickListener{
            checkAnswer()
            disable()
        }

        binding.newRound.setOnClickListener{
            finish()
        }


binding.button.setOnClickListener{

    binding.check.isEnabled=true

    val checkedQuestion=binding.rgroup.checkedRadioButtonId
    if (checkedQuestion != View.NO_ID) {
        val checked = findViewById<RadioButton>(checkedQuestion)
        val drawable = ContextCompat.getDrawable(this, R.drawable.radiobtnshape) // Replace "your_drawable" with the desired drawable resource name
        checked?.background = drawable
    }
    enable()
    binding.rgroup.clearCheck()
    nextQuestion()
}




    }

    private fun loading() {
        val toast= Toast(applicationContext)
        toast.duration= Toast.LENGTH_LONG
        val toastLayout=layoutInflater.inflate(R.layout.custom_toast,null)
        val toastTextView = toastLayout.findViewById<TextView>(R.id.ans)
        toastTextView.text = "LOADING QUESTIONS"
        toast.view = toastLayout
        toast.show()    }

    /* private fun firstquestion() {
         binding.rgroup.visibility=View.VISIBLE
         binding.textView2.text=index.toString()
         questionmodel=questionsList[index]
         setAllQuestions()
         index++
     }*/

    private fun setVideoView() {
        val videoView = findViewById<VideoView>(R.id.videoView2)
        val videoUri = Uri.parse("android.resource://" + packageName + "/" + R.raw.trivia2)
        videoView.setVideoURI(videoUri)
        videoView.setOnPreparedListener { mediaPlayer ->
            mediaPlayer.isLooping = true // Set to true for video looping
            mediaPlayer.start()
        }
        videoView.start()    }

    private fun getDataFromActivity2() {
        val amountString = intent.getStringExtra("Extra_ques")
        val difficultyString = intent.getStringExtra("Extra_diff")
        val categoryString = intent.getStringExtra("Extra_Category")
        val typeString = intent.getStringExtra("Extra_Type")
        amount = amountString?.toIntOrNull() ?: 0
        difficulty = when (difficultyString) {
            "0" -> "easy"
            "1" -> "medium"
            "2" -> "hard"
            else -> ""
        }
        category = categoryString?.toIntOrNull() ?: 0
        type = when (typeString) {
            "0" -> "multiple"
            "1" -> "boolean"
            "2" -> ""
            else -> ""
        }
        if(category!=-1)
        {
            category+=9
        }
        else {
            category=0
        }
    }


    private fun getMyData() {
x=0;
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiInterface = retrofit.create(ApiInterface::class.java)


        val call = apiInterface.getData(amount, difficulty,type, category)

        call.enqueue(object : Callback<MyResponse> {
            override fun onResponse(call: Call<MyResponse>, response: Response<MyResponse>) {
                val myResponse = response.body()

                // Access the data
                val myDataList = myResponse?.results
                if (myDataList != null) {
                  try{

                      for (myData in myDataList) {
                        val decodedQuestion = HtmlCompat.fromHtml(myData.question, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
                        val decodedoption1 = HtmlCompat.fromHtml(myData.correct_answer, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
                          val decodedoption2 = HtmlCompat.fromHtml(myData.incorrect_answers[0], HtmlCompat.FROM_HTML_MODE_LEGACY).toString()

                          if (myData.incorrect_answers.size >= 3) {
                              val decodedoption3 = HtmlCompat.fromHtml(myData.incorrect_answers[1], HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
                              val decodedoption4 = HtmlCompat.fromHtml(myData.incorrect_answers[2], HtmlCompat.FROM_HTML_MODE_LEGACY).toString()

                              when((1..4).random()){
                              1 ->questionsList.add(questions1(decodedQuestion,decodedoption1,decodedoption2,decodedoption3,decodedoption4,decodedoption1))
                              2-> questionsList.add(questions1(decodedQuestion,decodedoption2,decodedoption1,decodedoption3,decodedoption4,decodedoption1))
                              3->questionsList.add(questions1(decodedQuestion,decodedoption3,decodedoption2,decodedoption1,decodedoption4,decodedoption1))
                              4->questionsList.add(questions1(decodedQuestion,decodedoption4,decodedoption2,decodedoption3,decodedoption1,decodedoption1))

                              }
                              // Rest of the code}
                          } else {
                              val a=(1..2).random()
                              when(a){
                                  1 ->questionsList.add(questions1(decodedQuestion,decodedoption1,decodedoption2,"","",decodedoption1))
                                  2-> questionsList.add(questions1(decodedQuestion,decodedoption2,decodedoption1,"","",decodedoption1))}
                          }



                    }
                }
                  catch(e: Exception)
                  {

                      Log.e("Error",  e.toString())
                  //    val errorMessage = "An error occurred: ${e.toString()}"

                  }
                }

            }

            override fun onFailure(call: Call<MyResponse>, t: Throwable) {
                Log.d(TAG, "onFailure: " + t.message)
                val errorMessage = "An error occurred: ${t.toString()}"
                val toast= Toast(applicationContext)
                toast.duration= Toast.LENGTH_SHORT
                val toastLayout=layoutInflater.inflate(R.layout.custom_toast,null)
                val toastTextView = toastLayout.findViewById<TextView>(R.id.ans)
                val toastImageView=toastLayout.findViewById<ImageView>(R.id.imageView)
                toastTextView.text = "NO INTERNET"
                toast.view = toastLayout
                toast.show()
                binding.textView2.text=errorMessage

            }
        })


    }


    private fun countDown() {
        countdownTimer?.cancel()
        val duration: Long = TimeUnit.SECONDS.toMillis(11)
        countdownTimer=object : CountDownTimer(duration, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val sDuration: String = String.format(
                    Locale.ENGLISH, "%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                            TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                            )
                )
                binding.time.text = sDuration
            }

            override fun onFinish() {
                disable()
                val toast= Toast(applicationContext)
              //  val checkedQuestion=binding.rgroup.checkedRadioButtonId

                toast.duration= Toast.LENGTH_SHORT
                val answer=questionmodel.answer
              //  val checked = findViewById<RadioButton>(checkedQuestion)
               // val answer=questionmodel.answer
               // val checkedText = checked?.text?.toString()
              //  checked?.setBackgroundColor(Color.GREEN)
                val toastLayout=layoutInflater.inflate(R.layout.custom_toast,null)
                val toastTextView = toastLayout.findViewById<TextView>(R.id.ans)
                val toastImageView=toastLayout.findViewById<ImageView>(R.id.imageView)
                toastTextView.text = "TIME OUT"
                val canswer = toastLayout.findViewById<TextView>(R.id.correctAnswer)
                canswer.visibility= View.VISIBLE
                canswer.text="ANSWER IS: "+answer
                toastImageView.setImageResource(R.drawable.ic_wrong)
                toast.view = toastLayout
                toast.show()
                binding.score.text="Score:" +x.toString()+"/"+((index)*10).toString()

            }
        }.start() // Start the countdown
    }

    private fun disable( )
    {
        val radioGroup = findViewById<RadioGroup>(R.id.rgroup)

        for (i in 0 until radioGroup.childCount) {
            val radioButton = radioGroup.getChildAt(i) as RadioButton
            radioButton.isEnabled = false
        }

    }

    private fun enable()
    {
        val radioGroup=findViewById<RadioGroup>(R.id.rgroup)

        for(i in 0 until radioGroup.childCount)
        {
            val radioButton=radioGroup.getChildAt(i) as RadioButton
            radioButton.isEnabled=true
        }

    }


    private fun checkAnswer( )
    {
        countdownTimer?.cancel()
        val checkedQuestion=binding.rgroup.checkedRadioButtonId
        if (checkedQuestion != View.NO_ID) {
            val checked = findViewById<RadioButton>(checkedQuestion)
            val answer=questionmodel.answer
            val checkedText = checked?.text?.toString()
            if(answer==checkedText)  {checked?.setBackgroundResource(R.drawable.correct)
                val toast= Toast(applicationContext)
                toast.duration= Toast.LENGTH_SHORT
                toast.view=layoutInflater.inflate(R.layout.custom_toast,null)
                toast.show()
                x=x+10;
                binding.score.text="Score:" +x.toString()+"/"+((index)*10).toString()
                binding.check.isEnabled=false


            }
            else
            {
                checked?.setBackgroundResource(R.drawable.incorrect)
                val toast= Toast(applicationContext)
                toast.duration= Toast.LENGTH_SHORT
                val toastLayout=layoutInflater.inflate(R.layout.custom_toast,null)
                val toastTextView = toastLayout.findViewById<TextView>(R.id.ans)
                val toastImageView=toastLayout.findViewById<ImageView>(R.id.imageView)
                toastTextView.text = "WRONG ANSWER"
                val canswer = toastLayout.findViewById<TextView>(R.id.correctAnswer)
canswer.visibility= View.VISIBLE
                canswer.text="ANSWER IS: "+answer
                toastImageView.setImageResource(R.drawable.ic_wrong)
                toast.view = toastLayout
                toast.show()
                binding.score.text="Score:" +x.toString()+"/"+((index)*10).toString()

                binding.check.isEnabled=false

            }

        }

    }

            private fun setAllQuestions() {

binding.question.text=questionmodel.question
        binding.button1.text=questionmodel.option1
        binding.button2.text=questionmodel.option2

        binding.button3.text=questionmodel.option3
        binding.button4.text=questionmodel.option4
if(        binding.button3.text=="")
{
    binding.button3.visibility=View.GONE
    binding.button4.visibility=View.GONE

}

    }


private fun nextQuestion()
{
    binding.score.text="Score:" +x.toString()+"/"+((index)*10).toString()

    countdownTimer?.cancel()
countDown()
    binding.rgroup.setOnCheckedChangeListener { group, checkedId ->
        val checkButton = findViewById<Button>(R.id.check)
        checkButton.isEnabled = checkedId != View.NO_ID
    }
    if(index<questionsList.size)
    {     binding.rgroup.visibility=View.VISIBLE
        binding.textView2.text=index.toString()
        questionmodel=questionsList[index]
        binding.button3.visibility=View.VISIBLE
        binding.button4.visibility=View.VISIBLE
        setAllQuestions()
        index++

    }
    else
    {
        countdownTimer?.cancel()

        binding.question.visibility=View.GONE
        binding.rgroup.visibility=View.GONE
        binding.button.isEnabled=false
        binding.newRound.visibility=View.VISIBLE
        binding.time.visibility=View.INVISIBLE
        binding.imageView2.visibility=View.INVISIBLE
        binding.trophy.visibility=View.VISIBLE
        binding.button.visibility=View.INVISIBLE
        binding.check.visibility=View.INVISIBLE
        val toast= Toast(applicationContext)
        toast.duration= Toast.LENGTH_SHORT
        val toastLayout=layoutInflater.inflate(R.layout.custom_toast,null)
        val toastTextView = toastLayout.findViewById<TextView>(R.id.ans)
        toastTextView.text = "CONGRATULATIONS"
        toast.view = toastLayout
        toast.show()
    }
}



}