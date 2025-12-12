package com.example.template.ui

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.content.res.Configuration.UI_MODE_TYPE_NORMAL
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import org.lds.mobile.navigation3.navigator.Navigation3Navigator

@PreviewLightDark
annotation class PreviewDefault

@PreviewLightDark
@PreviewScreenSizes
annotation class PreviewAll

@PreviewPhonePortrait
@PreviewPhoneLandscape
annotation class PreviewPhoneOrientations

@Preview(group = "PhonePortrait", device = "spec:width=411dp,height=891dp,dpi=420", name = "Phone Portrait Light")
@Preview(group = "PhonePortrait", device = "spec:width=411dp,height=891dp,dpi=420", uiMode = UI_MODE_NIGHT_YES or UI_MODE_TYPE_NORMAL, name = "Phone Portrait Dark")
annotation class PreviewPhonePortrait

@Preview(group = "PhoneLandscape", device = "spec:width=411dp,height=891dp,orientation=landscape,dpi=420", name = "Phone Landscape Light")
@Preview(group = "PhoneLandscape", device = "spec:width=411dp,height=891dp,orientation=landscape,dpi=420", uiMode = UI_MODE_NIGHT_YES or UI_MODE_TYPE_NORMAL, name = "Phone Landscape Dark")
annotation class PreviewPhoneLandscape

class PreviewNavigator : Navigation3Navigator {
    override fun getCurrentBackStack(): NavBackStack<NavKey>? = null
    override fun navigate(key: NavKey) {}
    override fun navigate(keys: List<NavKey>) {}
    override fun pop(): Boolean = false
    override fun pop(key: NavKey?): Boolean = false
    override fun popAndNavigate(key: NavKey): Boolean = false
    override fun navigateTopLevel(key: NavKey, reselected: Boolean) {}
    override fun getSelectedTopLevelRoute(): NavKey = object : NavKey {}
}