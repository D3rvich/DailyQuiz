package ru.d3rvich.history.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import ru.d3rvich.domain.model.SortBy
import ru.d3rvich.history.R
import ru.d3rvich.ui.components.appbar.CollapsingTopAppBar
import ru.d3rvich.ui.components.appbar.CollapsingTopAppBarDefaults
import ru.d3rvich.ui.theme.DailyQuizTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun QuizHistoryTopAppBar(
    selectedSort: SortBy,
    byAscending: Boolean,
    scrollBehavior: TopAppBarScrollBehavior,
    onSortChange: (selectedSort: SortBy, byAscending: Boolean) -> Unit,
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
        actions = {
            Actions(selectedSort, byAscending, onSortChange)
        },
        expandedHeight = expandedHeight,
        colors = CollapsingTopAppBarDefaults.colors.copy(
            containerColor = MaterialTheme.colorScheme.background,
            actionsContentColor = Color.White,
            navigationIconContentColor = Color.White,
            scrolledContainerColor = MaterialTheme.colorScheme.background
        )
    )
}

@Composable
private fun Actions(
    selectedSort: SortBy,
    byAscending: Boolean,
    onSortChange: (selectedSort: SortBy, byAscending: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var showMenu by rememberSaveable { mutableStateOf(false) }
    Box(modifier = modifier) {
        IconButton({ showMenu = !showMenu }) {
            Icon(painterResource(R.drawable.outline_sort_24), stringResource(R.string.open_sorting))
        }
        DropdownMenu(showMenu, { showMenu = false }) {
            SortBy.entries.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item.name, style = MaterialTheme.typography.bodyLarge) },
                    onClick = {
                        onSortChange(item, if (selectedSort == item) !byAscending else byAscending)
                    },
                    trailingIcon = {
                        AnimatedVisibility(selectedSort == item) {
                            if (byAscending) {
                                Icon(
                                    Icons.Default.KeyboardArrowDown,
                                    stringResource(R.string.by_ascending)
                                )
                            } else {
                                Icon(
                                    Icons.Default.KeyboardArrowUp,
                                    stringResource(R.string.by_descending)
                                )
                            }
                        }
                    })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun QuizHistoryTopAppBarPreview() {
    DailyQuizTheme {
        QuizHistoryTopAppBar(
            SortBy.Default,
            true,
            TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
            { _, _ -> },
            {})
    }
}