package com.example.template.ux.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import org.lds.mobile.ui.ext.requireActivity

@Composable
fun getSharedMainViewModel(): MainViewModel = hiltViewModel(LocalContext.current.requireActivity())