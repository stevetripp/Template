package com.example.template.ux.video.player

import android.app.PictureInPictureParams
import android.content.res.Configuration
import android.os.Bundle
import android.util.Rational
import android.view.WindowManager
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.Lifecycle
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.cache.Cache
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.template.R
import com.example.template.databinding.ActivityVideoBinding
import com.example.template.util.SmtLogger
import com.example.template.ux.video.CastOwner
import com.example.template.ux.video.VideoId
import com.google.android.gms.cast.framework.CastButtonFactory
import dagger.hilt.android.AndroidEntryPoint
import org.lds.mobile.ext.withLifecycleOwner
import javax.inject.Inject

/**
 * Manifest Dependency: The following attribute must be placed on the <Activity> to prevent the activity from being destroyed during rotation.
 *
 * android:configChanges="orientation|keyboardHidden|screenSize|screenLayout|smallestScreenSize"
 */
@AndroidEntryPoint
class PlayerActivity : AppCompatActivity(), Player.Listener {

    private val viewModel by viewModels<PlayerViewModel>()

    @Inject
    lateinit var downloadCache: Cache

    //    private var playerManager: PlayerManager? = null
//    private var currentPlaybackPosition: Long = PlayerManager.INITIAL_PLAYBACK_POSITION
    private val activityVideoBinding by lazy(LazyThreadSafetyMode.NONE) { ActivityVideoBinding.inflate(layoutInflater) }
    private var player: ExoPlayer? = null
    private var currentMediaItemIndex = 0L
    private lateinit var playerView: PlayerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityVideoBinding.root)
        playerView = activityVideoBinding.videoView
        setUpComposeOverlay()
        setupToolbar()
        collectFlows()
        addBackPressedCallback()
    }

    private fun setUpComposeOverlay() {
//        viewBinding.composeOverlay.setContent {
//            AppTheme {
//                val nextTrack by nextTrack.collectAsStateWithLifecycle()
//                val isInPip by viewModel.isInPip.collectAsStateWithLifecycle()
//                nextTrack?.let {
//                    if (it.secondsUntil <= 10) {
//                        Box(modifier = Modifier.fillMaxSize()) {
//                            NextUpInComponent(
//                                modifier = Modifier
//                                    .align(Alignment.BottomEnd)
//                                    .padding(bottom = 32.dp, end = 32.dp),
//                                countDown = it.secondsUntil,
//                                imageRenditions = it.nextTrackImageRenditions,
//                                title = it.nextTrackTitle,
//                                isInPip = isInPip,
//                                onClick = { playerManager?.skipToNextTrack() }
//                            )
//                        }
//                    }
//                }
//            }
//        }
    }

    private fun setupToolbar() {
        activityVideoBinding.videoToolbar.apply {
            setNavigationIcon(R.drawable.ic_arrow_back_24)
            setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
            inflateMenu(R.menu.menu_video)
            CastButtonFactory.setUpMediaRouteButton(this@PlayerActivity, menu, R.id.media_route_menu_item)
        }

        activityVideoBinding.videoView.setControllerVisibilityListener(
            PlayerView.ControllerVisibilityListener {
                activityVideoBinding.videoToolbar.visibility = it
            }
        )
    }

    private fun collectFlows() {
        withLifecycleOwner(this) {
            viewModel.mediaItemsFlow.collectWhenStarted {
                setMediaItems(it.mediaItems, it.videoId)
            }
        }
    }

    private fun setMediaItems(mediaItems: List<MediaItem>, videoId: VideoId) {
        SmtLogger.i("""setMediaItems($mediaItems, ${videoId.value})""")
        val mediaItem = mediaItems.find { it.mediaId == videoId.value } ?: return
        // if (mediaItem.mediaId == currentMediaPlayerItemId) return

//        setOrGetPlaybackPosition(0)
//        currentMediaPlayerItemId = mediaItem?.mediaId
//        currentMediaPlayerPlayList = mediaItems
//        if (isCastSessionAvailable && castPlayerManager.lastCastSessionOpenedBy == CastOwner.PlayerManager && castPlayerManager.currentCastedItemId == mediaItem?.mediaId) return
        playMediaItem(videoId, mediaItems)
    }

    private fun playMediaItem(videoId: VideoId, mediaItems: List<MediaItem>) {
        if (true) {
            val mediaItem = mediaItems.find { it.mediaId == videoId.value } ?: return
            val startingIndex = mediaItems.indexOf(mediaItem)
            SmtLogger.i(
                """playMediaItem(${videoId.value}, $mediaItems
                |mediaItem: $mediaItem
                |player: $player
                |startingIndex: $startingIndex
            """.trimMargin()
            )
            player?.apply {
                setMediaItems(mediaItems)
                playWhenReady = true
                seekTo(startingIndex, 0L)
                prepare()
            }
        } else {
            viewModel.castPlayerManager.playOnCast(videoId, mediaItems, setOrGetPlaybackPosition(null), CastOwner.PlayerManager)
        }
    }

    private fun setOrGetPlaybackPosition(position: Long?): Long = position ?: 0L


    override fun onWindowFocusChanged(hasFocus: Boolean) {
        if (hasFocus) hideSystemUi()
        super.onWindowFocusChanged(hasFocus)
    }


    private fun releasePlayer() {
        player?.release()
        player = null
    }

    private fun hideSystemUi() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, activityVideoBinding.videoView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        enterPictureInPicture()
    }

    override fun onPictureInPictureModeChanged(isInPictureInPictureMode: Boolean, newConfig: Configuration) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
