package com.example.repos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.repos.base.BaseActivity
import com.example.repos.databinding.ActivityMainBinding
import com.example.repos.ui.HomeFragment
import com.example.repos.utils.NavigationUtils

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NavigationUtils.replaceFragment(HomeFragment(),supportFragmentManager,R.id.maincontainer)

    }
}