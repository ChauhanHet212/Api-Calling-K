package com.example.apicalling

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apicalling.adapter.ShowAdapter
import com.example.apicalling.model.Searchedshow
import com.example.apicalling.model.Show
import com.example.apicalling.retrofit.RetroAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    lateinit var recycler: RecyclerView
    lateinit var progress: ProgressBar
    lateinit var search: SearchView
    lateinit var adapter: ShowAdapter
    lateinit var allShowList: List<Show>

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycler = findViewById(R.id.recycler)
        progress = findViewById(R.id.progress)
        search = findViewById(R.id.searchView)

        search.visibility = View.GONE

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.tvmaze.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val retroAPI = retrofit.create(RetroAPI::class.java)

        retroAPI.getAllShows().enqueue(object : Callback<List<Show>>{
            override fun onResponse(call: Call<List<Show>>, response: Response<List<Show>>) {
                search.visibility = View.VISIBLE
                progress.visibility = View.GONE
                allShowList = response.body()!!

                recycler.layoutManager = GridLayoutManager(this@MainActivity, 2)
                adapter = ShowAdapter(this@MainActivity, allShowList)
                recycler.adapter = adapter
            }

            override fun onFailure(call: Call<List<Show>>, t: Throwable) {
                progress.visibility = View.GONE
                Log.d("hhh", "onFailure: ${t.localizedMessage}")
            }

        })

        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                recycler.visibility = View.GONE
                progress.visibility = View.VISIBLE

                if (!newText.equals("")) {
                    if (newText != null) {
                        retroAPI.searchShow(newText).enqueue(object : Callback<List<Searchedshow>>{
                            override fun onResponse(call: Call<List<Searchedshow>>, response: Response<List<Searchedshow>>) {
                                progress.visibility = View.GONE
                                val filteredList: ArrayList<Show> = arrayListOf()

                                for (item in response.body()!!){
                                    filteredList.add(item.show)
                                }

                                adapter.filterShow(filteredList)
                                recycler.visibility = View.VISIBLE
                            }

                            override fun onFailure(call: Call<List<Searchedshow>>, t: Throwable) {
                                progress.visibility = View.GONE
                                Log.d("hhh", "onFailure: ${t.localizedMessage}")
                            }

                        })
                    }
                } else {
                    adapter.filterShow(allShowList)
                    progress.visibility = View.GONE
                    recycler.visibility = View.VISIBLE
                }

                return true
            }
        })
    }
}