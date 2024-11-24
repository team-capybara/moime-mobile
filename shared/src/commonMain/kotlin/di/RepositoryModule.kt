package di

import data.repository.CameraRepositoryImpl
import data.repository.FriendRepositoryImpl
import data.repository.InsightRepositoryImpl
import data.repository.MeetingRepositoryImpl
import data.repository.NotificationRepositoryImpl
import data.repository.UserRepositoryImpl
import org.koin.dsl.module
import ui.repository.CameraRepository
import ui.repository.FriendRepository
import ui.repository.InsightRepository
import ui.repository.MeetingRepository
import ui.repository.NotificationRepository
import ui.repository.UserRepository

val repositoryModule = module {
    single<UserRepository> { UserRepositoryImpl(get(), get(), get()) }
    single<MeetingRepository> { MeetingRepositoryImpl(get()) }
    single<FriendRepository> { FriendRepositoryImpl(get()) }
    single<CameraRepository> { CameraRepositoryImpl(get()) }
    single<InsightRepository> { InsightRepositoryImpl(get()) }
    single<NotificationRepository> { NotificationRepositoryImpl(get()) }
}
