package wacode.yamada.yuki.nempaymentapp.rest

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import wacode.yamada.yuki.nempaymentapp.helper.ActiveNodeHelper
import wacode.yamada.yuki.nempaymentapp.types.NodeType
import java.util.concurrent.TimeUnit

object ApiManager {
    const val API_NEM_BOOK_URL = "https://s3-ap-northeast-1.amazonaws.com/xembook.net/"
    const val API_ZAIF_URL = "https://api.zaif.jp/"
    const val API_NODE_EXPLORER = "https://nodeexplorer.com/"

    fun builder(): Retrofit {
        val retrofit = Retrofit.Builder()
                .baseUrl(getBaseUrl())
                .client(builderHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(builderHttpClient())
                .build()
        return retrofit
    }

    fun builderNemBook(): Retrofit {
        val url: String = API_NEM_BOOK_URL
        return Retrofit.Builder()
                .baseUrl(url)
                .client(builderHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(builderHttpClient())
                .build()
    }

    private fun builderHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
        client.connectTimeout(30, TimeUnit.SECONDS)
        client.readTimeout(30, TimeUnit.SECONDS)
        client.writeTimeout(30, TimeUnit.SECONDS)
        client.addInterceptor(logging)
        client.addInterceptor { chain ->
            val original = chain.request()

            val request = original.newBuilder()
                    .header("Accept", "application/json")
                    .method(original.method(), original.body())
                    .build()

            val response = chain.proceed(request)

            response
        }

        return client.build()
    }

    fun builderZaif(): Retrofit {
        val url: String = API_ZAIF_URL
        return Retrofit.Builder()
                .baseUrl(url)
                .client(builderHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(builderHttpClient())
                .build()
    }

    fun builderNodeExplorer(): Retrofit {
        val url: String = API_NODE_EXPLORER
        return Retrofit.Builder()
                .baseUrl(url)
                .client(builderHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(builderHttpClient())
                .build()
    }

    fun getBaseUrl() =
            ActiveNodeHelper.selectedNodeType?.let { "http://" + it.nodeBaseUrl }
                    ?: run { "http://" + NodeType.ALICE2.nodeBaseUrl }

}