package team.capybara.moime.di

import org.koin.dsl.module
import team.capybara.moime.data.repository.CameraRepositoryImpl
import team.capybara.moime.data.repository.FriendRepositoryImpl
import team.capybara.moime.data.repository.InsightRepositoryImpl
import team.capybara.moime.data.repository.MeetingRepositoryImpl
import team.capybara.moime.data.repository.NotificationRepositoryImpl
import team.capybara.moime.data.repository.UserRepositoryImpl
import team.capybara.moime.ui.repository.CameraRepository
import team.capybara.moime.ui.repository.FriendRepository
import team.capybara.moime.ui.repository.InsightRepository
import team.capybara.moime.ui.repository.MeetingRepository
import team.capybara.moime.ui.repository.NotificationRepository
import team.capybara.moime.ui.repository.UserRepository

val repositoryModule = module {
    single<UserRepository> { UserRepositoryImpl(get(), get(), get()) }
    single<MeetingRepository> { MeetingRepositoryImpl(get()) }
    single<FriendRepository> { FriendRepositoryImpl(get()) }
    single<CameraRepository> { CameraRepositoryImpl(get()) }
    single<InsightRepository> { InsightRepositoryImpl(get()) }
    single<NotificationRepository> { NotificationRepositoryImpl(get()) }
}
