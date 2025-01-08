package team.capybara.moime.feature.main

import androidx.compose.runtime.Stable
import team.capybara.moime.feature.home.HomeTab
import team.capybara.moime.feature.home.HomeTabView
import team.capybara.moime.feature.insight.InsightTab
import team.capybara.moime.feature.insight.InsightTabView

@Stable
data class MainTabViewState(
    val currentHomeTabView: HomeTabView = HomeTabView.ListView,
    val currentInsightTabView: InsightTabView = InsightTabView.Summary
) {

    fun copy(tabView: MainTabView): MainTabViewState {
        return when (tabView) {
            is HomeTabView -> copy(currentHomeTabView = tabView)
            is InsightTabView -> copy(currentInsightTabView = tabView)
            else -> this
        }
    }

    fun getCurrentTabViewWithTab(currentTab: MainTab): MainTabView {
        return when (currentTab) {
            HomeTab -> currentHomeTabView
            InsightTab -> currentInsightTabView
            else -> throw IllegalArgumentException("Unknown tab: $currentTab")
        }
    }
}
