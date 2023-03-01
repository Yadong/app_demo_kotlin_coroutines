package com.abbreviationdemo.models

import com.abbreviationdemo.AbbreviationAPIs
import javax.inject.Inject

open class AbbreviationManager @Inject constructor(
    private val abbreviationAPIs: AbbreviationAPIs
){
    suspend fun getFullForms(abbreviation: String): List<FullForms> = abbreviationAPIs.getFullForms(abbreviation)
}