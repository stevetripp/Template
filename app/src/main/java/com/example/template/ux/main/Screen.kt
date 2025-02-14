package com.example.template.ux.main

import com.example.template.ux.about.AboutRoute
import com.example.template.ux.animatedgestures.AnimatedGesturesRoute
import com.example.template.ux.bottomSheet.BottomSheetRoute
import com.example.template.ux.bottomnavigation.BottomNavigationRoute
import com.example.template.ux.breadcrumbs.BreadcrumbsRoute
import com.example.template.ux.buttongroups.ButtonGroupsRoute
import com.example.template.ux.carousel.CarouselRoute
import com.example.template.ux.childwithnavigation.ChildWithNavigationRoute
import com.example.template.ux.chipsheet.ChipSheetRoute
import com.example.template.ux.datetimeformat.DateTimeFormatRoute
import com.example.template.ux.dialog.DialogRoute
import com.example.template.ux.edgetoedge.EdgeToEdgeRoute
import com.example.template.ux.flippable.FlippableRoute
import com.example.template.ux.gmailaddressfield.GmailAddressFieldRoute
import com.example.template.ux.home.HomeRoute
import com.example.template.ux.imagepicker.ImagePickerRoute
import com.example.template.ux.inputexamples.InputExamplesRoute
import com.example.template.ux.ktor.KtorRoute
import com.example.template.ux.memorize.MemorizeRoute
import com.example.template.ux.modalbottomsheet.ModalBottomSheetRoute
import com.example.template.ux.modaldrawersheet.ModalDrawerSheetRoute
import com.example.template.ux.modalsidesheet.ModalSideSheetRoute
import com.example.template.ux.navigatepager.NavigationPagerRoute
import com.example.template.ux.notification.NotificationRoute
import com.example.template.ux.notificationpermissions.NotificationPermissionsRoute
import com.example.template.ux.pager.PagerRoute
import com.example.template.ux.panningzooming.PanningZoomingRoute
import com.example.template.ux.parameters.ParametersRoute
import com.example.template.ux.permissions.PermissionsRoute
import com.example.template.ux.popwithresult.PopWithResultParentRoute
import com.example.template.ux.pullrefresh.PullRefreshRoute
import com.example.template.ux.regex.RegexRoute
import com.example.template.ux.reorderablelist.ReorderableListRoute
import com.example.template.ux.search.SearchRoute
import com.example.template.ux.servicesexamples.ServicesExamplesRoute
import com.example.template.ux.sidedrawer.SideDrawerRoute
import com.example.template.ux.snackbar.SnackbarRoute
import com.example.template.ux.stickyheaders.StickyHeadersRoute
import com.example.template.ux.swipablescreen.SwipableRoute
import com.example.template.ux.synchronizescrolling.SynchronizeScrollingRoute
import com.example.template.ux.systemui.SystemUiRoute
import com.example.template.ux.tabs.TabsRoute
import com.example.template.ux.typography.TypographyRoute
import com.example.template.ux.urinavigation.UriNavigationRoute
import com.example.template.ux.video.screen.VideoScreenRoute
import com.example.template.ux.webview.WebViewRoute
import org.lds.mobile.navigation.NavigationRoute

