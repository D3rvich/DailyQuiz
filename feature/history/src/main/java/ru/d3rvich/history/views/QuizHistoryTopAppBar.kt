package ru.d3rvich.history.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
        title = { Text(stringResource(R.string.history)) },
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
                    painterResource(R.drawable.home_24px),
                    contentDescription = stringResource(R.string.navigate_home)
                )
            }
        },
        actions = {
            Actions(selectedSort, onSortChange)
        },
        expandedHeight = expandedHeight,
        colors = CollapsingTopAppBarDefaults.colors.copy(
            containerColor = MaterialTheme.colorScheme.background,
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
        SortingOptions(selectedSort = selectedSort, onSortChange = onSortChange)
    }
}

@Composable
private fun ColumnScope.SortingOptions(
    selectedSort: SortBy,
    onSortChange: (selectedSort: SortBy) -> Unit,
) {
    val selectedSortByEnum = selectedSort.asSortByEnum()
    SortByEnum.entries.forEach { item ->
        val isSelected = selectedSortByEnum == item
        DropdownMenuItem(
            text = stringResource(item.labelTextId),
            isSelected = isSelected,
            onClick = { onSortChange(item.toDomainSortBy()) })
    }
    HorizontalDivider(modifier = Modifier.padding(vertical = 2.dp))
    selectedSortByEnum.AscendingOptions(byAscending = selectedSort.byAscending) { byAscending ->
        onSortChange(selectedSortByEnum.toDomainSortBy(byAscending))
    }
}

@Composable
context(scope: ColumnScope)
private fun SortByEnum.AscendingOptions(
    byAscending: Boolean,
    onAscendingChange: (byAscending: Boolean) -> Unit,
) {
    when (this) {
        SortByEnum.Name -> {
            AscendingOptionTemplate(
                text = { if (it) stringResource(R.string.AToZ) else stringResource(R.string.ZToA) },
                byAscending = byAscending,
                onAscendingChange = onAscendingChange
            )
        }

        SortByEnum.PassedTime -> {
            AscendingOptionTemplate(
                text = { if (it) stringResource(R.string.new_to_old) else stringResource(R.string.old_to_new) },
                byAscending = byAscending,
                onAscendingChange = onAscendingChange
            )
        }

        SortByEnum.CorrectAnswers -> {
            AscendingOptionTemplate(
                text = { if (it) stringResource(R.string.worst_first) else stringResource(R.string.best_first) },
                isReversed = true,
                byAscending = byAscending,
                onAscendingChange = onAscendingChange
            )
        }
    }
}

@Composable
context(scope: ColumnScope)
private fun AscendingOptionTemplate(
    byAscending: Boolean,
    onAscendingChange: (byAscending: Boolean) -> Unit,
    text: @Composable (option: Boolean) -> String,
    modifier: Modifier = Modifier,
    isReversed: Boolean = false
) {
    listOf(true, false).let {
        if (isReversed) it.reversed() else it
    }.forEach {
        scope.apply {
            DropdownMenuItem(
                modifier = modifier,
                text = text(it),
                isSelected = byAscending == it,
                onClick = { onAscendingChange(it) })
        }
    }
}

@Composable
private fun ColumnScope.DropdownMenuItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MenuDefaults.itemColors().copy(
        textColor = if (isSelected) MaterialTheme.colorScheme.primary else MenuDefaults.itemColors().textColor,
        trailingIconColor = if (isSelected) MaterialTheme.colorScheme.primary else MenuDefaults.itemColors().trailingIconColor
    )
    DropdownMenuItem(
        modifier = modifier,
        colors = colors,
        text = {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        onClick = onClick,
        leadingIcon = {
            AnimatedVisibility(visible = isSelected) {
                Icon(
                    painterResource(R.drawable.check_24px),
                    contentDescription = stringResource(R.string.selected),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun QuizHistoryTopAppBarPreview() {
    DailyQuizTheme {
        QuizHistoryTopAppBar(
            SortBy.Name(true),
            TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
            {},
            {})
    }
}

@Preview(showBackground = true)
@Composable
private fun SortingOptionsPreview() {
    DailyQuizTheme {
        Column {
            SortingOptions(SortBy.Name(true)) { }
        }
    }
}