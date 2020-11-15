package com.example.knowledgetree
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


import java.util.*

@Parcelize
data class Issue(
    val issueId: String,
    val title: String,
    val detail: String,
    val type: String,
    val progress: Int,
    val completed: Boolean,
    val createdTimestamp: Date,
    val lastAccessTimestamp: Date
) : Parcelable {
    constructor() : this("",  "", "", "", 0, false, Calendar.getInstance().time, Calendar.getInstance().time) {}
}
