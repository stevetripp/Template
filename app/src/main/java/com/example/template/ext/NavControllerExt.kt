package com.example.template.ext

import android.content.Context
import androidx.navigation.NavController
import org.lds.mobile.ui.ext.requireActivity

fun NavController.popBackStackOrFinishActivity(context: Context) {
    if (!popBackStack()) {
        context.requireActivity().finish()
    }
}
