package com.example.librarymanagementapp.di

import com.example.data.LibraryRepoImpl
import com.example.domain.repository.LibraryRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    abstract fun provideLibraryRepo(libraryRepoImpl: LibraryRepoImpl): LibraryRepo
}