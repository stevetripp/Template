package com.example.template.ux.video.activity

import android.app.PictureInPictureParams
import android.content.Intent
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
import androidx.media3.datasource.cache.Cache
import androidx.media3.ui.PlayerView
import com.example.template.R
import com.example.template.databinding.ActivityVideoBinding
import com.example.template.util.SmtLogger
import com.example.template.ux.video.NextTrack
import com.example.template.ux.video.PlayerManager
import com.example.template.ux.video.VideoId
import com.google.android.gms.cast.framework.CastButtonFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import org.lds.mobile.ext.withLifecycleOwner
import javax.inject.Inject

/**
 * Manifest Dependency: The following attribute must be placed on the <Activity> to prevent the activity from being destroyed during rotation.
 *
 * android:configChanges="orientation|keyboardHidden|screenSize|screenLayout|smallestScreenSize"
 */
@AndroidEntryPoint
open class VideoActivity : AppCompatActivity() {

    @Inject
    lateinit var downloadCache: Cache

    private var playerManager: PlayerManager? = null
    val viewModel by viewModels<VideoActivityViewModel>()
    private var currentPlaybackPosition: Long = PlayerManager.INITIAL_PLAYBACK_POSITION
    private val viewBinding by lazy(LazyThreadSafetyMode.NONE) { ActivityVideoBinding.inflate(layoutInflater) }
    private val nextTrack = MutableStateFlow<NextTrack?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpComposeOverlay()
        setContentView(viewBinding.root)
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
        viewBinding.videoToolbar.apply {
            setNavigationIcon(R.drawable.ic_arrow_back_24)
            setNavigationOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
            inflateMenu(R.menu.menu_video)
            CastButtonFactory.setUpMediaRouteButton(this@VideoActivity, menu, R.id.media_route_menu_item)
        }

        viewBinding.videoView.setControllerVisibilityListener(
            PlayerView.ControllerVisibilityListener {
                viewBinding.videoToolbar.visibility = it
            }
        )
    }

    private fun collectFlows() {
        withLifecycleOwner(this) {
            viewModel.mediaPlayerItemsFlow.collectWhenStarted { videoItems ->
                SmtLogger.i(
                    """mediaPlayerItemsFlow.collectWhenStarted
                    |videoItems: $videoItems
                """.trimMargin()
                )
                videoItems?.let {
                    val videoId = videoItems.first().id
                    playerManager?.setMediaItems(videoItems, videoId)
                }
            }
        }
    }

    public override fun onStart() {
        super.onStart()
        viewModel.setPipState(false)
        if (playerManager != null) return
        playerManager = PlayerManager(
            activity = this,
            playerView = viewBinding.videoView,
            viewModel = viewModel,
            downloadCache = downloadCache,
            playbackPosition = ::setOrGetPlaybackPosition,
            isPlayingChanged = ::onIsPlayingChanged
        ).also {
            withLifecycleOwner(this) {
                it.getSecondsToNextTrackFlow().collectLatestWhenStarted {
                    nextTrack.value = it
                }
            }
        }
    }

    private fun setOrGetPlaybackPosition(position: Long?, savePlaybackPosition: Boolean, videoId: VideoId?): Long {
        position?.let {
            currentPlaybackPosition = position
            if (savePlaybackPosition) videoId?.let { viewModel.saveVideoPosition(videoId, position, playerManager?.currentPlayer?.duration ?: 0) }
        }
        return currentPlaybackPosition
    }

    private fun onIsPlayingChanged(isPlaying: Boolean) {
        if (isPlaying) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    public override fun onResume() {
        super.onResume()
        hideSystemUi()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        if (hasFocus) hideSystemUi()
        super.onWindowFocusChanged(hasFocus)
    }

    public override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    private fun releasePlayer() {
        playerManager?.release()
        playerManager = null
    }

    private fun hideSystemUi() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, viewBinding.videoView).let { controller ->
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
        viewModel.setPipState(isInPictureInPictureMode)
        viewBinding.videoView.useController = !isInPictureInPictureMode

        if (lifecycle.currentState == Lifecycle.State.CREATED) {
            // User closed PIP
            releasePlayer()
            finish()
        }
    }

    private fun enterPictureInPicture(): Boolean {
        var enteredToPip = false

        if (viewModel.supportsPip && playerManager?.isPlaying == true && playerManager?.isCasting == false) {
            viewBinding.videoView.hideController()
            val pipParams = PictureInPictureParams.Builder().setAspectRatio(Rational(16, 9)).build()
            enteredToPip = enterPictureInPictureMode(pipParams)
        }
        viewModel.setPipState(enteredToPip)
        return enteredToPip
    }

    private fun addBackPressedCallback() {
        onBackPressedDispatcher.addCallback(this) {
            if (!enterPictureInPicture()) finish()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val newVideoId = intent.extras?.getString(VideoActivityRoute.Arg.VIDEO_ID)
        currentPlaybackPosition = PlayerManager.INITIAL_PLAYBACK_POSITION
        viewModel.setVideoId(newVideoId)
    }
}
