package com.ninetyninex.googlebookssearch.repository

import android.content.SharedPreferences
import com.ninetyninex.googlebookssearch.data.remote.response.BooksVolumesResponse
import com.ninetyninex.googlebookssearch.network.ApiService
import com.ninetyninex.googlebookssearch.util.Config
import javax.inject.Inject

class BooksVolumesApiRepository @Inject constructor(
    private val sharedPref: SharedPreferences,
    private val apiService: ApiService
) {

    var dataBookVolumes: BooksVolumesResponse?= null
    var dataBookVolumesRetrieved: Boolean = false
    var errorCode: String = ""
    var errorMessage: String = ""

    fun fetchBooks(keyWord: String?) =
        apiService.fetchBooks(keyWord)
            .map {
                if (it.isSuccessful && it.code() == Config.RESPONSE_SUCCESS) {
                    dataBookVolumes = null
                    dataBookVolumes = it.body()
                    dataBookVolumesRetrieved = true
                } else {
                    // error
                    try {
                        errorCode = it.code().toString()
                        errorMessage = it.message()
                    } catch (e: Exception) {
                        e.message.toString()
                    }
                }
            }

}