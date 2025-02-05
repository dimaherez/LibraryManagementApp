package com.example.librarymanagementapp.di

import com.example.data.LibraryRepoImpl
import com.example.domain.repository.LibraryRepo
import com.example.domain.use_cases.BorrowBookUseCase
import com.example.domain.use_cases.FetchBooksUseCase
import com.example.domain.use_cases.ReturnBookUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideLibraryRepo(): LibraryRepo = LibraryRepoImpl()

    @Singleton
    @Provides
    fun provideFetchBooksUseCase(libraryRepo: LibraryRepo): FetchBooksUseCase {
        return FetchBooksUseCase(libraryRepo)
    }

    @Singleton
    @Provides
    fun provideReturnBookUseCase(libraryRepo: LibraryRepo): ReturnBookUseCase {
        return ReturnBookUseCase(libraryRepo)
    }

    @Singleton
    @Provides
    fun provideBorrowBookUseCase(libraryRepo: LibraryRepo): BorrowBookUseCase {
        return BorrowBookUseCase(libraryRepo)
    }
}