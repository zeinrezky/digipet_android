package com.stellkey.android.view.child.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.stellkey.android.R
import com.stellkey.android.databinding.DialogKidVideoBinding


class BasicKidVideoDialog(val onCloseClickListener: () -> Unit = {}) : DialogFragment(),
    SurfaceHolder.Callback,
    MediaPlayer.OnPreparedListener {
    private lateinit var dataBinding: DialogKidVideoBinding

    private val mediaPlayer = MediaPlayer()
    private val packageName = "com.stellkey.android"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_kid_video, container, false)
        dialog?.let {
            it.window?.requestFeature(Window.FEATURE_NO_TITLE)
            it.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initMediaPlayer()
        initVideoPlayerListener()
    }

    private fun initMediaPlayer() {
        mediaPlayer.setOnPreparedListener(this)
        dataBinding.vvPlayerVideo.holder.addCallback(this)
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.start()
        }
    }

    private fun initVideoPlayerListener() {
        dataBinding.ivDoneBtn.setOnClickListener {
            if (!mediaPlayer.isPlaying) {
                mediaPlayer.start()
            }
        }

        dataBinding.ivClose.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.stop()
            }
            onCloseClickListener.invoke()
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        dialog ?: return
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog?.window?.setLayout(width, height)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        mediaPlayer.apply {
            setDataSource(
                requireContext().applicationContext,
                Uri.parse("android.resource://$packageName/raw/" + R.raw.introduction_video)
            )

            setDisplay(holder)
            prepareAsync()
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

    override fun surfaceDestroyed(holder: SurfaceHolder) {}

    override fun onPrepared(mp: MediaPlayer?) {}

    companion object {
        const val TAG = "BasicKidVideoDialog"
    }
}