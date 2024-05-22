package com.example.template.ux.video.player

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavDestination
import androidx.navigation.NavDestinationBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.template.ux.video.VideoId
import org.lds.mobile.navigation.NavActivityRoute
import org.lds.mobile.navigation.NavRoute
import org.lds.mobile.navigation.NavRouteDefinition
import org.lds.mobile.navigation.RouteUtil

object PlayerRoute : NavActivityRoute() {

    private const val ROUTE = "PlayerRoute"

    override val routeDefinition = NavRouteDefinition("$ROUTE/${RouteUtil.defineArg(Arg.VIDEO_ID)}?${RouteUtil.defineOptionalArgs(Arg.PLAY_LIST)}")

    fun createRoute(videoId: VideoId, playList: List<VideoId>?): NavRoute {
        val videoIdArg = RouteUtil.encodeArg(videoId.value)
        val playListArg = RouteUtil.optionalArgs(Arg.PLAY_LIST to playList?.let { pl -> RouteUtil.listToString(pl.map { it.value }) })
        return NavRoute("$ROUTE/$videoIdArg?$playListArg")
    }


    override fun <D : NavDestination> NavDestinationBuilder<D>.setupNav() {
        argument(Arg.VIDEO_ID) { type = NavType.StringType }
        navArgument(Arg.PLAY_LIST) {
            type = NavType.StringType
            defaultValue = null
            nullable = true
        }
    }

    data class Args(val videoId: VideoId, val playList: List<VideoId>?)

    fun getArgs(savedStateHandle: SavedStateHandle): Args {
        val videoId = VideoId(requireNotNull(savedStateHandle.get<String>(Arg.VIDEO_ID)))
        val playListString = savedStateHandle.get<String?>(Arg.PLAY_LIST)
        val playList: List<String>? = playListString?.let { playListString.split(RouteUtil.DEFAULT_STRING_LIST_DELIMITER) }
        return Args(videoId, playList?.map { VideoId(it) })
    }

    object Arg {
        const val VIDEO_ID = "videoId"
        const val PLAY_LIST = "playList"
    }
}


