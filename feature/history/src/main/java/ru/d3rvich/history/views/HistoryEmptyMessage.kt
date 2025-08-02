package ru.d3rvich.history.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.d3rvich.history.R
import ru.d3rvich.ui.components.DailyQuizButton
import ru.d3rvich.ui.theme.DailyQuizTheme

@Composable
internal fun HistoryEmptyMessage(onStartQuizClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .padding(20.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(40.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp),
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                stringResource(R.string.no_quiz_message),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            DailyQuizButton(text = stringResource(R.string.start_quiz), onClick = onStartQuizClick)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HistoryEmptyMessagePreview() {
    DailyQuizTheme {
        HistoryEmptyMessage({})
    }
}