package com.abbreviationdemo

import com.abbreviationdemo.models.FullForm
import com.abbreviationdemo.models.FullForms
import retrofit2.http.GET
import retrofit2.http.Query

interface AbbreviationAPIs {
    @GET("dictionary.py")
    suspend fun getFullForms(@Query("sf") sf: String): List<FullForms>
}