enum class Screen(val title: String, val route: NavigationRoute, val type: ScreenType = ScreenType.NAVIGATION) {
    ABOUT("About", AboutRoute),
    ANIMATED_GESTURE("Animated Gesture", AnimatedGesturesRoute, ScreenType.UI),
    BOTTOM_NAVIGATION("Bottom Navigation", BottomNavigationRoute, ScreenType.UI),
    BOTTOM_SHEET("Bottom Sheet", BottomSheetRoute, ScreenType.UI),
    BREADCRUMBS_SCREEN("Breadcrumbs", BreadcrumbsRoute(title = "Breadcrumbs"), ScreenType.UI),
    BUTTON_GROUPS("Button Groups", ButtonGroupsRoute, ScreenType.UI),
    CAROUSEL("Carousel", CarouselRoute, ScreenType.UI),
    CHILD_WITH_NAVIGATION("Child with Navigation", ChildWithNavigationRoute, ScreenType.UI),
    CHIP_SHEET("Chip Sheet", ChipSheetRoute, ScreenType.UI),
    DATE_TIME_FORMAT("Date/Time Format", DateTimeFormatRoute, ScreenType.UI),
    DIALOG("Dialog", DialogRoute, ScreenType.UI),
    EDGE_TO_EDGE("Edge to Edge Example", EdgeToEdgeRoute, ScreenType.UI),
    FLIPPABLE("Flippable", FlippableRoute, ScreenType.UI),
    GMAIL_ADDRESS_FIELD("Gmail Address Field", GmailAddressFieldRoute, ScreenType.UI),
    HOME("UI Examples", HomeRoute),
    IMAGE_PICKER("Image Picker", ImagePickerRoute, ScreenType.UI),
    INPUT_EXAMPLES("Input Examples", InputExamplesRoute, ScreenType.UI),
    KTOR("Ktor Test", KtorRoute, ScreenType.SERVICES),
    MEMORIZE("Memorize", MemorizeRoute, ScreenType.UI),
    MODAL_BOTTOM_SHEET("Modal Bottom Sheet", ModalBottomSheetRoute, ScreenType.UI),
    MODAL_DRAWER_SHEET("Modal Drawer Sheet", ModalDrawerSheetRoute, ScreenType.UI),
    MODAL_SIDE_SHEET("Modal Side Sheet", ModalSideSheetRoute, ScreenType.UI),
    NAVIGATION_PAGER("Navigation Pager", NavigationPagerRoute, ScreenType.UI),
    NOTIFICATION("Notifications", NotificationRoute, ScreenType.UI),
    NOTIFICATION_PERMISSIONS("Permissions (Notification)", NotificationPermissionsRoute, ScreenType.UI),
    PAGER("Pager", PagerRoute, ScreenType.UI),
    PANNING_ZOOMING("Panning and Zooming", PanningZoomingRoute, ScreenType.UI),
    PARAMETERS("Parameters", ParametersRoute, ScreenType.UI),
    PERMISSIONS("Permissions (Location)", PermissionsRoute, ScreenType.UI),
    POP_WITH_RESULT("Pop With Result", PopWithResultParentRoute, ScreenType.UI),
    PULL_REFRESH("Pull Refresh", PullRefreshRoute, ScreenType.UI),
    REGEX("RegEx Validator", RegexRoute, ScreenType.SERVICES),
    REORDERABLE_LIST("Reorderable List", ReorderableListRoute, ScreenType.UI),
    SEARCH("Search", SearchRoute, ScreenType.UI),
    SERVICE_EXAMPLES("Service Examples", ServicesExamplesRoute),
    SIDE_DRAWER("Side Drawer", SideDrawerRoute, ScreenType.UI),
    SNACKBAR("Snackbar", SnackbarRoute, ScreenType.UI),
    STICKY_HEADERS("Sticky Headers", StickyHeadersRoute, ScreenType.UI),
    SWIPABLE("Swipable", SwipableRoute, ScreenType.UI),
    SYNCHRONIZE_SCROLLING("Synchronize Scrolling", SynchronizeScrollingRoute, ScreenType.UI),
    SYSTEM_UI("System UI", SystemUiRoute, ScreenType.UI),
    TABS("Tabs", TabsRoute, ScreenType.UI),
    TYPOGRAPHY("Typography", TypographyRoute, ScreenType.UI),
    URI_NAVIGATION("Uri Navigation", UriNavigationRoute, ScreenType.UI),
    VIDEO("Video", VideoScreenRoute, ScreenType.UI),
    WEBVIEW("Webview", WebViewRoute, ScreenType.UI);
}

enum class ScreenType {
    UI,
    SERVICES,
    NAVIGATION;
}