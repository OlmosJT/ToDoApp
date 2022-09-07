package uz.gita.todoapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.gita.todoapp.domain.repository.AppRepository
import uz.gita.todoapp.domain.repository.impl.AppRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @[Binds Singleton]
    fun getAppRepository(impl: AppRepositoryImpl): AppRepository

}