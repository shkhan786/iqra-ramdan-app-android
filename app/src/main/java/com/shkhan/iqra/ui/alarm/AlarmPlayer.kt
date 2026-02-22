package com.shkhan.iqra.ui.alarm

import android.content.Context
import android.media.MediaPlayer
import com.shkhan.iqra.R

object AlarmPlayer {

    private var mediaPlayer: MediaPlayer? = null

    fun play(context: Context) {

        if (mediaPlayer != null) return

        mediaPlayer = MediaPlayer.create(context, R.raw.sehri_alarm)
        mediaPlayer?.isLooping = true
        mediaPlayer?.start()
    }

    fun stop() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying == true
    }
}