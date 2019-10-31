package com.example.tutorme.models

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName

data class Student(
    @get:Exclude var id: String? = null,
    @JvmField @PropertyName(FIRST_NAME) var first_name: String? = null,
    @JvmField @PropertyName(LAST_NAME) var last_name: String? = null,
    @JvmField @PropertyName(EMAIL) var email: String? = null,
    @JvmField @PropertyName(PROFILE_PICTURE_URL) var profile_picture_url: String? = null,
    @JvmField @PropertyName(SCHOOL) var school: String? = null
) {
    companion object {
        const val FIRST_NAME = "first_name"
        const val LAST_NAME = "last_name"
        const val PROFILE_PICTURE_URL = "profile_picture_url"
        const val SCHOOL = "school"
        const val EMAIL = "email"
    }
}