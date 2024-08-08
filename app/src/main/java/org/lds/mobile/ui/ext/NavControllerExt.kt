package org.lds.mobile.ui.ext

import android.content.Context
import androidx.navigation.NavController

/**
 * NavController extension to popBackStack() AND finishAffinity() IF pop did NOT succeed
 *
 * This is useful for support of Navigation Back button when using deeplinking
 *
 * @param context Ui Context (do not use Application Context)
 */
fun NavController.popBackStackOrFinishActivity(context: Context) {
    if (!popBackStack()) {
        context.requireActivity().finishAffinity()
    }
}