package com.example.data

import com.example.domain.di.DiReplacer

object Repo {

    init {
        DiReplacer.libraryRepo = LibraryRepoImpl()
    }

    fun init() {}
}