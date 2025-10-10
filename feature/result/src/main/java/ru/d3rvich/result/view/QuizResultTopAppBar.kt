package ru.d3rvich.result.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import ru.d3rvich.domain.model.Category
import ru.d3rvich.domain.model.Difficulty
import ru.d3rvich.result.R
import ru.d3rvich.ui.R as UiR
import ru.d3rvich.ui.components.appbar.CollapsingTopAppBar
import ru.d3rvich.ui.components.appbar.CollapsingTopAppBarDefaults
import ru.d3rvich.ui.extensions.stringRes
import ru.d3rvich.ui.theme.DailyQuizTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun QuizResultTopAppBar(
    category: String,
    difficulty: String,
    scrollBehavior: TopAppBarScrollBehavior,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val expandedHeight = 260.dp
    CollapsingTopAppBar(
        scrollBehavior = scrollBehavior,
        title = { Text(text = stringResource(R.string.results)) },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    painterResource(UiR.drawable.arrow_back_24px),
                    contentDescription = stringResource(R.string.navigate_back)
                )
            }
        },
        expandedContent = {
            Column(
                Modifier
                    .fillMaxWidth()
                    .height(expandedHeight),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = stringResource(R.string.results),
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = TextUnit(0f, TextUnitType.Sp),
                    modifier = modifier.padding(bottom = 12.dp)
                )
                Text(
                    stringResource(
                        R.string.category_placement,
                        category
                    )
                )
                Text(
                    stringResource(
                        R.string.difficult_placement,
                        difficulty
                    )
                )
            }
        },
        expandedHeight = expandedHeight,
        colors = CollapsingTopAppBarDefaults.colors.copy(
            containerColor = MaterialTheme.colorScheme.background,
            scrolledContainerColor = MaterialTheme.colorScheme.background
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun QuizResultTopAppBarPreview() {
    DailyQuizTheme {
        QuizResultTopAppBar(
            category = stringResource(Category.AnyCategory.stringRes),
            difficulty = stringResource(Difficulty.AnyDifficulty.stringRes),
            scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
            onBackClick = {})
    }
}