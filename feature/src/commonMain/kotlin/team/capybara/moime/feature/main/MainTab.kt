package team.capybara.moime.feature.main

import cafe.adriel.voyager.navigator.tab.Tab

interface MainTab : Tab {
    val tabViews: List<MainTabView>
}
