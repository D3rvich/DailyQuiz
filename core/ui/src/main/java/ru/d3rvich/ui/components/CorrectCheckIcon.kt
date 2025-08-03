package ru.d3rvich.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import ru.d3rvich.ui.R

@Composable
fun CorrectCheckIcon(isCorrect: Boolean, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        if (isCorrect) {
            Icon(
                Icons.Default.CheckCircle,
                contentDescription = stringResource(R.string.correct_answer),
                tint = Color(0xFF00AE3A)
            )
        } else {
            Icon(
                Icons.Default.Clear,
                contentDescription = stringResource(R.string.wrong_answer),
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color(0xFFE70000)),
                tint = Color.White
            )
        }
    }
}