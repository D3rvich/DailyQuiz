package ru.d3rvich.ui.components

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import ru.d3rvich.ui.R
import ru.d3rvich.ui.theme.OnSurfaceContainerLight
import ru.d3rvich.ui.theme.FilledStarColor

@Composable
fun DailyQuizStarIcon(isActive: Boolean, modifier: Modifier = Modifier) {
    val tintColor = if (isActive) {
        FilledStarColor
    } else {
        OnSurfaceContainerLight
    }
    Icon(
        painterResource(R.drawable.ic_star),
        modifier = modifier,
        contentDescription = stringResource(R.string.star_icon),
        tint = tintColor
    )
}