package ru.d3rvich.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import ru.d3rvich.ui.R

@Composable
fun DailyQuizRadioButtonIcon(isSelected: Boolean, modifier: Modifier = Modifier) {
    if (isSelected) {
        Icon(
            Icons.Default.CheckCircle,
            contentDescription = stringResource(R.string.selected_item_icon),
            tint = MaterialTheme.colorScheme.primary,
            modifier = modifier,
        )
    } else {
        Icon(
            painterResource(R.drawable.ic_outline_circle),
            contentDescription = stringResource(R.string.unselected_item_icon),
            tint = Color.Black,
            modifier = modifier,
        )
    }
}