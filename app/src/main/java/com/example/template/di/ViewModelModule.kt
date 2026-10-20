package com.example.template.di

import com.example.template.ux.breadcrumbs.BreadCrumbsViewModel
import com.example.template.ux.chipsheet.ChipSheetViewModel
import com.example.template.ux.dialog.DialogViewModel
import com.example.template.ux.edgetoedge.EdgeToEdgeViewModel
import com.example.template.ux.fab.FabViewModel
import com.example.template.ux.gmailaddressfield.GmailAddressFieldViewModel
import com.example.template.ux.googlemaps.GoogleMapsViewModel
import com.example.template.ux.home.HomeViewModel
import com.example.template.ux.ktor.KtorViewModel
import com.example.template.ux.main.MainViewModel
import com.example.template.ux.maze.MazeViewModel
import com.example.template.ux.memorize.MemorizeViewModel
import com.example.template.ux.modalsidesheet.ModalSideSheetViewModel
import com.example.template.ux.notification.NotificationViewModel
import com.example.template.ux.notificationpermissions.NotificationPermissionsViewModel
import com.example.template.ux.parameters.DestinationViewModel
import com.example.template.ux.parameters.ParametersViewModel
import com.example.template.ux.permissions.PermissionsViewModel
import com.example.template.ux.popwithresult.PopWithResultChildViewModel
import com.example.template.ux.popwithresult.PopWithResultParentViewModel
import com.example.template.ux.pullrefresh.PullRefreshViewModel
import com.example.template.ux.regex.RegexViewModel
import com.example.template.ux.reorderablelist.ReorderableListViewModel
import com.example.template.ux.search.SearchViewModel
import com.example.template.ux.servicesexamples.ServicesExamplesViewModel
import com.example.template.ux.settings.SettingsViewModel
import com.example.template.ux.snackbar.SnackbarViewModel
import com.example.template.ux.startup.StartupViewModel
import com.example.template.ux.stickyheaders.StickyHeaderViewModel
import com.example.template.ux.synchronizescrolling.SynchronizeScrollingViewModel
import com.example.template.ux.video.player.PlayerViewModel
import com.example.template.ux.video.screen.VideoScreenViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::BreadCrumbsViewModel)
    viewModelOf(::ChipSheetViewModel)
    viewModelOf(::DestinationViewModel)
    viewModelOf(::DialogViewModel)
    viewModelOf(::EdgeToEdgeViewModel)
    viewModelOf(::FabViewModel)
    viewModelOf(::GmailAddressFieldViewModel)
    viewModelOf(::GoogleMapsViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::KtorViewModel)
    viewModelOf(::MainViewModel)
    viewModelOf(::MazeViewModel)
    viewModelOf(::MemorizeViewModel)
    viewModelOf(::ModalSideSheetViewModel)
    viewModelOf(::NotificationPermissionsViewModel)
    viewModelOf(::NotificationViewModel)
    viewModelOf(::ParametersViewModel)
    viewModelOf(::PermissionsViewModel)
    viewModelOf(::PlayerViewModel)
    viewModelOf(::PopWithResultChildViewModel)
    viewModelOf(::PopWithResultParentViewModel)
    viewModelOf(::PullRefreshViewModel)
    viewModelOf(::RegexViewModel)
    viewModelOf(::ReorderableListViewModel)
    viewModelOf(::SearchViewModel)
    viewModelOf(::ServicesExamplesViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::SnackbarViewModel)
    viewModelOf(::StartupViewModel)
    viewModelOf(::StickyHeaderViewModel)
    viewModelOf(::SynchronizeScrollingViewModel)
    viewModelOf(::VideoScreenViewModel)
}
