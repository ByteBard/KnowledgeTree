package com.example.knowledgetree
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*
@Parcelize
data class User(
    val userId: String,
    val username: String,
    val password: String,
    val createdTimestamp: Date,
    val lastAccessTimestamp: Date
) : Parcelable