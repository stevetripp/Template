package com.example.template.ux.video.player

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavDestination
import androidx.navigation.NavDestinationBuilder
import androidx.navigation.NavType
import com.example.template.ux.video.VideoId
import org.lds.mobile.navigation.NavActivityRoute
import org.lds.mobile.navigation.NavRoute
import org.lds.mobile.navigation.NavRouteDefinition
import org.lds.mobile.navigation.RouteUtil

object PlayerRoute : NavActivityRoute() {

    private const val ROUTE = "PlayerRoute"

    override val routeDefinition = NavRouteDefinition("$ROUTE/${RouteUtil.defineArg(Arg.VIDEO_ID)}")

    fun createRoute(videoId: VideoId): NavRoute {
        val videoIdArg = RouteUtil.encodeArg(videoId.value)
        return NavRoute("$ROUTE/$videoIdArg")
    }

    override fun <D : NavDestination> NavDestinationBuilder<D>.setupNav() {
        argument(Arg.VIDEO_ID) { type = NavType.StringType }
    }

    data class Args(val videoId: VideoId)

    fun getArgs(savedStateHandle: SavedStateHandle): Args {
        val videoId = VideoId(requireNotNull(savedStateHandle.get<String>(Arg.VIDEO_ID)))
        return Args(videoId)
    }

    object Arg {
        const val VIDEO_ID = "videoId"
    }
}


