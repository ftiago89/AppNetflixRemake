package com.felipe.netflixremake.kotlin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.felipe.netflixremake.R
import com.felipe.netflixremake.model.Categories
import com.felipe.netflixremake.model.Category
import com.felipe.netflixremake.model.Movie
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.category_item.view.*
import kotlinx.android.synthetic.main.movie_item.view.*
import retrofit2.*

class MainActivity : AppCompatActivity() {

    private lateinit var mainAdapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = recycler_view_main
        val categories = arrayListOf<Category>()
        mainAdapter = MainAdapter(categories)

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = mainAdapter

        retrofit().create(NetflixAPI::class.java)
            .listCategories()
            .enqueue(object: Callback<Categories> {
                override fun onResponse(call: Call<Categories>, response: Response<Categories>) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            mainAdapter.categories.clear()
                            mainAdapter.categories.addAll(it.categories)
                            mainAdapter.notifyDataSetChanged()
                        }
                    }
                }

                override fun onFailure(call: Call<Categories>, t: Throwable) {
                    Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_SHORT).show()
                }

            })
    }

    private inner class MainAdapter(val categories: MutableList<Category>) : RecyclerView.Adapter<CategoryHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder = CategoryHolder(layoutInflater.inflate(R.layout.category_item, parent, false))

        override fun onBindViewHolder(holder: CategoryHolder, position: Int) = holder.bind(categories[position])

        override fun getItemCount(): Int = categories.size
    }

    private inner class MovieAdapter(val movies: List<Movie>, private val listener: ((Movie) -> Unit)?) : RecyclerView.Adapter<MovieHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder = MovieHolder(layoutInflater.inflate(R.layout.movie_item, parent, false), listener)

        override fun onBindViewHolder(holder: MovieHolder, position: Int) = holder.bind(movies[position])

        override fun getItemCount(): Int = movies.size
    }

    private inner class CategoryHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(category: Category) {
            itemView.text_view_category_name.text = category.name
            itemView.recycler_view_category.adapter = MovieAdapter(category.movies) {
                if(it.id > 3) {
                    Toast.makeText(this@MainActivity, "Esta funcionalidade não foi implementada", Toast.LENGTH_LONG).show()
                } else {
                    val intent = Intent(this@MainActivity, MovieActivity::class.java)
                    intent.putExtra("id", it.id)
                    startActivity(intent)
                }
            }
            itemView.recycler_view_category.layoutManager = LinearLayoutManager(this@MainActivity,
                RecyclerView.HORIZONTAL, false)
        }
    }

    private class MovieHolder(itemView: View, val onClick: ((Movie) -> Unit)?) : RecyclerView.ViewHolder(itemView) {
        fun bind(movie: Movie) = with(itemView){
            Glide.with(context)
                .load(movie.coverUrl)
                .placeholder(R.drawable.placeholder_bg)
                .into(image_view_cover)
            image_view_cover.setOnClickListener() {
                onClick?.invoke(movie)
            }
        }
    }
}