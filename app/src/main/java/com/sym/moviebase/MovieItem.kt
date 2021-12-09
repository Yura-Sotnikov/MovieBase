package com.sym.moviebase

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieItem(val title: String, val description: String, val poster: Int) : Parcelable
