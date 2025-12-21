package ru.d3rvich.history.impl.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import ru.d3rvich.history.impl.R
import ru.d3rvich.ui.components.DailyQuizButton
import ru.d3rvich.ui.components.DailyQuizLogo
import ru.d3rvich.ui.theme.DailyQuizTheme
import ru.d3rvich.ui.R as UiR

@Composable
internal fun EmptyHistoryView(
    onStartQuizClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isLandscape = !(currentWindowAdaptiveInfo().windowSizeClass
        .isHeightAtLeastBreakpoint(WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND))
    Scaffold(
        modifier = modifier,
        topBar = {
            if (!isLandscape) {
                TopBar(
                    onBackClick = onBackClick,
                    modifier = Modifier.windowInsetsPadding(
                        WindowInsets.safeDrawing.only(WindowInsetsSides.Top)
                    )
                )
            }
        },
        bottomBar = {
            if (!isLandscape) {
                Box(
                    modifier = Modifier
                        .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom))
                        .padding(bottom = 20.dp)
                        .fillMaxWidth()
                        .height(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    DailyQuizLogo()
                }
            }
        }
    ) { innerPadding ->
        EmptyHistoryCard(
            onStartQuizClick = onStartQuizClick,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun TopBar(onBackClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(modifier.fillMaxWidth()) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 52.dp)
        ) {
            Icon(
                painterResource(UiR.drawable.arrow_back_24px),
                contentDescription = stringResource(R.string.navigate_back)
            )
        }
        Text(
            text = stringResource(R.string.history),
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            letterSpacing = TextUnit(0f, TextUnitType.Sp),
            modifier = Modifier
                .padding(top = 52.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun EmptyHistoryCard(onStartQuizClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .widthIn(max = 600.dp)
                .padding(horizontal = 20.dp),
            shape = RoundedCornerShape(40.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(40.dp),
                modifier = Modifier.padding(28.dp)
            ) {
                Text(
                    stringResource(R.string.no_quiz_message),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.headlineSmall
                )
                DailyQuizButton(
                    text = stringResource(R.string.start_quiz),
                    onClick = onStartQuizClick
                )
            }
        }
    }
}

@PreviewScreenSizes
@Composable
private fun EmptyHistoryPreview() {
    DailyQuizTheme {
        EmptyHistoryView({}, {})
    }
}