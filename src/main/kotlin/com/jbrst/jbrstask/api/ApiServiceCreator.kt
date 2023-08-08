package com.jbrst.jbrstask.api

import com.google.common.net.HttpHeaders.AUTHORIZATION
import com.jbrst.jbrstask.core.UserData
import com.jbrst.jbrstask.core.models.User
import io.qameta.allure.okhttp3.AllureOkHttp3
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.springframework.beans.factory.annotation.Value
import retrofit2.Retrofit

class ApiServiceCreator(private val retrofit: Retrofit.Builder, private val userData: UserData) {

    @Value("\${tc.server.api.host}")
    internal lateinit var tcServerApiHost: String

    companion object {
        private fun createHttpClient(vararg interceptors: Interceptor): OkHttpClient {
            return OkHttpClient.Builder().apply {
                interceptors.forEach { addInterceptor(it) }
            }.build()
        }

        private fun loggingInterceptor() =
            HttpLoggingInterceptor().apply {
                setLevel(HttpLoggingInterceptor.Level.BODY)
            }

        private fun basicAuthInterceptor(user: User) =
            Interceptor {
                val token = Credentials.basic(user.username, user.password)
                val request = it.request().newBuilder()
                    .addHeader(AUTHORIZATION, token)
                    .build()
                it.proceed(request)
            }
    }

    private fun buildBasicAuthUrl(user: User) = "http://${user.username}:${user.password}@${tcServerApiHost}"

    fun <T> createService(clazz: Class<T>, user: User = userData.superAdmin()): T {
        return retrofit.baseUrl(buildBasicAuthUrl(user))
            .client(createHttpClient(loggingInterceptor(), basicAuthInterceptor(user), AllureOkHttp3()))
            .build()
            .create(clazz)
    }

}