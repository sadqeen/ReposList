package com.example.repos.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import com.example.repos.data.ResponseMain

import com.example.repos.repository.local.ReposDb
import com.example.repos.repository.network.GlobalRetrofit
import com.example.repos.repository.network.ReposRemoteMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch

class RepositoryViewModel(application: Application) : AndroidViewModel(application),
    LifecycleOwner {
    private lateinit var pagingSource: PagingSource<Int, ResponseMain>
    var bgScope = CoroutineScope(Dispatchers.IO)

    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    @SuppressLint("StaticFieldLeak")
    private var lifecycleRegistry: LifecycleRegistry = LifecycleRegistry(this)
    private var db: ReposDb
    private var retrofitApi = GlobalRetrofit.getClient()
    private val _data = MutableLiveData<ResponseMain>()
    val data: LiveData<ResponseMain>
        get() = _data


    init {
        lifecycleRegistry.currentState = Lifecycle.State.CREATED
        lifecycleRegistry.currentState = Lifecycle.State.STARTED
        db = ReposDb.invoke(application)
        pagingSource = db.reposDao().getAllPictures()
    }

    fun setData(value: ResponseMain) {
        _data.value = value
    }


    fun setBookmark(item:ResponseMain)
    {
        bgScope.launch {
            db.reposDao().updateBookmark(item)
            pagingSource.invalidate()


        }

    }


    @ExperimentalPagingApi
    fun getAllData(): Flow<PagingData<ResponseMain>> {

        return Pager(
            config = PagingConfig(25, enablePlaceholders = false,),
            pagingSourceFactory = { db.reposDao().getAllPictures() },
            remoteMediator = ReposRemoteMediator(db, retrofitApi)
        ).flow.cachedIn(viewModelScope)
    }

    override val lifecycle: Lifecycle
        get() = lifecycleRegistry
}