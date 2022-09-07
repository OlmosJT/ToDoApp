package uz.gita.todoapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.gita.todoapp.domain.usecase.AppUseCase
import uz.gita.todoapp.domain.usecase.impl.AppUseCaseImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface UseCaseModule {

    @[Binds Singleton]
    fun getAppUseCase(impl: AppUseCaseImpl): AppUseCase
}