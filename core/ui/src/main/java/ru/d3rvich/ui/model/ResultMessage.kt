package ru.d3rvich.ui.model

import ru.d3rvich.ui.R

enum class ResultMessage(val headlineResId: Int, val subtitleResId: Int) {
    ZERO(R.string.result_hl_zero, R.string.result_st_zero),
    ONE(R.string.result_hl_one, R.string.result_st_one),
    TWO(R.string.result_hl_two, R.string.result_st_two),
    THREE(R.string.result_hl_three, R.string.result_st_three),
    FOUR(R.string.result_hl_four, R.string.result_st_four),
    FIVE(R.string.result_hl_five, R.string.result_st_five)
}