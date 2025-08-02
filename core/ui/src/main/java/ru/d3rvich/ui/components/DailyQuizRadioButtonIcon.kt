package ru.d3rvich.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import ru.d3rvich.ui.R

@Composable
fun DailyQuizRadioButtonIcon(isSelected: Boolean, modifier: Modifier = Modifier) {
    AnimatedContent(isSelected, modifier = modifier) {
        if (it) {
            Icon(
                painterResource(R.drawable.ic_selected),
                contentDescription = stringResource(R.string.selected_item_icon),
                tint = Color.Unspecified
            )
        } else {
            Icon(
                painterResource(R.drawable.ic_unselected),
                contentDescription = stringResource(R.string.unselected_item_icon),
                tint = Color.Unspecified
            )
        }
    }
}