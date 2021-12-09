package com.example.movielist.data

interface RepositoryListener {
    fun dataChanged()
    fun alarmDeleted(movieId: Int)
    fun alarmAdded(movieId: Int)
}