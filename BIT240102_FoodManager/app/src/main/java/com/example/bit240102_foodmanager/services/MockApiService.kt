package com.example.bit240102_foodmanager.services

import com.example.bit240102_foodmanager.data.MockApiCategory
import com.example.bit240102_foodmanager.data.MockApiDish
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface MockApiService {
    @GET("categories")
    suspend fun findCategories(@Query("BIT240102_LocalId") localId: Long): List<MockApiCategory>

    @POST("categories")
    suspend fun createCategory(@Body category: MockApiCategory): MockApiCategory

    @PUT("categories/{id}")
    suspend fun updateCategory(
        @Path("id") remoteId: String,
        @Body category: MockApiCategory,
    ): MockApiCategory

    @DELETE("categories/{id}")
    suspend fun deleteCategory(@Path("id") remoteId: String): Response<Unit>

    @GET("dishes")
    suspend fun findDishes(@Query("BIT240102_LocalId") localId: Long): List<MockApiDish>

    @POST("dishes")
    suspend fun createDish(@Body dish: MockApiDish): MockApiDish

    @PUT("dishes/{id}")
    suspend fun updateDish(
        @Path("id") remoteId: String,
        @Body dish: MockApiDish,
    ): MockApiDish

    @DELETE("dishes/{id}")
    suspend fun deleteDish(@Path("id") remoteId: String): Response<Unit>
}
