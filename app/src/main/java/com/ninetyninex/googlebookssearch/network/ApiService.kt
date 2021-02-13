package com.ninetyninex.googlebookssearch.network

import com.ninetyninex.googlebookssearch.data.remote.response.BooksVolumesResponse
import com.ninetyninex.googlebookssearch.util.Config
import io.reactivex.Flowable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {

    /**
     * Fetch book volumes by given keyword
     *
     * @param keyword
     */
    @Headers(Config.CONTENT_TYPE_JSON)
    @GET("volumes")
    fun fetchBooks(@Query("q") keyword: String?): Flowable<Response<BooksVolumesResponse>>
}