//        viewModel.setPipState(isInPictureInPictureMode)
        activityVideoBinding.videoView.useController = !isInPictureInPictureMode

        if (lifecycle.currentState == Lifecycle.State.CREATED) {
            // User closed PIP
            releasePlayer()
            finish()
        }
    }

    private fun enterPictureInPicture(): Boolean {
        var enteredToPip = false

        if (viewModel.supportsPip && player?.isPlaying == true /*&& player?.isCasting == false*/) {
            activityVideoBinding.videoView.hideController()
            val pipParams = PictureInPictureParams.Builder().setAspectRatio(Rational(16, 9)).build()
            enteredToPip = enterPictureInPictureMode(pipParams)
        }
//        viewModel.setPipState(enteredToPip)
        return enteredToPip
    }

    private fun addBackPressedCallback() {
        onBackPressedDispatcher.addCallback(this) {
            if (!enterPictureInPicture()) finish()
        }
    }

//    override fun onNewIntent(intent: Intent) {
//        super.onNewIntent(intent)
//        SmtLogger.i("""onNewIntent($intent)""")
//        val newVideoId = intent.extras?.getString(PlayerRoute.Arg.VIDEO_ID)
////        currentPlaybackPosition = PlayerManager.INITIAL_PLAYBACK_POSITION
//        viewModel.setVideoId(newVideoId)
//    }

    // ---- Lifecycle methods

    private fun initializePlayer() {
        // Add support for downloaded content
        val httpDataSourceFactory = DefaultHttpDataSource.Factory()
        val cacheDataSourceFactory: DataSource.Factory = CacheDataSource.Factory()
            .setCache(downloadCache)
            .setUpstreamDataSourceFactory(httpDataSourceFactory)
            .setCacheWriteDataSinkFactory(null) // Disable writing.

        // Create player
        player = ExoPlayer.Builder(this)
//            .setMediaSourceFactory(DefaultMediaSourceFactory(cacheDataSourceFactory))
            .build()
            .also { exoPlayer ->
                playerView.player = exoPlayer
                exoPlayer.addListener(this)
            }
    }

    public override fun onStart() {
        super.onStart()
//        viewModel.setPipState(false)
        SmtLogger.i("""onStart()""")
        initializePlayer()
    }

    public override fun onResume() {
        super.onResume()
        hideSystemUi()
    }

    public override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    // ---- Player.Listener overrides ----

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        SmtLogger.i("""onIsPlayingChanged($isPlaying)""")
        if (isPlaying) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    override fun onEvents(player: Player, events: Player.Events) {
        super.onEvents(player, events)
    }
}
