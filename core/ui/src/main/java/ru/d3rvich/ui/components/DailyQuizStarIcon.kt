package ru.d3rvich.ui.components

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import ru.d3rvich.ui.R
import ru.d3rvich.ui.theme.Grey75
import ru.d3rvich.ui.theme.Yellow80

@Composable
fun DailyQuizStarIcon(isActive: Boolean, modifier: Modifier = Modifier) {
    val tintColor = if (isActive) {
        Yellow80
    } else {
        Grey75
    }
    Icon(
        painterResource(R.drawable.ic_star),
        modifier = modifier,
        contentDescription = stringResource(R.string.star_icon),
        tint = tintColor
    )
}