package com.example.template.ux.nesteddeeplink

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.lds.mobile.navigation.ViewModelNav
import org.lds.mobile.navigation.ViewModelNavImpl
import javax.inject.Inject

@HiltViewModel
class Level3ViewModel
@Inject
constructor(

) : ViewModel(), ViewModelNav by ViewModelNavImpl()