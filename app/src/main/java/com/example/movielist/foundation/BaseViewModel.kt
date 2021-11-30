package com.example.movielist.foundation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movielist.utils.Status

typealias LiveResult<T> = LiveData<Status<T>>
typealias MutableLiveResult<T> = MutableLiveData<Status<T>>
typealias MediatorLiveResult<T> = MediatorLiveData<Status<T>>

open class BaseViewModel: ViewModel() {
}