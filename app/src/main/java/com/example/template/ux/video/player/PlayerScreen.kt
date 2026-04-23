package com.example.template.ux.video.player

import android.app.PictureInPictureParams
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Rect
import android.os.Build
import android.util.Log
import android.util.Rational
import android.view.ContextThemeWrapper
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toAndroidRectF
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.PictureInPictureModeChangedInfo
import androidx.core.util.Consumer
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.cast.CastPlayer
import androidx.media3.cast.SessionAvailabilityListener
import androidx.media3.common.C
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.compose.PlayerSurface
import androidx.media3.ui.compose.material3.Player
import com.example.template.ui.composable.AppTopAppBar
import com.google.android.gms.cast.framework.CastButtonFactory
import com.google.android.gms.cast.framework.CastContext
import com.google.common.util.concurrent.MoreExecutors
import org.koin.compose.viewmodel.koinViewModel
import org.lds.mobile.navigation3.navigator.Navigation3Navigator

@Composable
@Suppress("CyclomaticComplexMethod", "LongMethod")
fun PlayerScreen(
    navigator: Navigation3Navigator,
    viewModel: PlayerViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val playList by viewModel.mediaItemsFlow.collectAsStateWithLifecycle(initialValue = null)

    var exoPlayer by remember { mutableStateOf<ExoPlayer?>(null) }
    var activePlayer by remember { mutableStateOf<Player?>(null) }
    val isInPipMode by rememberIsInPipMode()

    LaunchedEffect(playList) {
        val currentPlayList = playList ?: return@LaunchedEffect

        if (exoPlayer == null) {
            val localPlayer = ExoPlayer.Builder(context).build()

            // Try to initialize CastContext (will fail if Google Play Services aren't available)
            var currentCastPlayer: CastPlayer? = null
            try {
                val castContextResult = CastContext.getSharedInstance(context, MoreExecutors.directExecutor())
                castContextResult.addOnSuccessListener { castContext ->
                    currentCastPlayer = CastPlayer(castContext)

                    currentCastPlayer.setSessionAvailabilityListener(object : SessionAvailabilityListener {
                        override fun onCastSessionAvailable() {
                            activePlayer = currentCastPlayer
                            // Transfer state to cast player
                            transferState(localPlayer, currentCastPlayer, currentPlayList)
                        }

                        override fun onCastSessionUnavailable() {
                            activePlayer = localPlayer
                            // Transfer state to local player
                            transferState(currentCastPlayer, localPlayer, currentPlayList)
                        }
                    })

                    if (currentCastPlayer.isCastSessionAvailable) {
                        activePlayer = currentCastPlayer
                        setupPlayer(currentCastPlayer, currentPlayList)
                    } else {
                        activePlayer = localPlayer
                        setupPlayer(localPlayer, currentPlayList)
                    }
                }
            } catch (@Suppress("TooGenericExceptionCaught") e: Exception) {
                // Ignore, no cast framework available
                Log.d("PlayerScreen", "CastContext not available: ${e.message}")
                activePlayer = localPlayer
                setupPlayer(localPlayer, currentPlayList)
            }

            exoPlayer = localPlayer
            if (activePlayer == null) {
                activePlayer = localPlayer
                setupPlayer(localPlayer, currentPlayList)
            }
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE, Lifecycle.Event.ON_STOP -> {
                    if (context.findActivity()?.isInPictureInPictureMode != true) {
                        // We do not pause the cast player when activity stops
                        if (activePlayer == exoPlayer) {
                            activePlayer?.pause()
                        }
                    }
                }

                Lifecycle.Event.ON_DESTROY -> {
                    exoPlayer?.release()
                    exoPlayer = null
                    activePlayer = null
                }

                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            exoPlayer?.release()
            // Do not release castPlayer so casting can continue in the background
            // castPlayer?.release()
            activePlayer = null
        }
    }

    BackHandler {
        if (viewModel.supportsPip && activePlayer?.isPlaying == true && activePlayer == exoPlayer) {
            enterPictureInPicture(context)
        } else {
            navigator.pop()
        }
    }

    var isPlaying by remember { mutableStateOf(false) }

    DisposableEffect(activePlayer) {
        val listener = object : Player.Listener {
            override fun onIsPlayingChanged(isPlayingState: Boolean) {
                isPlaying = isPlayingState
            }
        }
        activePlayer?.addListener(listener)
        isPlaying = activePlayer?.isPlaying == true
        onDispose {
            activePlayer?.removeListener(listener)
        }
    }

    val shouldEnterPipMode = viewModel.supportsPip && isPlaying && activePlayer == exoPlayer

    val pipModifier = Modifier.onGloballyPositioned { layoutCoordinates ->
        val activity = context.findActivity() ?: return@onGloballyPositioned
        val builder = PictureInPictureParams.Builder()

        if (shouldEnterPipMode) {
            val sourceRectF = layoutCoordinates.boundsInWindow().toAndroidRectF()
            val sourceRect = Rect(
                sourceRectF.left.toInt(),
                sourceRectF.top.toInt(),
                sourceRectF.right.toInt(),
                sourceRectF.bottom.toInt()
            )
            builder.setSourceRectHint(sourceRect)
            builder.setAspectRatio(Rational(16, 9))
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            builder.setAutoEnterEnabled(shouldEnterPipMode)
        }

        try {
            activity.setPictureInPictureParams(builder.build())
        } catch (@Suppress("TooGenericExceptionCaught") e: Exception) {
            // Ignore PIP params setting failures
            Log.w("PlayerScreen", "Failed to set PIP params: ${e.message}", e)
        }
    }

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
        DisposableEffect(context, shouldEnterPipMode) {
            val activity = context.findActivity()
            val onUserLeaveBehavior = Runnable {
                if (shouldEnterPipMode) {
                    activity?.enterPictureInPictureMode(
                        PictureInPictureParams.Builder().setAspectRatio(Rational(16, 9)).build()
                    )
                }
            }
            activity?.addOnUserLeaveHintListener(onUserLeaveBehavior)
            onDispose {
                activity?.removeOnUserLeaveHintListener(onUserLeaveBehavior)
            }
        }
    }

    if (isInPipMode) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .then(pipModifier)
        ) {
            activePlayer?.let { player ->
                PlayerSurface(
                    player = player,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    } else {
        Scaffold(
            topBar = {
                AppTopAppBar(
                    title = "Player",
                    onBack = { navigator.pop() },
                    actions = {
                        AndroidView(factory = { ctx ->
                            // Wrap context with Material theme to provide a non-transparent background
                            // This prevents IllegalArgumentException from MediaRouterThemeHelper
                            val themedContext = ContextThemeWrapper(ctx, com.example.template.R.style.AppTheme)
                            androidx.mediarouter.app.MediaRouteButton(themedContext).apply {
                                CastButtonFactory.setUpMediaRouteButton(context, this)
                            }
                        })
                    }
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .then(pipModifier)
            ) {
                activePlayer?.let { player ->
                    Player(
                        player = player,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

private fun setupPlayer(player: Player?, playList: PlayList?) {
    if (player == null || playList == null) return
    val mediaItem = playList.mediaItems.find { it.mediaId == playList.videoId.value }
    val startingIndex = if (mediaItem != null) playList.mediaItems.indexOf(mediaItem) else 0

    player.setMediaItems(playList.mediaItems, startingIndex, 0)
    player.prepare()
    player.playWhenReady = true
}

private fun transferState(from: Player?, to: Player?, playList: PlayList?) {
    if (from == null || to == null || playList == null) return

    var playbackPositionMs = C.TIME_UNSET
    var currentItemIndex = C.INDEX_UNSET
    var playWhenReady = false

    val playbackState = from.playbackState
    if (playbackState != Player.STATE_ENDED) {
        playbackPositionMs = from.currentPosition
        playWhenReady = from.playWhenReady
        currentItemIndex = from.currentMediaItemIndex
    }
    from.stop()
    from.clearMediaItems()

    to.setMediaItems(playList.mediaItems, currentItemIndex, playbackPositionMs)
    to.playWhenReady = playWhenReady
    to.prepare()
}

@Composable
fun rememberIsInPipMode(): State<Boolean> {
    val context = LocalContext.current
    val activity = context.findActivity()
    val pipMode = remember { mutableStateOf(activity?.isInPictureInPictureMode ?: false) }

    DisposableEffect(activity) {
        val observer = Consumer<PictureInPictureModeChangedInfo> { info ->
            pipMode.value = info.isInPictureInPictureMode
        }
        activity?.addOnPictureInPictureModeChangedListener(observer)
        onDispose {
            activity?.removeOnPictureInPictureModeChangedListener(observer)
        }
    }
    return pipMode
}

private fun enterPictureInPicture(context: Context) {
    val activity = context.findActivity() ?: return
    val params = PictureInPictureParams.Builder()
        .setAspectRatio(Rational(16, 9))
        .build()
    activity.enterPictureInPictureMode(params)
}

internal fun Context.findActivity(): ComponentActivity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is ComponentActivity) return context
        context = context.baseContext
    }
    return null
}
