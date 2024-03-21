package com.example.exampleproject.viewmodel

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.example.exampleproject.model.SongModel
import com.example.exampleproject.model.SongResponse
import com.example.exampleproject.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class SongViewModel : BaseViewModel() {
    val songList  : MutableLiveData<List<SongModel>> = MutableLiveData()
    val sortingByPrice : MutableLiveData<Boolean> = MutableLiveData(false)
    var originalList : List<SongModel> = listOf()

    fun getSongList(searchContent : String = "歌") {
        viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    var reader : BufferedReader? = null
                    try {
                        val connection = URL("https://itunes.apple.com/search?term=${searchContent}&limit=200&country=HK").openConnection() as HttpURLConnection
                        connection.run {
                            requestMethod = "GET"
                            doInput = true
                            doOutput = false
                            connectTimeout = 10000
                            readTimeout = 10000
                            connect()
                        }
                        reader = BufferedReader(InputStreamReader(connection.inputStream, "UTF-8"))
                        var line : String? = reader.readLine()
                        val stringBuilder = StringBuilder()
                        while (line != null) {
                            stringBuilder.append(line)
                            line = reader.readLine()
                        }
                        LogUtils.d(stringBuilder.toString())
                        val data = GsonUtils.fromJson(stringBuilder.toString(), SongResponse::class.java)
                        originalList = data.results
                        withContext(Dispatchers.Main) {
                            songList.value = data.results
                        }

                    } catch (e : Exception) {
                        e.printStackTrace()
                    } finally {
                        reader?.close()
                    }
                }

        }
    }

    fun sortingList(isSort : Boolean) {
        if (isSort) {
            songList.value = originalList.sortedBy { it.trackPrice.toFloat() }
        } else {
            songList.value = originalList.toList()
        }
    }

    fun search(content : String) {
        if (TextUtils.isEmpty(content)) {
            songList.value = originalList.toList()
        } else {
            songList.value = originalList.filter { it.trackName.lowercase().contains(content.lowercase())
                    || it.artistName.lowercase().contains(content.lowercase()) }
        }
    }
}