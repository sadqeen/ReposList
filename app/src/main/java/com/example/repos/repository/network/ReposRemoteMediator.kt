package com.example.repos.repository.network

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.repos.data.RemoteKeys
import com.example.repos.data.ResponseMain
import com.example.repos.repository.local.ReposDb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

@ExperimentalPagingApi
class ReposRemoteMediator constructor(
    private val db: ReposDb,
    private val apiService: RetrofitApi
) : RemoteMediator<Int, ResponseMain>() {
    private val STARTING_PAGE_INDEX = 1

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ResponseMain>
    ): MediatorResult {
        val pageKeyData = getKeyPageData(loadType, state)
        val page = when (pageKeyData) {
            is MediatorResult.Success -> {
                return pageKeyData
            }

            else -> {
                pageKeyData as Int
            }
        }

        try {
            val response = apiService.getAllRepos(page)
            val endOfList = response.isEmpty()
            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                   /* db.reposDao().clearAll()
                    db.remoteDao().clearAll()*/
               }
                val prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfList) null else page + 1
                val keys = response.map {
                    RemoteKeys(it.id!!, prevKey, nextKey)
                }
                db.remoteDao().insertRemote(keys)
                response.map { it.page=page }
                db.reposDao().addAllPictures(response)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfList)
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }


    private suspend fun getKeyPageData(
        loadType: LoadType,
        state: PagingState<Int, ResponseMain>
    ): Any {
        return when (loadType) {
            LoadType.REFRESH -> {
                Log.e("TAG", "getKeyPageData: refresh", )
                val remoteKeys = getRefreshRemoteKey(state)
                remoteKeys?.nextKey?.minus(1) ?: STARTING_PAGE_INDEX
            }

            LoadType.PREPEND -> {
                val remoteKeys = getFirstRemoteKey(state)
                Log.e("TAG", "getKeyPageData: prepend", )
                val prevKey = remoteKeys?.prevKey ?: MediatorResult.Success(
                    endOfPaginationReached = false
                )
                prevKey
            }

            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKey(state)

                val nextKey = remoteKeys?.nextKey ?: MediatorResult.Success(
                    endOfPaginationReached = false
                )
                nextKey
            }
        }
    }

    private suspend fun getFirstRemoteKey(state: PagingState<Int, ResponseMain>): RemoteKeys? {
        return withContext(Dispatchers.IO) {
            state.pages
                .firstOrNull { it.data.isNotEmpty() }
                ?.data?.firstOrNull()
                ?.let { responseMain -> db.remoteDao().getRemoteKeys(responseMain.id!!) }
        }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, ResponseMain>): RemoteKeys? {
        return withContext(Dispatchers.IO) {
            state.pages
                .lastOrNull { it.data.isNotEmpty() }
                ?.data?.lastOrNull()
                ?.let { responseMain -> db.remoteDao().getRemoteKeys(responseMain.id!!) }
        }
    }

    private suspend fun getRefreshRemoteKey(state: PagingState<Int, ResponseMain>): RemoteKeys? {
        return withContext(Dispatchers.IO) {
            state.anchorPosition?.let { position ->
                state.closestItemToPosition(position)?.id?.let { repId ->
                    db.remoteDao().getRemoteKeys(repId)
                }
            }
        }
    }

}
