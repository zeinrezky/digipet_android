package com.stellkey.android.di

import android.content.Context
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.haroldadmin.cnradapter.NetworkResponseAdapterFactory
import com.stellkey.android.repository.UserService
import com.stellkey.android.util.CacheInterceptor
import com.stellkey.android.util.Constant
import com.stellkey.android.util.HeaderInterceptor
import okhttp3.Cache
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

val NetworkModule = module {
    single { createOkHttpClient(get()) }
    single { createWebService<UserService>(get()) }
}

fun createOkHttpClient(applicationContext: Context): OkHttpClient {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

    val cacheInterceptor = CacheInterceptor()
    val headerInterceptor = HeaderInterceptor()

    val httpCacheDirectory = File(applicationContext.cacheDir, "http-cache")
    val cacheSize = 10 * 1024 * 1024 // 10 MiB
    val cache = Cache(httpCacheDirectory, cacheSize.toLong())

    return OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(httpLoggingInterceptor)
        .addInterceptor(cacheInterceptor)
        .addInterceptor(headerInterceptor)
        .cache(cache)
        .connectionSpecs(listOf(ConnectionSpec.CLEARTEXT, ConnectionSpec.MODERN_TLS))
        .build()
}

inline fun <reified T> createWebService(okHttpClient: OkHttpClient): T {
    val gson = GsonBuilder()
        .enableComplexMapKeySerialization()
        .serializeNulls()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
        .setPrettyPrinting()
        .setVersion(1.0)
        .create()

    val retrofit = Retrofit.Builder()
        .baseUrl(Constant.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(NetworkResponseAdapterFactory())
        .build()

    return retrofit.create(T::class.java)
}