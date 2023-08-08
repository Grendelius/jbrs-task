package com.jbrst.jbrstask.api.config

import com.google.gson.GsonBuilder
import com.jbrst.jbrstask.api.ApiServiceCreator
import com.jbrst.jbrstask.api.client.*
import com.jbrst.jbrstask.core.UserData
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Configuration
open class RetrofitConfiguration {

    @Bean
    open fun retrofit(): Retrofit.Builder {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setPrettyPrinting().create()))
    }

    @Bean
    @DependsOn("retrofit")
    open fun apiServiceCreator(@Autowired userData: UserData): ApiServiceCreator {
        return ApiServiceCreator(retrofit(), userData)
    }

    @Bean
    open fun projectApi(apiServiceCreator: ApiServiceCreator): ProjectApi {
        return apiServiceCreator.createService(ProjectApi::class.java)
    }

    @Bean
    open fun buildApi(apiServiceCreator: ApiServiceCreator): BuildApi {
        return apiServiceCreator.createService(BuildApi::class.java)
    }

    @Bean
    open fun vcsRootApi(apiServiceCreator: ApiServiceCreator): VcsRootApi {
        return apiServiceCreator.createService(VcsRootApi::class.java)
    }

    @Bean
    open fun buildTypeApi(apiServiceCreator: ApiServiceCreator): BuildTypeApi {
        return apiServiceCreator.createService(BuildTypeApi::class.java)
    }

    @Bean
    open fun buildQueueApi(apiServiceCreator: ApiServiceCreator): BuildQueueApi {
        return apiServiceCreator.createService(BuildQueueApi::class.java)
    }

    @Bean
    open fun agentApi(apiServiceCreator: ApiServiceCreator): AgentApi {
        return apiServiceCreator.createService(AgentApi::class.java)
    }

    @Bean
    open fun userApi(apiServiceCreator: ApiServiceCreator): UsersApi {
        return apiServiceCreator.createService(UsersApi::class.java)
    }

}