package com.example.repos.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import com.application.nasapicturesapp.base.BaseFragment
import com.example.repos.MainActivity
import com.example.repos.R
import com.example.repos.data.ResponseMain
import com.example.repos.databinding.HomeFragmentBinding
import com.example.repos.utils.NavigationUtils
import com.example.repos.viewmodel.RepositoryViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagingApi::class)
class HomeFragment : BaseFragment<HomeFragmentBinding>() {
    private lateinit var repositoryViewModel: RepositoryViewModel
    private lateinit var homeFragmentBinding: HomeFragmentBinding
    private lateinit var repositoriesAdapter: RepositoriesAdapter
    private var position=0
    override fun getLayoutId(): Int {
        return R.layout.home_fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeFragmentBinding = getViewDataBinding()
        repositoryViewModel = ViewModelProvider(requireActivity()).get(RepositoryViewModel::class.java)
        initRecyclerView()
        lifecycleScope.launch {
            repositoryViewModel.getAllData().collectLatest { response ->
                homeFragmentBinding = getViewDataBinding()
                    .apply {
                        homeFragmentBinding.recvHome.visibility = View.VISIBLE
                        homeFragmentBinding.ProgressBar.visibility = View.GONE

                    }
                repositoriesAdapter.submitData(response)
                repositoriesAdapter.notifyItemChanged(position)
            }
        }
    }

    override fun onResume() {
        super.onResume()


    }

    private fun initRecyclerView() {
        homeFragmentBinding.recvHome.apply {
            setHasFixedSize(true)
            repositoriesAdapter = RepositoriesAdapter(::itemClicked)
            adapter = repositoriesAdapter
        }

    }

   private fun itemClicked(item:ResponseMain,position:Int):Unit{
       this.position=position
       repositoryViewModel.setData(item)
       NavigationUtils.addFragment(DetailFragment(),(activity as MainActivity).supportFragmentManager,R.id.maincontainer)

    }
}