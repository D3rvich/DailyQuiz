package ru.d3rvich.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import ru.d3rvich.ui.R
import ru.d3rvich.ui.theme.Green50
import ru.d3rvich.ui.theme.Red50

@Composable
fun CorrectCheckIcon(isCorrect: Boolean, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        if (isCorrect) {
            Icon(
                painterResource(R.drawable.check_circle_24px),
                contentDescription = stringResource(R.string.correct_answer),
                tint = Green50
            )
        } else {
            Icon(
                painterResource(R.drawable.cancel_24px),
                contentDescription = stringResource(R.string.wrong_answer),
                tint = Red50
            )
        }
    }
}