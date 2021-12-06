package com.example.movielist.di

import com.example.movielist.data.MovieRepository
import com.example.movielist.network.MovieService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

const val BASE_URL = "https://api.themoviedb.org/3/"
const val API_KEY = "bfe801649ca860d496a1b7a533405418"
const val TMDB_IMG_URL = "https://image.tmdb.org/t/p/original"

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {


    @Singleton
    @Provides
    fun provideMovieService(retrofit: Retrofit): MovieService {
        return retrofit.create(MovieService::class.java)
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
    ): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Provides
    fun provideHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        apiKeyInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(apiKeyInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Provides
    fun provideApiKeyInterceptor(): Interceptor {
        val interceptor = Interceptor {
            val url = it.request()
                .url
                .newBuilder()
                .addQueryParameter("api_key", API_KEY)
                .addQueryParameter("language", LANGUAGE)
                .addQueryParameter("region", REGION)
                .addQueryParameter("append_to_response", TRAILERS)
                .build()
            val request = it.request()
                .newBuilder()
                .url(url)
                .build()
            return@Interceptor it.proceed(request)
        }
        return interceptor
    }

    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }
}