package com.example.template.ux.main

enum class Screen(val title: String, val type: ScreenType = ScreenType.NAVIGATION) {
    ABOUT("About"),
    ANIMATED_GESTURE("Animated Gesture", ScreenType.UI),
    BOTTOM_NAVIGATION("Bottom Navigation", ScreenType.UI),
    BOTTOM_SHEET("Bottom Sheet", ScreenType.UI),
    BUTTON_GROUPS("Button Groups", ScreenType.UI),
    CAROUSEL("Carousel", ScreenType.UI),
    CHILD_WITH_NAVIGATION("Child with Navigation", ScreenType.UI),
    CHIP_SHEET("Chip Sheet", ScreenType.UI),
    DATE_TIME_FORMAT("Date/Time Format", ScreenType.UI),
    DIALOG("Dialog", ScreenType.UI),
    FLIPPABLE("Flippable", ScreenType.UI),
    GMAIL_ADDRESS_FIELD("Gmail Address Field", ScreenType.UI),
    HOME("UI Examples"),
    IMAGE_PICKER("Image Picker", ScreenType.UI),
    INPUT_EXAMPLES("Input Examples", ScreenType.UI),
    KTOR("Ktor Test", ScreenType.SERVICES),
    MODAL_BOTTOM_SHEET("Modal Bottom Sheet", ScreenType.UI),
    MODAL_SIDE_SHEET("Modal Side Sheet", ScreenType.UI),
    NAVIGATION_PAGER("Navigation Pager", ScreenType.UI),
    NOTIFICATION_PERMISSIONS("Permissions (Notification)", ScreenType.UI),
    PAGER("Pager", ScreenType.UI),
    PANNING_ZOOMING("Panning and Zooming", ScreenType.UI),
    PARAMETERS("Parameters", ScreenType.UI),
    PERMISSIONS("Permissions (Location)", ScreenType.UI),
    POP_WITH_RESULT("Pop With Result", ScreenType.UI),
    PULL_REFRESH("Pull Refresh", ScreenType.UI),
    REORDERABLE_LIST("Reorderable List", ScreenType.UI),
    SEARCH("Search", ScreenType.UI),
    SERVICE_EXAMPLES("Service Examples"),
    SIDE_DRAWER("Side Drawer", ScreenType.UI),
    SNACKBAR("Snackbar", ScreenType.UI),
    STICKY_HEADERS("Sticky Headers", ScreenType.UI),
    SWIPABLE("Swipable", ScreenType.UI),
    SYSTEM_UI("System UI", ScreenType.UI),
    TABS("Tabs", ScreenType.UI),
    URI_NAVIGATION("Uri Navigation", ScreenType.UI),
    VIDEO("Video", ScreenType.UI),
    WEBVIEW("Webview", ScreenType.UI);
}

enum class ScreenType {
    UI,
    SERVICES,
    NAVIGATION;
}