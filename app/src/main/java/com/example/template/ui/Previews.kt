package com.example.template.ui

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.content.res.Configuration.UI_MODE_TYPE_NORMAL
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes

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
@Preview(group = "PhoneLandscape", device = "spec:width=411dp,height=891dp,orientation=landscape,dpi=420", uiMode = UI_MODE_NIGHT_YES or UI_MODE_TYPE_NORMAL, name = "Phone Landscape Light")
annotation class PreviewPhoneLandscape