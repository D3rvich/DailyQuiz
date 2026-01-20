package ru.d3rvich.history.impl.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.d3rvich.history.impl.R
import ru.d3rvich.ui.theme.DailyQuizTheme

@Composable
internal fun RemoveDialog(onDismissRequest: () -> Unit, modifier: Modifier = Modifier) {
    AlertDialog(
        shape = RoundedCornerShape(40.dp),
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        confirmButton = { TextButton(onDismissRequest) { Text(stringResource(R.string.close)) } },
        title = {
            Text(
                stringResource(R.string.attempt_removed),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                stringResource(R.string.retry_message),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 20.sp),
            )
        }
    )
}

@Preview(showBackground = true)
@PreviewLightDark
@Composable
private fun RemoveDialogPreview() {
    DailyQuizTheme {
        Box(Modifier.fillMaxSize()) {
            RemoveDialog({})
        }
    }
}