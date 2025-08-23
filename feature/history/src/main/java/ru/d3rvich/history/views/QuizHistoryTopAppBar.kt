package ru.d3rvich.history.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.compose.material3.MenuDefaults
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
import androidx.compose.ui.platform.LocalDensity
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
import ru.d3rvich.history.model.SortByEnum
import ru.d3rvich.history.model.asSortByEnum
import ru.d3rvich.history.model.toDomainSortBy
import ru.d3rvich.ui.components.appbar.CollapsingTopAppBar
import ru.d3rvich.ui.components.appbar.CollapsingTopAppBarDefaults
import ru.d3rvich.ui.theme.DailyQuizTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun QuizHistoryTopAppBar(
    selectedSort: SortBy,
    scrollBehavior: TopAppBarScrollBehavior,
    onSortChange: (selectedSort: SortBy) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val expandedHeight = 200.dp
    CollapsingTopAppBar(
        modifier = modifier,
        title = { Text(stringResource(R.string.history), color = Color.White) },
        scrollBehavior = scrollBehavior,
        expandedContent = {
            val heightOffset =
                LocalDensity.current.run { (scrollBehavior.state.heightOffset * 0.6f).toDp() }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(expandedHeight)
                    .offset(y = heightOffset),
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
            Actions(selectedSort, onSortChange)
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
    onSortChange: (selectedSort: SortBy) -> Unit,
    modifier: Modifier = Modifier
) {
    var showMenu by rememberSaveable { mutableStateOf(false) }
    IconButton({ showMenu = !showMenu }, modifier = modifier) {
        Icon(painterResource(R.drawable.outline_sort_24), stringResource(R.string.open_sorting))
    }
    DropdownMenu(showMenu, { showMenu = false }) {
        SortByEnum.entries.forEach { item ->
            val isSelected = selectedSort.asSortByEnum() == item
            SortingOptionsItem(
                item = item,
                byAscending = selectedSort.byAscending,
                isSelected = isSelected,
                onSortChange = { sortByEnum ->
                    onSortChange(
                        sortByEnum.toDomainSortBy(
                            if (isSelected) {
                                !selectedSort.byAscending
                            } else {
                                selectedSort.byAscending
                            }
                        )
                    )
                })
        }
    }
}

@Composable
private fun SortingOptionsItem(
    item: SortByEnum,
    byAscending: Boolean,
    isSelected: Boolean,
    onSortChange: (SortByEnum) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedColor = MaterialTheme.colorScheme.primary
    val itemColors = if (isSelected) {
        MenuDefaults.itemColors().copy(
            textColor = selectedColor,
            trailingIconColor = selectedColor
        )
    } else {
        MenuDefaults.itemColors()
    }
    DropdownMenuItem(
        modifier = modifier,
        colors = itemColors,
        text = {
            Text(
                stringResource(item.labelTextId),
                style = MaterialTheme.typography.bodyLarge
            )
        },
        onClick = { onSortChange(item) },
        trailingIcon = {
            AnimatedVisibility(isSelected) {
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

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun QuizHistoryTopAppBarPreview() {
    DailyQuizTheme {
        QuizHistoryTopAppBar(
            SortBy.Default(true),
            TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
            {},
            {})
    }
}