package ru.d3rvich.history.impl.utils

import kotlinx.datetime.format.MonthNames

internal val MonthNames.Companion.RUSSIAN_FULL: MonthNames
    get() = MonthNames(
        listOf(
            "Января",
            "Февраля",
            "Марта",
            "Апреля",
            "Мая",
            "Июня",
            "Июля",
            "Августа",
            "Сентября",
            "Октября",
            "Ноября",
            "Декабря"
        )
    )