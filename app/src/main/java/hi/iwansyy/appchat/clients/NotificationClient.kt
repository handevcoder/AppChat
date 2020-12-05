package hi.iwansyy.appchat.clients

import android.util.Log
import com.google.gson.GsonBuilder
import hi.iwansyy.appchat.BuildConfig
import hi.iwansyy.appchat.services.NotificationService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NotificationClient {
    companion object {
        val service: NotificationService by lazy {
            val httpLoggingInterceptor = HttpLoggingInterceptor { Log.e("LOG_API", it) }
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

            val okHttpClient = OkHttpClient()
                .newBuilder()
                .apply {
                    if (BuildConfig.DEBUG) addInterceptor(httpLoggingInterceptor)
                }
                .build()

            val retrofit = Retrofit
                .Builder()
                .baseUrl("https://fcm.googleapis.com/fcm/")
                .client(okHttpClient)
                .addConverterFactory(
                    GsonConverterFactory.create(GsonBuilder().setLenient().create())
                )
                .build()

            retrofit.create(NotificationService::class.java)
        }
    }
}
