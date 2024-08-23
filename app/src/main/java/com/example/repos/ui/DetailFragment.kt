package com.example.repos.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.application.nasapicturesapp.base.BaseFragment
import com.example.repos.R
import com.example.repos.data.ResponseMain
import com.example.repos.databinding.DetailFragmentBinding
import com.example.repos.viewmodel.RepositoryViewModel

class DetailFragment:BaseFragment<DetailFragmentBinding>() {
    private lateinit var detailFragmentBinding: DetailFragmentBinding
    private lateinit var repositoryViewModel: RepositoryViewModel
    private lateinit var repoItem:ResponseMain



    override fun getLayoutId(): Int {
       return R.layout.detail_fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        detailFragmentBinding=getViewDataBinding()
        repositoryViewModel = ViewModelProvider(requireActivity()).get(RepositoryViewModel::class.java)
        repositoryViewModel.data.observe(viewLifecycleOwner, Observer {
            repoItem=it
            detailFragmentBinding.data=repoItem
            if(repoItem.isBookMark)
            {
                detailFragmentBinding.icBookmark.isChecked=true
            }
        })




        detailFragmentBinding.icBookmark.setOnCheckedChangeListener { buttonView, isChecked ->
            repoItem.isBookMark=isChecked
            repositoryViewModel.setBookmark(repoItem)

        }


    }
}