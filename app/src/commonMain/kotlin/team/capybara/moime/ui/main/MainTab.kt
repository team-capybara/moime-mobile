package team.capybara.moime.ui.main

import cafe.adriel.voyager.navigator.tab.Tab

interface MainTab : Tab {
    val tabViews: List<MainTabView>
}
