package com.example.androidpracticumcustomview.ui.theme

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

const val DEFAULT_ANIMATION_DURATION_MILLIS = 5000
const val DEFAULT_ALPHA_DURATION_MILLIS = 2000

/*
Задание:
Реализуйте необходимые компоненты;
Создайте проверку что дочерних элементов не более 2-х;
Предусмотрите обработку ошибок рендера дочерних элементов.
Задание по желанию:
Предусмотрите параметризацию длительности анимации.
 */
@Composable
fun CustomContainerCompose(
    firstChild: @Composable (() -> Unit)?,
    secondChild: @Composable (() -> Unit)?,
    animationDurationMillis: Int = DEFAULT_ANIMATION_DURATION_MILLIS,
    alphaDurationMillis: Int = DEFAULT_ALPHA_DURATION_MILLIS
) {
    // Блок создания и инициализации переменных
    var visible by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val offsetY = remember { Animatable(0f) }

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.toFloat()

    val animateBox = {
        coroutineScope.launch {
            // Анимация перемещения по оси Y
            offsetY.animateTo(
                targetValue = screenHeight.dp.value / 2.1f,
                animationSpec = tween(animationDurationMillis)
            )
        }
    }

    // Блок активации анимации при первом запуске
    LaunchedEffect(Unit) {
        visible = true
        animateBox()
    }

    // Основной контейнер
    Box {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(
                animationSpec = tween(alphaDurationMillis)
            ),
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .offset(0.dp, -offsetY.value.dp)
                ) {
                    firstChild?.invoke()
                }
                Box(
                    modifier = Modifier
                        .offset(0.dp, offsetY.value.dp)
                ) {
                    secondChild?.invoke()
                }
            }
        }
    }
}