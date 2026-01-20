package ru.d3rvich.quiz.impl.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.d3rvich.quiz.impl.R
import ru.d3rvich.ui.R as UiR
import ru.d3rvich.ui.components.DailyQuizLogo

@Composable
internal fun TopBar(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    windowInsets: WindowInsets = WindowInsets.safeDrawing.only(
        WindowInsetsSides.Top + WindowInsetsSides.Horizontal
    )
) {
    Box(
        modifier
            .windowInsetsPadding(windowInsets)
            .padding(bottom = 32.dp, top = 12.dp)
            .fillMaxWidth()
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 12.dp)
        ) {
            Icon(
                painterResource(UiR.drawable.arrow_back_24px),
                contentDescription = stringResource(R.string.navigate_back)
            )
        }
        DailyQuizLogo(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .height(40.dp)
        )
    }
}