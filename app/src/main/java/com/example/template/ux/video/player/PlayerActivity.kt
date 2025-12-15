package com.example.template.ux.video.player

import android.app.PictureInPictureParams
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Rational
import android.view.KeyEvent
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.Lifecycle
import androidx.media3.common.MediaItem
import androidx.media3.datasource.cache.Cache
import androidx.media3.ui.PlayerView
import com.example.template.R
import com.example.template.databinding.ActivityVideoBinding
import com.example.template.util.SmtLogger
import com.example.template.ux.video.VideoId
import com.google.android.gms.cast.framework.CastButtonFactory
import com.google.android.gms.cast.framework.CastContext
import com.google.common.util.concurrent.MoreExecutors
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import org.lds.mobile.ext.withLifecycleOwner

/**
 * Manifest Dependency: The following attribute must be placed on the <Activity> to prevent the activity from being destroyed during rotation.
 *
 * android:configChanges="orientation|keyboardHidden|screenSize|screenLayout|smallestScreenSize"
 */
@AndroidEntryPoint
class PlayerActivity : AppCompatActivity(), PlayerManager.Listener {

    private val viewModel by viewModels<PlayerViewModel>()

    @Inject
    lateinit var downloadCache: Cache

    private val activityVideoBinding by lazy(LazyThreadSafetyMode.NONE) { ActivityVideoBinding.inflate(layoutInflater) }
    private lateinit var playerView: PlayerView

    // Begin demo code
    private var playerManager: PlayerManager? = null
    private var castContext: CastContext? = null
    // End demo code

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        castContext = CastContext.getSharedInstance(this, MoreExecutors.directExecutor()).result
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
//            }
//        }
    }

    private fun setupToolbar() {
        activityVideoBinding.videoToolbar.apply {
            setNavigationIcon(R.drawable.ic_arrow_back_24)
            setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
            inflateMenu(R.menu.menu_video)
            CastButtonFactory.setUpMediaRouteButton(this@PlayerActivity, menu, R.id.cast_menu_item)
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
                playMediaItem(it.videoId, it.mediaItems)
            }
        }
    }

    private fun playMediaItem(videoId: VideoId, mediaItems: List<MediaItem>) {
        val mediaItem = mediaItems.find { it.mediaId == videoId.value } ?: return
        val startingIndex = mediaItems.indexOf(mediaItem)
        // Begin demo code
        mediaItems.forEach { playerManager?.addItem(it) }
        playerManager?.setCurrentItem(startingIndex)
        // End demo code
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        if (hasFocus) hideSystemUi()
        super.onWindowFocusChanged(hasFocus)
    }

    private fun releasePlayer() {
        playerManager?.release()
        playerManager = null
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
        activityVideoBinding.videoView.useController = !isInPictureInPictureMode

        if (lifecycle.currentState == Lifecycle.State.CREATED) {
            // User closed PIP
            releasePlayer()
            finish()
        }
    }

    private fun enterPictureInPicture(): Boolean {
        var enteredToPip = false

        if (viewModel.supportsPip && playerManager?.isPlaying() == true && playerManager?.isCasting() == false) {
            activityVideoBinding.videoView.hideController()
            val pipParams = PictureInPictureParams.Builder().setAspectRatio(Rational(16, 9)).build()
            enteredToPip = enterPictureInPictureMode(pipParams)
        }
        return enteredToPip
    }

    private fun addBackPressedCallback() {
        onBackPressedDispatcher.addCallback(this) {
            if (!enterPictureInPicture()) finish()
        }
    }

    // ---- Lifecycle methods

    private fun initializePlayerManager() {
        SmtLogger.i("""initializePlayer()""")
        playerManager = PlayerManager(this, this, playerView, castContext)
    }

    public override fun onStart() {
        super.onStart()
//        viewModel.setPipState(false)
        SmtLogger.i("""onStart()""")
        initializePlayerManager()
    }

    override fun onPause() {
        super.onPause()
        // Begin demo code
        castContext ?: return
//        playerManager?.release()
//        playerManager = null
        // End demo code
    }

    public override fun onResume() {
        super.onResume()
        hideSystemUi()
    }

    public override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    // Begin demo code

    override fun onQueuePositionChanged(previousIndex: Int, newIndex: Int) {
        SmtLogger.i("""onQueuePositionChanged($previousIndex, $newIndex)""")
    }

    override fun onUnsupportedTrack(trackType: Int) {
        SmtLogger.i("""onUnsupportedTrack($trackType)""")
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        return super.dispatchKeyEvent(event) || (playerManager?.dispatchKeyEvent(event) ?: false)
    }

    // End demo code

    companion object {
        fun launch(context: Context, videoId: VideoId) {
            val intent = Intent(context, PlayerActivity::class.java).apply {
                putExtra(PlayerRouteArgs.VIDEO_ID, videoId.value)
            }
            context.startActivity(intent)
        }
    }
}
