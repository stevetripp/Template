package com.example.template.ux.main

import androidx.lifecycle.ViewModel
import org.lds.mobile.navigation.ViewModelNav
import org.lds.mobile.navigation.ViewModelNavImpl

class MainViewModel : ViewModel(), ViewModelNav by ViewModelNavImpl() {
    fun isReady(): Boolean = true
}