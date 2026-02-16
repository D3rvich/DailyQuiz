package ru.d3rvich.history.impl.views

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.layout.LazyLayoutCacheWindow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import ru.d3rvich.domain.model.Category
import ru.d3rvich.domain.model.Difficulty
import ru.d3rvich.history.impl.R
import ru.d3rvich.history.impl.utils.RUSSIAN_FULL
import ru.d3rvich.navigation.LocalSharedTransitionScope
import ru.d3rvich.ui.components.DailyQuizStarIcon
import ru.d3rvich.ui.extensions.stringRes
import ru.d3rvich.ui.model.AnswerUiModel
import ru.d3rvich.ui.model.QuestionUiModel
import ru.d3rvich.ui.model.QuizResultUiModel
import ru.d3rvich.ui.theme.DailyQuizTheme
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3AdaptiveApi::class)
@Composable
internal fun QuizHistoryView(
    quizList: ImmutableList<QuizResultUiModel>,
    onQuizCLick: (quizResult: QuizResultUiModel) -> Unit,
    onRemoveQuiz: (quizResult: QuizResultUiModel) -> Unit,
    onSelectionChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedItem: QuizResultUiModel? by remember { mutableStateOf(null) }
    val animateColor = animateColor(selectedItem != null)
    var showDialog by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(selectedItem) {
        onSelectionChange(selectedItem != null)
    }
    val cacheWindow = remember { LazyLayoutCacheWindow(0.5f, 0.5f) }
    val state = rememberLazyListState(cacheWindow = cacheWindow)
    LazyColumn(
        state = state,
        modifier = modifier
            .drawBehind { drawRect(animateColor) }
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom + WindowInsetsSides.Start)
            .asPaddingValues()
    ) {
        items(quizList, key = { it.id }) { item ->
            val animateItemColor = animateColor(selectedItem != null && selectedItem != item)
            QuizResultItem(
                modifier = Modifier
                    .animateItem()
                    .drawWithContent {
                        drawContent()
                        if (selectedItem != null) {
                            drawRect(animateItemColor)
                        }
                    },
                quizResult = item,
                onQuizCLick = { onQuizCLick(item) },
                onRemoveQuiz = {
                    showDialog = true
                    onRemoveQuiz(item)
                },
                onItemSelectChange = { selected ->
                    selectedItem = selected
                },
            )
        }
    }

    if (showDialog) {
        RemoveDialog({ showDialog = false })
    }
}

@Composable
internal fun animateColor(target: Boolean): Color =
    animateColorAsState(
        if (target) {
            Color.Black.copy(0.3f)
        } else {
            Color.Transparent
        }
    ).value

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun QuizResultItem(
    quizResult: QuizResultUiModel,
    onItemSelectChange: (QuizResultUiModel?) -> Unit,
    onQuizCLick: () -> Unit,
    onRemoveQuiz: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showMenu by remember { mutableStateOf(false) }
    var pressOffset by remember { mutableStateOf(DpOffset.Zero) }
    var itemHeight by remember { mutableStateOf(0.dp) }
    val interactionSource = remember { MutableInteractionSource() }
    val listDetailStrategy = rememberListDetailSceneStrategy<NavKey>()
    val sharedModifier =
        if (listDetailStrategy.directive.maxHorizontalPartitions == 1) {
            with(LocalSharedTransitionScope.current) {
                Modifier.sharedBounds(
                    rememberSharedContentState(key = "quiz:${quizResult.id}"),
                    animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                    clipInOverlayDuringTransition = OverlayClip(RoundedCornerShape(40.dp)),
                    boundsTransform = { _, _ ->
                        spring(stiffness = Spring.StiffnessLow)
                    }
                )
            }
        } else {
            Modifier
        }
    val density = LocalDensity.current
    val screenHeightPx = LocalWindowInfo.current.containerSize.height
    var anchorTopY by remember { mutableFloatStateOf(0f) }
    var anchorHeight by remember { mutableIntStateOf(0) }
    val estimatedMenuHeightPx = remember { with(density) { 100.dp.toPx() } }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .then(sharedModifier)
            .clip(RoundedCornerShape(40.dp))
            .then(modifier)
            .onGloballyPositioned {
                itemHeight = with(density) { it.size.height.toDp() }
                anchorTopY = it.positionInWindow().y
                anchorHeight = it.size.height
            }
    ) {
        QuizResultItemContent(
            quizResult = quizResult,
            modifier = Modifier
                .indication(interactionSource, LocalIndication.current)
                .pointerInput(quizResult) {
                    detectTapGestures(
                        onLongPress = {
                            onItemSelectChange(quizResult)
                            showMenu = true
                            pressOffset = DpOffset(it.x.toDp(), it.y.toDp())
                        },
                        onTap = {
                            onQuizCLick()
                        },
                        onPress = {
                            val press = PressInteraction.Press(it)
                            interactionSource.emit(press)
                            tryAwaitRelease()
                            interactionSource.emit(PressInteraction.Release(press))
                        }
                    )
                })
        val offsetY = if (screenHeightPx - (anchorHeight + anchorTopY) > estimatedMenuHeightPx) {
            pressOffset.y - itemHeight
        } else {
            0.dp
        }
        RemoveItemMenu(
            isVisible = showMenu,
            onCloseRequest = {
                onItemSelectChange(null)
                showMenu = false
            },
            onRemove = onRemoveQuiz,
            offset = pressOffset.copy(y = offsetY)
        )
    }
}

