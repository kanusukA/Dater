package com.example.dater.di

import com.example.dater.Data.UiState.dataSource.repository.UiStateHomeRepoImpli
import com.example.dater.Data.UiState.dataSource.repository.UiStateRepoImpli
import com.example.dater.Data.UiState.domain.model.UiState
import com.example.dater.Data.UiState.domain.repository.UiStateHomeRepository
import com.example.dater.Data.UiState.domain.repository.UiStateRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UiModule {

    @Provides
    @Singleton
    fun provideUiState(): UiState{
        return UiState()
    }

    @Provides
    @Singleton
    fun provideUiStateRepository(uiState: UiState): UiStateRepository{
        return UiStateRepoImpli(uiState)
    }

    @Provides
    @Singleton
    fun providesUiStateHomeRepository(uiState: UiState): UiStateHomeRepository{
        return UiStateHomeRepoImpli(uiState)
    }
}