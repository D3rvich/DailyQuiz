package ru.d3rvich.quiz.impl.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.d3rvich.quiz.impl.R
import ru.d3rvich.ui.R as UiR
import ru.d3rvich.ui.components.DailyQuizButton
import ru.d3rvich.ui.theme.DailyQuizTheme

@Composable
internal fun TimeoutMessage(onDismissRequest: () -> Unit, modifier: Modifier = Modifier) {
    AlertDialog(
        modifier = modifier,
        shape = RoundedCornerShape((40.dp)),
        title = {
            Text(
                stringResource(R.string.timeout_title),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                stringResource(R.string.timeout_message),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 20.sp),
            )
        },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            DailyQuizButton(text = stringResource(UiR.string.restart), onDismissRequest)
        })
}

@Preview(showBackground = true)
@Composable
private fun TimeoutMessagePreview() {
    DailyQuizTheme {
        Box(Modifier.fillMaxSize()) {
            TimeoutMessage({})
        }
    }
}