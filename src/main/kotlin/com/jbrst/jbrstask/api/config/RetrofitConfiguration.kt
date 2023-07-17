package com.jbrst.jbrstask.api.config

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Configuration
open class RetrofitConfiguration {

    @Bean
    open fun gson(): Gson = GsonBuilder().setPrettyPrinting().create()


    @Bean
    open fun retrofit(): Retrofit.Builder {
        return Retrofit.Builder().addConverterFactory(GsonConverterFactory.create(gson()))
    }

}