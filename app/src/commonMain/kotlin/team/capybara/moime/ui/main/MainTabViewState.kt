package team.capybara.moime.ui.main

import team.capybara.moime.ui.main.home.HomeTab
import team.capybara.moime.ui.main.home.HomeTabView
import team.capybara.moime.ui.main.insight.InsightTab
import team.capybara.moime.ui.main.insight.InsightTabView

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
