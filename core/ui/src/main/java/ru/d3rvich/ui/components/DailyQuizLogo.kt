package ru.d3rvich.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ru.d3rvich.ui.R
import ru.d3rvich.ui.theme.DailyQuizTheme

@Composable
fun DailyQuizLogo(modifier: Modifier = Modifier) {
    Icon(
        painterResource(R.drawable.logo),
        contentDescription = stringResource(R.string.dailyquiz_logo),
        modifier = modifier,
        tint = Color.White
    )
}

@Preview(showBackground = true)
@Composable
private fun DailyQuizLogoPreview() {
    DailyQuizTheme {
        Surface(color = Color.Blue) {
            DailyQuizLogo()
        }
    }
}