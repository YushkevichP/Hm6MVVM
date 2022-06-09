package com.example.hm6_mvvm_koin.koin

import com.example.hm6_mvvm_koin.retrofit.RickMortyApi
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

//https://youtu.be/NzHB9KrQAxk?t=2050

val networkModule = module {

    single {
        OkHttpClient.Builder()
            .readTimeout(20, TimeUnit.SECONDS)
            .build()
    }

    //create retrofit
    single {
        Retrofit.Builder()
            .baseUrl("https://rickandmortyapi.com/api/")
            .addConverterFactory(GsonConverterFactory.create()) // преобразует json объекты в наши оюъекты
            .client(get())
            .build()
    }

    //create api
    single {
        get<Retrofit>().create<RickMortyApi>()
    }
}