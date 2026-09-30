package com.example.flixter

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

class MainActivity : AppCompatActivity() {

    private val movies = mutableListOf<Movie>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rvMovies = findViewById<RecyclerView>(R.id.rvMovies)

        val movieAdapter = MovieAdapter(movies)

        rvMovies.layoutManager = LinearLayoutManager(this)
        rvMovies.adapter = movieAdapter

        val client = AsyncHttpClient()

        client.get(
            "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed",
            object : JsonHttpResponseHandler() {

                override fun onSuccess(
                    statusCode: Int,
                    headers: Headers,
                    json: JSON
                ) {
                    Log.d("MainActivity", "Response successful: $json")

                    val results = json.jsonObject.getJSONArray("results")

                    for (i in 0 until results.length()) {
                        val movieJson = results.getJSONObject(i)

                        val movie = Movie(
                            title = movieJson.getString("title"),
                            overview = movieJson.getString("overview"),
                            posterPath = movieJson.getString("poster_path")
                        )

                        movies.add(movie)
                    }

                    movieAdapter.notifyDataSetChanged()
                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Headers?,
                    response: String,
                    throwable: Throwable?
                ) {
                    Log.e("MainActivity", "API request failed: $response")
                }
            }
        )
    }
}