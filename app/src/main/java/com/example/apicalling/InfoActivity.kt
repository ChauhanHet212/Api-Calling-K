package com.example.apicalling

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apicalling.adapter.CastAdapter
import com.example.apicalling.adapter.EpisodeAdapter
import com.example.apicalling.model.Cast
import com.example.apicalling.model.Episode
import com.example.apicalling.model.Show
import com.example.apicalling.retrofit.RetroAPI
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class InfoActivity : AppCompatActivity() {

    lateinit var main_bg: ImageView
    lateinit var img: ImageView
    lateinit var name: TextView
    lateinit var language: TextView
    lateinit var type: TextView
    lateinit var runtime: TextView
    lateinit var premiered: TextView
    lateinit var ended: TextView
    lateinit var offsite: TextView
    lateinit var rating: TextView
    lateinit var about: TextView
    lateinit var cast: TextView
    lateinit var recycler: RecyclerView
    lateinit var epRecycler: RecyclerView
    lateinit var allCastList: List<Cast>
    lateinit var allEpisodeList: List<Episode>
    lateinit var moreBtn: LinearLayout
    lateinit var adapter: EpisodeAdapter
    lateinit var moreBtnTxt: TextView
    lateinit var moreBtnImg: ImageView

    var less = true
    val episodeList: ArrayList<Episode> = arrayListOf()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        main_bg = findViewById(R.id.main_bg)
        img = findViewById(R.id.info_img)
        name = findViewById(R.id.info_name)
        language = findViewById(R.id.info_language)
        type = findViewById(R.id.info_type)
        runtime = findViewById(R.id.info_runtime)
        premiered = findViewById(R.id.info_premiered)
        ended = findViewById(R.id.info_ended)
        offsite = findViewById(R.id.info_offsite)
        rating = findViewById(R.id.info_rating)
        about = findViewById(R.id.info_about)
        cast = findViewById(R.id.cast)
        recycler = findViewById(R.id.recycler)
        epRecycler = findViewById(R.id.epRecycler)
        moreBtn = findViewById(R.id.moreBtn)
        moreBtnTxt = findViewById(R.id.moreBtnTxt)
        moreBtnImg = findViewById(R.id.moreBtnImg)

        val show: Show = intent.getSerializableExtra("data") as Show

        val dialog = ProgressDialog(this)
        dialog.setCancelable(false)
        dialog.setMessage("Loading...")
        dialog.show()

        try {
            Picasso.get().load(show.image.original).placeholder(R.drawable.placeholder).into(main_bg)
            Picasso.get().load(show.image.original).placeholder(R.drawable.placeholder).into(img)
        } catch (e: Exception) {
            main_bg.setBackgroundColor(resources.getColor(R.color.white))
            findViewById<CardView?>(R.id.card).visibility = View.GONE
            img.visibility = View.GONE
        }
        name.setText(show.name)
        language.setText(show.language)

        var str = ""
        for (i in 0 until show.genres.size){
            if (i == show.genres.lastIndex){
                str += show.genres.get(i)
            } else {
                str += "${show.genres.get(i)},\n"
            }
        }
        type.setText(str)
        runtime.setText(show.runtime.toString())
        premiered.setText(show.premiered)
        if (show.ended != null) ended.setText(show.ended) else ended.setText("-")
        if (show.officialSite != null){
            offsite.setText(Html.fromHtml("<U>${show.officialSite}</U>"))
            offsite.setOnClickListener(object : View.OnClickListener{
                override fun onClick(p0: View?) {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(show.officialSite)
                    startActivity(intent)
                }
            })
        } else offsite.setText("-")
        if (show.rating != null) rating.setText(show.rating.average.toString()) else rating.setText("-")
        if (show.summary != null) about.setText(Html.fromHtml(show.summary)) else about.setText("-")

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.tvmaze.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val retroAPI = retrofit.create(RetroAPI::class.java)

        retroAPI.getEpisodes(show.id).enqueue(object : Callback<List<Episode>>{
            override fun onResponse(call: Call<List<Episode>>, response: Response<List<Episode>>) {
                moreBtn.visibility = View.VISIBLE
                dialog.dismiss()

                allEpisodeList = response.body()!!

                try {
                    for (i in 0..9){
                        episodeList.add(allEpisodeList[i])
                    }
                } catch (e: Exception) {
                    for (i in 0..(allEpisodeList.size/2)){
                        episodeList.add(allEpisodeList[i])
                    }
                }

                epRecycler.layoutManager = LinearLayoutManager(this@InfoActivity)
                adapter = EpisodeAdapter(this@InfoActivity, episodeList)
                epRecycler.adapter = adapter
            }

            override fun onFailure(call: Call<List<Episode>>, t: Throwable) {
                dialog.dismiss()
                Log.d("hhh", "onFailure: ${t.localizedMessage}")
            }
        })

        retroAPI.getCast(show.id).enqueue(object :Callback<List<Cast>>{
            override fun onResponse(call: Call<List<Cast>>, response: Response<List<Cast>>) {
                cast.visibility = View.VISIBLE
                recycler.visibility = View.VISIBLE

                allCastList = response.body()!!

                recycler.layoutManager = LinearLayoutManager(this@InfoActivity, RecyclerView.HORIZONTAL, false)
                recycler.adapter = CastAdapter(this@InfoActivity, allCastList)
            }

            override fun onFailure(call: Call<List<Cast>>, t: Throwable) {
                cast.visibility = View.GONE
                recycler.visibility = View.GONE
                Log.d("hhh", "onFailure: ${t.localizedMessage}")
            }
        })

        moreBtn.setOnClickListener(View.OnClickListener { view ->
            if (less){
                adapter.change(allEpisodeList)
                moreBtnTxt.setText("Less")
                moreBtnImg.setImageResource(R.drawable.ic_baseline_expand_less_24)
                less = false
            } else {
                adapter.change(episodeList)
                moreBtnTxt.setText("More")
                moreBtnImg.setImageResource(R.drawable.ic_baseline_expand_more_24)
                less = true
            }
        })
    }
}