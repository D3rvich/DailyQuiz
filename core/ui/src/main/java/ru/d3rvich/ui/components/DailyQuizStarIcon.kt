package ru.d3rvich.ui.components

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import ru.d3rvich.ui.R

@Composable
fun DailyQuizStarIcon(isActive: Boolean, modifier: Modifier = Modifier) {
    val tintColor = if (isActive) {
        Color(0xFFFFB800)
    } else {
        Color(0xFFBABABA)
    }
    Icon(
        painterResource(R.drawable.ic_star),
        modifier = modifier,
        contentDescription = stringResource(R.string.star_icon),
        tint = tintColor
    )
}