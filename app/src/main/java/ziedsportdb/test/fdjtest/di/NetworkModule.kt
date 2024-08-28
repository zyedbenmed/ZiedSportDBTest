package ziedsportdb.test.fdjtest.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ziedsportdb.test.fdjtest.BuildConfig
import ziedsportdb.test.fdjtest.features.leagues.data.service.LeaguesApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideLeaguesApi(retrofit: Retrofit): LeaguesApi =
        retrofit.create(LeaguesApi::class.java)

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl("${BuildConfig.BASE_URL}${BuildConfig.APIKEY}/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

    @Provides
    fun providesOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request()
                val requestBuilder = request.newBuilder().build()
                return@addInterceptor chain.proceed(requestBuilder)
            }
        if (BuildConfig.DEBUG) {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addNetworkInterceptor(httpLoggingInterceptor)
        }
        return builder.build()
    }

    @Provides
    fun providesHttpLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor()
}
