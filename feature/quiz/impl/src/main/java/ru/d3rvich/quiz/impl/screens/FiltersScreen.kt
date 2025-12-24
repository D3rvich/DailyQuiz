package ru.d3rvich.quiz.impl.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import ru.d3rvich.domain.model.Category
import ru.d3rvich.domain.model.Difficulty
import ru.d3rvich.quiz.impl.R
import ru.d3rvich.ui.components.DailyQuizButton
import ru.d3rvich.ui.components.DailyQuizLogo
import ru.d3rvich.ui.extensions.stringRes
import ru.d3rvich.ui.theme.DailyQuizTheme
import ru.d3rvich.ui.R as UiR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FiltersScreen(
    category: Category?,
    difficulty: Difficulty?,
    onCategoryChange: (Category) -> Unit,
    onDifficultChange: (Difficulty) -> Unit,
    onStartQuiz: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(modifier = modifier.fillMaxSize()) {
        val (topBar, filtersCard) = createRefs()
        CenterAlignedTopAppBar(
            modifier = Modifier.constrainAs(topBar) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            title = {
                DailyQuizLogo(
                    modifier = Modifier.height(40.dp)
                )
            },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        painterResource(UiR.drawable.arrow_back_24px),
                        contentDescription = stringResource(R.string.navigate_back)
                    )
                }
            })
        SelectorCard(
            category = category,
            difficulty = difficulty,
            onCategoryChange = onCategoryChange,
            onDifficultChange = onDifficultChange,
            onStartClick = onStartQuiz,
            modifier = Modifier.constrainAs(filtersCard) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            }.widthIn(max = 600.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SelectorCard(
    category: Category?,
    difficulty: Difficulty?,
    onCategoryChange: (Category) -> Unit,
    onDifficultChange: (Difficulty) -> Unit,
    onStartClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(40.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                stringResource(R.string.almost_ready),
                Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
            Text(
                stringResource(R.string.filters_message),
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                textAlign = TextAlign.Center
            )
            val categories = Category.entries.sortedBy { it.name }.toMutableList()
            categories.apply {
                remove(Category.AnyCategory)
                add(0, Category.AnyCategory)
            }
            DropdownTextField(
                selectedValue = category,
                values = categories,
                text = { it?.run { stringResource(stringRes) } ?: "" },
                label = stringResource(R.string.category),
                onValueSelect = { it?.let { onCategoryChange(it) } })
            DropdownTextField(
                selectedValue = difficulty,
                values = Difficulty.entries,
                text = { it?.run { stringResource(it.stringRes) } ?: "" },
                label = stringResource(R.string.difficult),
                onValueSelect = { it?.let { onDifficultChange(it) } })
            DailyQuizButton(
                modifier = Modifier.padding(top = 32.dp),
                text = stringResource(R.string.start_quiz),
                onClick = onStartClick,
                enabled = category != null && difficulty != null
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <T> DropdownTextField(
    selectedValue: T,
    values: List<T>,
    label: String,
    text: @Composable (T) -> String,
    onValueSelect: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val rotate by animateFloatAsState(targetValue = if (expanded) -180f else 0f)
    ExposedDropdownMenuBox(
        expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            shape = RoundedCornerShape(40.dp),
            value = text(selectedValue),
            onValueChange = {},
            label = {
                Text(
                    label,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.background(Color.Transparent)
                )
            },
            modifier = Modifier
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable)
                .fillMaxWidth(),
            readOnly = true,
            trailingIcon = {
                Icon(
                    painter = painterResource(UiR.drawable.keyboard_arrow_down_24px),
                    contentDescription = null,
                    modifier = modifier.graphicsLayer(rotationZ = rotate)
                )
            },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
            )
        )
        ExposedDropdownMenu(expanded, { expanded = false }) {
            values.forEach { value ->
                DropdownMenuItem(
                    text = { Text(text(value), style = MaterialTheme.typography.bodyLarge) },
                    onClick = {
                        onValueSelect(value)
                        expanded = false
                    })
            }
        }
    }
}

@PreviewScreenSizes
@Composable
private fun FiltersEmptyPreview() {
    DailyQuizTheme {
        FiltersScreen(null, null, {}, {}, {}, {})
    }
}

@Preview(showBackground = true, apiLevel = 35)
@Composable
private fun FiltersPreview() {
    DailyQuizTheme {
        FiltersScreen(Category.AnyCategory, Difficulty.AnyDifficulty, {}, {}, {}, {})
    }
}