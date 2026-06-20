package com.sangeeta.chronomind.ui.onboarding.screens

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import com.sangeeta.chronomind.ui.onboarding.OnboardingScaffold
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.sangeeta.chronomind.R
import kotlinx.coroutines.delay


@Composable
fun MeetAuraScreen(
    onLetBegin: () -> Unit,
    modifier: Modifier = Modifier
) {
    var visible by remember { mutableStateOf(false) }
    var isTypingDone by remember { mutableStateOf(false) }

    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(600),
        label = "meetAlpha"
    )

    LaunchedEffect(Unit) { visible = true }

    val infiniteAnim = rememberInfiniteTransition(label = "botFloat")

    val botOffsetY by infiniteAnim.animateFloat(
        initialValue = 0f,
        targetValue = -12f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "botY"
    )

    OnboardingScaffold(
        buttonText = "Let's Begin",
        buttonEnabled = isTypingDone,
        onButtonClick = onLetBegin,
        showProgress = false,
        modifier = modifier
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .graphicsLayer(alpha = alpha),
            contentAlignment = Alignment.Center
        ) {
            AuraIntroHero(
                botOffsetY = botOffsetY.dp,
                onTypingComplete = { isTypingDone = true }
            )
        }
    }
}





@Composable
fun AuraIntroHero(
    modifier: Modifier = Modifier,
    botOffsetY: Dp = 0.dp,
    onTypingComplete: () -> Unit = {}
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 360.dp)
                .drawBehind {
                    val w = size.width
                    val h = size.height
                    val r = 38f
                    val arrowX = w / 2f

                    val path = Path().apply {
                        moveTo(r, 0f)
                        lineTo(w - r, 0f)
                        quadraticTo(w, 0f, w, r)
                        lineTo(w, h - r)
                        quadraticTo(w, h, w - r, h)

                        lineTo(arrowX + 20f, h)
                        lineTo(arrowX, h + 20f)
                        lineTo(arrowX - 20f, h)

                        lineTo(r, h)
                        quadraticTo(0f, h, 0f, h - r)
                        lineTo(0f, r)
                        quadraticTo(0f, 0f, r, 0f)
                        close()
                    }

                    drawPath(path = path, color = Color(0xFF1E1E1E))
                }
                .padding(horizontal = 12.dp, vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            TypewriterText(
                text = "Hi, I'm Aura.\nI'll help you track time and stay consistent.\nA few quick questions to set things up.",
                textAlign = TextAlign.Start,
                onTypingComplete = onTypingComplete
            )
        }

                  Image(
                      painter = painterResource(id = R.drawable.bot_img),
                      contentDescription = "Aura Bot",
                      modifier = Modifier
                          .size(280.dp)
                          .offset(y = botOffsetY),
                      contentScale = ContentScale.Fit
                  )

    }
}

 @Composable
fun TypewriterText(
    text: String,
    modifier: Modifier = Modifier,
    typingDelay: Long = 28L,
    startDelay: Long = 400L,
    color: Color = Color.White.copy(alpha = 0.95f),
    fontSize: TextUnit = 15.sp,
    textAlign: TextAlign = TextAlign.Center,
    lineHeight: TextUnit = 22.sp,
    onTypingComplete: () -> Unit = {}
) {
    var visibleText by remember(text) { mutableStateOf("") }

    LaunchedEffect(text) {
        visibleText = ""
        delay(startDelay)
        text.forEach { char ->
            visibleText += char
            delay(typingDelay)
        }
        onTypingComplete()
    }

    Box(modifier = modifier) {
        Text(
            text = text,
            color = Color.Transparent,
            fontSize = fontSize,
            lineHeight = lineHeight,
            textAlign = textAlign
        )

        Text(
            text = visibleText,
            color = color,
            fontSize = fontSize,
            lineHeight = lineHeight,
            textAlign = textAlign
        )
    }
}