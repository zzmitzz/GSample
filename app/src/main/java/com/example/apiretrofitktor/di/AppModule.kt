package com.example.apiretrofitktor.di

import android.content.Context
import androidx.room.Room
import com.example.apiretrofitktor.data.Constant.API_LINK
import com.example.apiretrofitktor.data.Constant.httpLoggingInterceptor
import com.example.apiretrofitktor.data.local.LocalService
import com.example.apiretrofitktor.data.local.room.AppDataBase
import com.example.apiretrofitktor.data.local.room.PokemonLocalDAO
import com.example.apiretrofitktor.data.remote.NetworkService
import com.example.apiretrofitktor.data.remote.retrofit.ApiServicePokemon
import com.example.apiretrofitktor.data.remote.retrofit.RetrofitService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private val okHttpClient: OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor { chain ->
                val request =
                    chain
                        .request()
                        .newBuilder()
                        .addHeader("Content-Type", "application/json")
                        .build()
                chain.proceed(request)
            }
            .addInterceptor(httpLoggingInterceptor)
            .build()
    @Provides
    @Singleton
    fun getRetrofit(): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl(API_LINK)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun getApiService(retrofit: Retrofit): ApiServicePokemon {
        return retrofit.create(ApiServicePokemon::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext appContext: Context
    ): AppDataBase {
        return Room.databaseBuilder(
            appContext,
            AppDataBase::class.java,
            "app_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideUserDao(database: AppDataBase): PokemonLocalDAO {
        return database.pokemonLocalDAO()
    }
}

@Module
//Repositories will live same as the activity that requires them
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideNetworkServiceImpl(retrofit: RetrofitService): NetworkService

    @Binds
    abstract fun provideLocalService(pokemonLocalDAO: PokemonLocalDAO): LocalService
}