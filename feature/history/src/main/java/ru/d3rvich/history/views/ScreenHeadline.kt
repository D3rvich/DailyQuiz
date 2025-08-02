package ru.d3rvich.history.views

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import ru.d3rvich.history.R

@Composable
internal fun ScreenHeadline(
    modifier: Modifier = Modifier,
    text: String = stringResource(R.string.history)
) {
    Text(
        text = text,
        style = MaterialTheme.typography.displaySmall,
        fontWeight = FontWeight.Bold,
        letterSpacing = TextUnit(0f, TextUnitType.Sp),
        modifier = modifier.padding(bottom = 40.dp, top = 52.dp)
    )
}