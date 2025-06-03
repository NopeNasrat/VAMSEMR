package com.example.vamsemr.core

import android.content.Context
import android.media.SoundPool
import com.example.vamsemr.R

object SoundManager {
    private var soundPool: SoundPool? = null
    private var tickSoundId: Int = 0
    private var loaded = false

    fun init(context: Context) {
        soundPool = SoundPool.Builder().setMaxStreams(1).build()
        tickSoundId = soundPool!!.load(context, R.raw.tick_tak, 1)
        loaded = true
    }

    fun playTick() {
        if (loaded) {
            soundPool?.play(tickSoundId, 1f, 1f, 1, 0, 1f)
        }
    }

    fun release() {
        soundPool?.release()
        soundPool = null
    }
}
