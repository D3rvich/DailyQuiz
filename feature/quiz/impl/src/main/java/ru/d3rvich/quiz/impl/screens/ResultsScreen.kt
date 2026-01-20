package ru.d3rvich.quiz.impl.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import ru.d3rvich.quiz.impl.R
import ru.d3rvich.ui.components.QuizResultCard
import ru.d3rvich.ui.theme.DailyQuizTheme

@Composable
internal fun ResultsScreen(
    correctAnswers: Int,
    totalQuestions: Int,
    navigateToSource: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isLandscape = !(currentWindowAdaptiveInfo().windowSizeClass
        .isHeightAtLeastBreakpoint(WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND))
    Column(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!isLandscape) {
            Text(
                text = stringResource(R.string.results),
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                letterSpacing = TextUnit(0f, TextUnitType.Sp),
                modifier = Modifier.padding(40.dp)
            )
        }
        QuizResultCard(
            correctAnswers = correctAnswers,
            totalQuestions = totalQuestions,
            onRetryClick = navigateToSource,
            modifier = Modifier.widthIn(max = 600.dp)
        )
    }
}

@PreviewScreenSizes
@Composable
private fun ResultsPreview() {
    DailyQuizTheme {
        ResultsScreen(correctAnswers = 4, totalQuestions = 5, {})
    }
}