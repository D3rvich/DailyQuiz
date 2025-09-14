package ru.d3rvich.quiz.views

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import ru.d3rvich.domain.model.Category
import ru.d3rvich.domain.model.Difficulty
import ru.d3rvich.quiz.R
import ru.d3rvich.ui.components.DailyQuizButton
import ru.d3rvich.ui.components.DailyQuizLogo
import ru.d3rvich.ui.extensions.stringRes
import ru.d3rvich.ui.theme.DailyQuizTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FiltersView(
    category: Category?,
    difficulty: Difficulty?,
    onCategoryChange: (Category) -> Unit,
    onDifficultChange: (Difficulty) -> Unit,
    onStartClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(modifier = modifier.fillMaxSize()) {
        val (topBar, filtersCard) = createRefs()
        CenterAlignedTopAppBar(
            modifier = Modifier.constrainAs(topBar) {
                top.linkTo(parent.top)
            },
            title = {
                DailyQuizLogo(
                    modifier = Modifier.height(40.dp)
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = stringResource(R.string.navigate_back)
                    )
                }
            })
        SelectorCard(
            category = category,
            difficulty = difficulty,
            onCategoryChange = onCategoryChange,
            onDifficultChange = onDifficultChange,
            onStartClick = onStartClick,
            modifier = Modifier.constrainAs(filtersCard) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            })
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
            DropdownTextField(
                selectedValue = category,
                values = Category.entries,
                text = { it?.text ?: "" },
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
                .menuAnchor(MenuAnchorType.PrimaryEditable)
                .fillMaxWidth(),
            readOnly = true,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
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

@Preview(showBackground = true, apiLevel = 35)
@Composable
private fun FiltersEmptyPreview() {
    DailyQuizTheme {
        FiltersView(null, null, {}, {}, {}, {})
    }
}

@Preview(showBackground = true, apiLevel = 35)
@Composable
private fun FiltersPreview() {
    DailyQuizTheme {
        FiltersView(Category.AnyCategory, Difficulty.AnyDifficulty, {}, {}, {}, {})
    }
}