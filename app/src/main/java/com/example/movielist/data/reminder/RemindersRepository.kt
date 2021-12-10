package com.example.movielist.data.reminder

import android.content.Context
import com.example.movielist.screens.reminders.data.Reminder
import com.example.movielist.data.Repository
import com.example.movielist.data.RepositoryListener
import com.example.movielist.utils.Status

class RemindersRepository(
    private val dao: RemindersDao,
    private val context: Context
): Repository {

    private var listeners: MutableSet<RepositoryListener> = mutableSetOf()
    fun addListener(listener: RepositoryListener) {
        listeners.add(listener)
        listeners.forEach { update() }
    }

    private fun update() { listeners.forEach { it.dataChanged() } }
    private fun reminderDeleted(movieId: Int) { listeners.forEach { it.reminderDeleted(movieId) } }
    private fun reminderAdded(movieId: Int) { listeners.forEach { it.reminderAdded(movieId) } }


    suspend fun getReminders(): Status<List<Reminder>> {
        return try {
            Status.Success(dao.getReminders())
        } catch (e: Exception) {
            return Status.Error(e)
        }
    }

    suspend fun getReminder(movieId: Int): Status<Reminder> {
        return try {
            val reminder = dao.getReminder(movieId)
            if (reminder != null) {
                Status.Success(reminder)
            } else {
                val s: String = "$movieId not found"
                Status.Error(Exception(s))
            }
        } catch (e: Exception) {
            return Status.Error(e)
        }
    }

    suspend fun deleteReminder(movieId: Int) {
        dao.deleteReminder(movieId)
        reminderDeleted(movieId)
        update()
    }

    suspend fun addReminder(reminder: Reminder) {
        dao.addReminder(reminder)
        reminderAdded(reminder.movieId)
        update()
    }
}