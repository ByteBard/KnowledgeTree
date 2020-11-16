package com.example.knowledgetree
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*
@Parcelize
data class Task(
    val taskId: String, val issueId: String, val name: String,
    val completed: Boolean, val createdTimestamp: Date, val lastAccessTimestamp: Date
) : Parcelable {
    constructor() : this("", "", "", false, Calendar.getInstance().time,
        Calendar.getInstance().time) {}
}