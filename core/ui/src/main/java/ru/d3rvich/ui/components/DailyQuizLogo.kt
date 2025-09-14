package ru.d3rvich.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.d3rvich.ui.R
import ru.d3rvich.ui.theme.DailyQuizTheme

@Composable
fun DailyQuizLogo(modifier: Modifier = Modifier) {
    Icon(
        painterResource(R.drawable.logo),
        contentDescription = stringResource(R.string.logo),
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
private fun DailyQuizLogoLightPreview() {
    DailyQuizTheme(darkTheme = false) {
        DailyQuizLogo(modifier = Modifier.padding(20.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun DailyQuizLogoDarkPreview() {
    DailyQuizTheme(darkTheme = true) {
        DailyQuizLogo(modifier = Modifier.padding(20.dp))
    }
}