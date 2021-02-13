package com.ninetyninex.googlebookssearch.injection

import androidx.preference.PreferenceManager
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.ninetyninex.googlebookssearch.BuildConfig
import com.ninetyninex.googlebookssearch.app.App
import com.ninetyninex.googlebookssearch.network.ApiService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class AppModule(val app: App) {

    @Provides
    @Singleton
    @ForApplication
    fun provideApp() = app

    @Provides
    @Singleton
    fun provideSharedPreference() = PreferenceManager.getDefaultSharedPreferences(app)!!

    /**
     * Provides the lock service implementation.
     * @param retrofit the Retrofit object used to instantiate the service
     * @return the lock service implementation.
     */
    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create().withNullSerialization())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .addNetworkInterceptor(StethoInterceptor())
                    .addInterceptor(interceptor).build()
            )
            .build()
            .create(ApiService::class.java)
    }
}