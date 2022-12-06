package com.stellkey.android.util

import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.TimeUnit

class CacheInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        val cacheControl = CacheControl.Builder()
            .maxAge(5, TimeUnit.MINUTES) // 5 minutes cache
            .build()
        return response.newBuilder()
            .removeHeader(Constant.Header.CACHE)
            .header(Constant.Header.CACHE, cacheControl.toString())
            .build()
    }
}