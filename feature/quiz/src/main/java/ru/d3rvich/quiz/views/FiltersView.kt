package ru.d3rvich.quiz.views

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import ru.d3rvich.domain.model.Category
import ru.d3rvich.domain.model.Difficult
import ru.d3rvich.quiz.R
import ru.d3rvich.ui.components.DailyQuizButton
import ru.d3rvich.ui.components.DailyQuizLogo
import ru.d3rvich.ui.theme.DailyQuizTheme

@Composable
internal fun FiltersView(
    category: Category?,
    difficult: Difficult?,
    onCategoryChange: (Category) -> Unit,
    onDifficultChange: (Difficult) -> Unit,
    onStartClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 32.dp)
        ) {
            IconButton(
                onClick = onBackClick, modifier = Modifier
                    .padding(start = 8.dp)
                    .align(Alignment.CenterStart)
            ) {
                Icon(
                    Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = stringResource(R.string.navigate_back)
                )
            }
            DailyQuizLogo(
                modifier = Modifier.height(40.dp)
            )
        }
        SelectorCard(category, difficult, onCategoryChange, onDifficultChange, onStartClick)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SelectorCard(
    category: Category?,
    difficult: Difficult?,
    onCategoryChange: (Category) -> Unit,
    onDifficultChange: (Difficult) -> Unit,
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
                selectedValue = difficult,
                values = Difficult.entries,
                text = { it?.text ?: "" },
                label = stringResource(R.string.difficult),
                onValueSelect = { it?.let { onDifficultChange(it) } })
            DailyQuizButton(
                modifier = Modifier.padding(top = 32.dp),
                text = stringResource(R.string.start_quiz),
                onClick = onStartClick,
                enabled = category != null && difficult != null
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
    text: (T) -> String,
    onValueSelect: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val rotate by animateFloatAsState(targetValue = if (expanded) -180f else 0f)
    val containerColor = Color(0xFFF3F3F3)
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
                    color = Color(0xFF2B0063),
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
                unfocusedContainerColor = containerColor,
                focusedContainerColor = containerColor,
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
        FiltersView(Category.AnyCategory, Difficult.AnyDifficulty, {}, {}, {}, {})
    }
}