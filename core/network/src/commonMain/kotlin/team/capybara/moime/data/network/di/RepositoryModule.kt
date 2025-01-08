package team.capybara.moime.data.network.di

import org.koin.dsl.module
import team.capybara.moime.core.data.repository.CameraRepository
import team.capybara.moime.core.data.repository.FriendRepository
import team.capybara.moime.core.data.repository.InsightRepository
import team.capybara.moime.core.data.repository.MeetingRepository
import team.capybara.moime.core.data.repository.NotificationRepository
import team.capybara.moime.core.data.repository.UserRepository
import team.capybara.moime.data.network.repository.DefaultCameraRepository
import team.capybara.moime.data.network.repository.DefaultFriendRepository
import team.capybara.moime.data.network.repository.DefaultInsightRepository
import team.capybara.moime.data.network.repository.DefaultMeetingRepository
import team.capybara.moime.data.network.repository.DefaultNotificationRepository
import team.capybara.moime.data.network.repository.DefaultUserRepository

val repositoryModule = module {
    single<UserRepository> { DefaultUserRepository(get(), get(), get()) }
    single<MeetingRepository> { DefaultMeetingRepository(get()) }
    single<FriendRepository> { DefaultFriendRepository(get()) }
    single<CameraRepository> { DefaultCameraRepository(get()) }
    single<InsightRepository> { DefaultInsightRepository(get()) }
    single<NotificationRepository> { DefaultNotificationRepository(get()) }
}
