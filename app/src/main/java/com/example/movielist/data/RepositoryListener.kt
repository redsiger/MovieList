package com.example.movielist.data

interface RepositoryListener {
    fun dataChanged()
    fun reminderDeleted(movieId: Int)
    fun reminderAdded(movieId: Int)
}