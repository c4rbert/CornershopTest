package gentera.yastas.yas_app_client_gestion_ventas.api.client

import android.content.Context
import com.cornershop.counterstest.domain.builder.NetworkModule

class Client constructor(val context: Context){

    fun getClient(cls: Class<*>): Any?{
        return NetworkModule.provideRetrofitClient(context).create(cls)
    }
}