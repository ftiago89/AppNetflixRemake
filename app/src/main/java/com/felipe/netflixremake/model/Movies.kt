package com.felipe.netflixremake.model

import com.google.gson.annotations.SerializedName

data class Categories(@SerializedName("category") val categories: List<Category>)

data class Category(@SerializedName("title")var name: String = "",
                    @SerializedName("movie") var movies: List<Movie> = arrayListOf())

data class Movie(var id: Int = 0,
                 @SerializedName("cover_url") var coverUrl: String = "",
                 var title: String = "",
                 var desc: String = "",
                 var cast: String = "")

data class MovieDetail(val id: Int = 0,
                       @SerializedName("cover_url") var coverUrl: String = "",
                       var title: String = "",
                       var desc: String = "",
                       var cast: String = "",
                       @SerializedName("movie") val similarMovies: List<Movie>)