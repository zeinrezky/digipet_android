package com.stellkey.android.util

import com.stellkey.android.helper.extension.emptyString
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class HeaderInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original: Request = chain.request()
        val request: Request = original.newBuilder()
            .header(
                Constant.Header.AUTH,
                if (AppPreference.isKidLogin())
                    if (AppPreference.getKidToken() == emptyString) AppPreference.getLoginToken() else AppPreference.getKidToken()
                else
                    if (AppPreference.getCarerToken() == emptyString) AppPreference.getLoginToken() else AppPreference.getCarerToken()
            )
            .build()
        return chain.proceed(request)
    }
}