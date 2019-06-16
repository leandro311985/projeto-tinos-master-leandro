package br.com.iresult.gmfmobile.koin

import android.content.Context
import androidx.room.Room
import br.com.iresult.gmfmobile.model.database.AppDataBase
import br.com.iresult.gmfmobile.service.GeocodingService
import br.com.iresult.gmfmobile.service.LoginService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object ServiceModules {

    fun provideDataBase(context: Context): AppDataBase {
        return Room.databaseBuilder(context, AppDataBase::class.java, "gmf-tinos-app-db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()
    }

    private fun getClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)
        return httpClient.build()
    }

    fun provideGson(): Gson {
        return GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .setLenient()
                .create()
    }

    fun provideLoginService(gson: Gson): LoginService {
        return Retrofit.Builder()
                .baseUrl("http://gmfmobile.hospedagemdesites.ws/")
          //      .baseUrl("http://192.168.15.14:8080/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(getClient())
                .build()
                .create(LoginService::class.java)
    }

    fun provideGeocodeService(gson: Gson): GeocodingService {
        return Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(getClient())
                .build()
                .create(GeocodingService::class.java)
    }
}