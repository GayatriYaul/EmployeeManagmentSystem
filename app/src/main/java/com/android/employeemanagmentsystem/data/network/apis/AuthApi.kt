package com.android.employeemanagmentsystem.data.network.apis

import com.android.employeemanagmentsystem.data.models.responses.Employee
import com.android.employeemanagmentsystem.utils.BASE_URL
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthApi {

    //api request for login of all employees of all roles
    @FormUrlEncoded
    @POST("Authentication/login")
    suspend fun employeeLogin(
        @Field("email") email: String,
        @Field("password") password: String,
    ): Response<Employee>


    companion object{
        operator fun invoke(
            //networkConnectionInterceptor: NetworkConnectionInterceptor
        ) : AuthApi {

//            val okkHttpclient = OkHttpClient.Builder()
//                .addInterceptor(networkConnectionInterceptor)
//                .build()

            return Retrofit.Builder()
                //.client(okkHttpclient)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(AuthApi::class.java)
        }
    }

}

