package com.example.moviedb.ui.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviedb.data.remote.convertToBaseException
import com.example.moviedb.utils.SingleLiveEvent
import kotlinx.coroutines.*
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.UnknownHostException

abstract class BaseViewModel : ViewModel() {

    // loading flag
    val isLoading = MutableLiveData<Boolean>().apply { value = false }

    // error message
    val errorMessage = SingleLiveEvent<String>()

    // optional flags
    val noInternetConnectionEvent = SingleLiveEvent<Unit>()
    val connectTimeoutEvent = SingleLiveEvent<Unit>()
    val forceUpdateAppEvent = SingleLiveEvent<Unit>()
    val serverMaintainEvent = SingleLiveEvent<Unit>()
    val unknownErrorEvent = SingleLiveEvent<Unit>()

    // rx
//    private val compositeDisposable = CompositeDisposable()
//    fun addDisposable(disposable: Disposable) = compositeDisposable.add(disposable)

    // coroutines

    // exception handler for coroutine
    private val exceptionHandler = CoroutineExceptionHandler { context, throwable ->
        viewModelScope.launch {
            onError(throwable)
        }
    }

    protected val viewModelScopeExceptionHandler = viewModelScope + exceptionHandler

    /**
     * handle throwable when load fail
     */
    open suspend fun onError(throwable: Throwable) {
        withContext(Dispatchers.Main) {
            when (throwable) {
                // case no internet connection
                is UnknownHostException -> {
                    noInternetConnectionEvent.call()
                }
                is ConnectException -> {
                    noInternetConnectionEvent.call()
                }
                // case request time out
                is SocketTimeoutException -> {
                    connectTimeoutEvent.call()
                }
                else -> {
                    // convert throwable to base exception to get error information
                    val baseException = convertToBaseException(throwable)
                    when (baseException.httpCode) {
                        HttpURLConnection.HTTP_UNAUTHORIZED -> {
                            errorMessage.value = baseException.message
                        }
                        HttpURLConnection.HTTP_INTERNAL_ERROR -> {
                            errorMessage.value = baseException.message
                        }
                        else -> {
                            unknownErrorEvent.call()
                        }
                    }
                }
            }
            hideLoading()
        }
    }

    open fun showError(e: Throwable) {
        errorMessage.value = e.message
    }

    fun showLoading() {
        isLoading.value = true
    }

    fun hideLoading() {
        isLoading.value = false
    }

    override fun onCleared() {
        super.onCleared()
//        compositeDisposable.clear()
//        viewModelJob.cancel()
    }
}