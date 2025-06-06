package com.example.vamsemr.core

import android.content.Context
import android.media.SoundPool
import com.example.vamsemr.R



/**
 *
 *  SoundManager na prehravanie zvukov
 *
 * @author Bc. Fabo Peter
 */
object SoundManager {
    private var soundPool: SoundPool? = null
    private var tickSoundId: Int = 0
    private var loaded = false

    /**
     * Inicializacia soundmanageru
     */
    fun init(context: Context) {
        soundPool = SoundPool.Builder().setMaxStreams(1).build()
        tickSoundId = soundPool!!.load(context, R.raw.tick_tak, 1)
        loaded = true
    }

    /**
     * Prehrat Zvuk
     */
    fun playTick() {
        if (loaded) {
            soundPool?.play(tickSoundId, 1f, 1f, 1, 0, 1f)
        }
    }

    /**
     * Uvolnit pamät
     */
    fun release() {
        soundPool?.release()
        soundPool = null
    }
}