@Composable
private fun QuizResultItemContent(quizResult: QuizResultUiModel, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Quiz ${quizResult.id}",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            val correctAnswers = remember { quizResult.correctAnswers }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                repeat(quizResult.questions.size) {
                    DailyQuizStarIcon(it + 1 <= correctAnswers, modifier = Modifier.size(18.dp))
                }
            }
        }
        TimeRow(quizResult.passedTime)
        Column(
            Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(2.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                stringResource(
                    R.string.category_placement,
                    stringResource(quizResult.generalCategory.stringRes)
                ),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
            Text(
                stringResource(
                    R.string.difficult_placement,
                    stringResource(quizResult.difficulty.stringRes)
                ),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun TimeRow(dateTime: LocalDateTime, modifier: Modifier = Modifier) {
    Row(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        val languageTag = LocalConfiguration.current.locales[0].toLanguageTag()
        val dateFormat = remember(languageTag) {
            LocalDateTime.Format {
                day(Padding.NONE)
                char(' ')
                monthName(
                    when (languageTag) {
                        "ru-RU" -> MonthNames.RUSSIAN_FULL
                        else -> MonthNames.ENGLISH_FULL
                    }
                )
            }
        }
        val formatDate = dateTime.format(dateFormat)
        Text(formatDate, style = MaterialTheme.typography.bodyMedium)
        val timeFormat = remember {
            LocalDateTime.Format {
                hour()
                char(':')
                minute()
            }
        }
        val formatTime = dateTime.format(timeFormat)
        Text(formatTime, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun RemoveItemMenu(
    isVisible: Boolean,
    onCloseRequest: () -> Unit,
    onRemove: () -> Unit,
    offset: DpOffset,
    modifier: Modifier = Modifier
) {
    DropdownMenu(
        isVisible,
        onDismissRequest = onCloseRequest,
        modifier = modifier,
        offset = offset,
    ) {
        Row(
            modifier = Modifier
                .width(200.dp)
                .clickable(onClick = {
                    onRemove()
                    onCloseRequest()
                }),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painterResource(R.drawable.ic_delete),
                contentDescription = null,
                modifier = Modifier
                    .padding(8.dp)
                    .padding(horizontal = 12.dp)
            )
            Text(stringResource(R.string.remove))
        }
    }
}

@OptIn(ExperimentalTime::class)
@PreviewLightDark
@Composable
private fun QuizHistoryNonDynamicPreview() {
    DailyQuizTheme(dynamicColor = false) {
        val answers = List(4) {
            AnswerUiModel("it?", it == 1)
        }.toPersistentList()
        val questions = List(5) {
            QuestionUiModel("", "it?", answers, it % 4)
        }.toPersistentList()
        val list = List(8) {
            QuizResultUiModel(
                Category.entries[it],
                Difficulty.entries[it % 4],
                Clock.System.now().toLocalDateTime(TimeZone.UTC),
                questions,
                1,
                it.toLong()
            )
        }
        QuizHistoryView(list.toPersistentList(), {}, {}, {})
    }
}

@OptIn(ExperimentalTime::class)
@PreviewDynamicColors
@PreviewLightDark
@Composable
private fun QuizHistoryViewPreview() {
    DailyQuizTheme {
        val answers = List(4) {
            AnswerUiModel("it?", it == 1)
        }.toPersistentList()
        val questions = List(5) {
            QuestionUiModel("", "it?", answers, it % 4)
        }.toPersistentList()
        val list = List(8) {
            QuizResultUiModel(
                Category.entries[it],
                Difficulty.entries[it % 4],
                Clock.System.now().toLocalDateTime(TimeZone.UTC),
                questions,
                1,
                it.toLong()
            )
        }
        QuizHistoryView(list.toPersistentList(), {}, {}, {})
    }
}