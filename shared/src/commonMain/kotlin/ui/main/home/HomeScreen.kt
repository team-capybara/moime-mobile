package ui.main.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.koin.koinScreenModel
import ui.component.ExceptionDialog
import ui.component.MoimeLoading
import ui.main.MainScreenModel

class HomeScreen : Screen {

    override val key: ScreenKey = uniqueScreenKey

    @Composable
    override fun Content() {
        val mainScreenModel = koinScreenModel<MainScreenModel>()
        val homeScreenModel = koinScreenModel<HomeScreenModel>()
        val homeState by homeScreenModel.state.collectAsState()

        when (mainScreenModel.tabViewState.currentHomeTabView) {
            HomeTabView.ListView ->
                if (homeState.homeListState.isMeetingInitialized) {
                    HomeListView(
                        state = homeState.homeListState,
                        onRefresh = { homeScreenModel.refreshListState() },
                        onLoadCompletedMeetings = { homeScreenModel.loadCompleteMeetings() },
                        isActiveMeetingVisible = mainScreenModel.topAppBarBackgroundVisible.not(),
                        onActiveMeetingVisibleChanged = {
                            mainScreenModel.setTopAppBarBackgroundVisibility(it.not())
                        }
                    )
                } else {
                    MoimeLoading()
                }

            HomeTabView.CalendarView -> {
                HomeCalendarView(
                    state = homeState.homeCalendarState,
                    onDayClicked = { day ->
                        homeScreenModel.loadMeetingOfDay(day) {
                            mainScreenModel.showMeetingsBottomSheet(it)
                        }
                    },
                    onRefresh = { homeScreenModel.refreshCalendarState() }
                )
            }
        }
        homeState.exception?.let {
            ExceptionDialog(
                exception = it,
                onDismiss = { homeScreenModel.clearException() }
            )
        }
    }
}
