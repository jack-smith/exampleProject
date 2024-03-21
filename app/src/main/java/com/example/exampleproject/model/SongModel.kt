package com.example.exampleproject.model

import java.io.Serializable

data class SongModel(val artistName : String, val trackName : String, val trackPrice : String, val artworkUrl100 : String) : Serializable

data class SongResponse(val resultCount : Int, val results : List<SongModel>)
