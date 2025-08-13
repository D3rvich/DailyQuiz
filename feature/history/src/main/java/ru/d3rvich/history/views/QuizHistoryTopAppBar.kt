package ru.d3rvich.history.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import ru.d3rvich.history.R
import ru.d3rvich.ui.components.appbar.CollapsingTopAppBar
import ru.d3rvich.ui.components.appbar.CollapsingTopAppBarDefaults

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun QuizHistoryTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val expandedHeight = 200.dp
    CollapsingTopAppBar(
        modifier = modifier,
        title = { Text(stringResource(R.string.history), color = Color.White) },
        scrollBehavior = scrollBehavior,
        expandedContent = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(expandedHeight),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.history),
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = TextUnit(0f, TextUnitType.Sp),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )
            }
        },
        navigationIcon = {
            IconButton(onBackClick) {
                Icon(
                    Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = stringResource(R.string.navigate_back)
                )
            }
        },
        expandedHeight = expandedHeight,
        colors = CollapsingTopAppBarDefaults.colors.copy(
            containerColor = MaterialTheme.colorScheme.background,
            navigationIconContentColor = Color.White,
            scrolledContainerColor = MaterialTheme.colorScheme.background
        )
    )
}