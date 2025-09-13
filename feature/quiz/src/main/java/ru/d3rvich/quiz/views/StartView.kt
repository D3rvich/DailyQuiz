package ru.d3rvich.quiz.views

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import ru.d3rvich.quiz.R
import ru.d3rvich.ui.components.DailyQuizButton
import ru.d3rvich.ui.components.DailyQuizLogo
import ru.d3rvich.ui.theme.DailyQuizTheme

@Composable
internal fun StartView(
    isLoading: Boolean,
    onStartClick: () -> Unit,
    onHistoryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ConstraintLayout(modifier = modifier.fillMaxSize()) {
        val (historyButton, main) = createRefs()
        AnimatedVisibility(
            !isLoading, modifier = Modifier.constrainAs(historyButton) {
                top.linkTo(parent.top)
                bottom.linkTo(main.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            HistoryButton(onHistoryClick)
        }
        MainContent(
            isLoading = isLoading,
            onStartClick = onStartClick,
            modifier = Modifier.constrainAs(main) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            })
    }
}

@Composable
private fun HistoryButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        modifier = modifier,
    ) {
        Text(stringResource(R.string.history))
        Icon(
            painterResource(R.drawable.ic_outline_history_2_24),
            contentDescription = stringResource(R.string.history),
            modifier = Modifier
                .padding(start = 16.dp)
                .padding(vertical = 8.dp)
        )
    }
}

@Composable
private fun MainContent(
    isLoading: Boolean,
    onStartClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DailyQuizLogo()
        Spacer(Modifier.height(40.dp))
        Box(
            Modifier
                .fillMaxWidth()
                .height(220.dp), contentAlignment = Alignment.Center
        ) {
            AnimatedContent(
                isLoading,
                contentAlignment = Alignment.Center,
                transitionSpec = { fadeIn() togetherWith fadeOut() }) { value ->
                if (value) {
                    CircularProgressIndicator(
                        color = Color(0xFFBCB7FF),
                        trackColor = Color.White,
                    )
                } else {
                    Card(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp),
                        shape = RoundedCornerShape(40.dp)
                    ) {
                        Column(
                            verticalArrangement = Arrangement.SpaceAround,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(20.dp)
                        ) {
                            Text(
                                stringResource(R.string.welcome_message),
                                style = MaterialTheme.typography.displaySmall,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.fillMaxWidth()
                            )
                            DailyQuizButton(
                                stringResource(R.string.start_quiz),
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                onClick = onStartClick
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, apiLevel = 35)
@Composable
private fun StartPreview() {
    DailyQuizTheme {
        StartView(false, {}, {})
    }
}

@Preview(showBackground = true, apiLevel = 35)
@Composable
private fun StartLoadingPreview() {
    DailyQuizTheme {
        StartView(true, {}, {})
    }
}