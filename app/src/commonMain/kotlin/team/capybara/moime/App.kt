package team.capybara.moime

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.NavigatorDisposeBehavior
import cafe.adriel.voyager.transitions.SlideTransition
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.request.crossfade
import dev.chrisbanes.haze.HazeState
import team.capybara.moime.ui.LocalHazeState
import team.capybara.moime.ui.LocalScreenSize
import team.capybara.moime.ui.LocalToastHandler
import team.capybara.moime.ui.ScreenSize
import team.capybara.moime.ui.ToastHandler
import team.capybara.moime.ui.ToastState
import team.capybara.moime.ui.component.MoimeToast
import team.capybara.moime.ui.splash.SplashScreen
import team.capybara.moime.ui.theme.MoimeTheme

@OptIn(ExperimentalVoyagerApi::class)
@Composable
fun App() {
    setSingletonImageLoaderFactory { context ->
        ImageLoader
            .Builder(context)
            .crossfade(true)
            .build()
    }

    var screenSize by remember { mutableStateOf(ScreenSize()) }
    var toastState by remember { mutableStateOf<ToastState?>(null) }

    Layout(
        content = {
            Box(modifier = Modifier.fillMaxSize()) {
                MoimeTheme {
                    CompositionLocalProvider(
                        LocalScreenSize provides screenSize,
                        LocalHazeState provides HazeState(),
                        LocalToastHandler provides
                            ToastHandler(
                                isShowing = toastState != null,
                                onShow = { toastState = it },
                            ),
                    ) {
                        Navigator(
                            screen = SplashScreen(),
                            disposeBehavior = NavigatorDisposeBehavior(disposeSteps = false),
                        ) { navigator ->
                            SlideTransition(
                                navigator = navigator,
                                disposeScreenAfterTransitionEnd = true,
                            )
                        }
                    }
                }
                toastState?.let {
                    MoimeToast(
                        state = it,
                        onDismissRequest = { toastState = null },
                        modifier = Modifier.align(Alignment.BottomCenter),
                    )
                }
            }
        },
        measurePolicy = { measurables, constraints ->
            val width = constraints.maxWidth
            val height = constraints.maxHeight

            screenSize = ScreenSize(width, height)

            val placeables =
                measurables.map { measurable ->
                    measurable.measure(constraints)
                }

            layout(width, height) {
                var yPosition = 0
                placeables.forEach { placeable ->
                    placeable.placeRelative(x = 0, y = yPosition)
                    yPosition += placeable.height
                }
            }
        },
    )
}
