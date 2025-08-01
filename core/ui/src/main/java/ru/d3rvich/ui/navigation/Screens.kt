package ru.d3rvich.ui.navigation

import kotlinx.serialization.Serializable

object Screens {

    @Serializable
    data object List

    @Serializable
    data class Detail(val id: Int)
}