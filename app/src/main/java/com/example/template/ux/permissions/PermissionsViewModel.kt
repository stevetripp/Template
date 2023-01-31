package com.example.template.ux.permissions

import androidx.lifecycle.ViewModel
import com.example.template.ui.navigation.ViewModelNav
import com.example.template.ui.navigation.ViewModelNavImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PermissionsViewModel
@Inject constructor(
) : ViewModel(), ViewModelNav by ViewModelNavImpl()