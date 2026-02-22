package com.shkhan.iqra.engine

import com.shkhan.iqra.ui.model.*

class HeroPhaseEngine {

    fun calculatePhase(
        currentTime: Long,
        sehriTime: Long,
        fajrTime: Long,
        dhuhrTime: Long,
        asrTime: Long,
        maghribTime: Long
    ): HeroPhase {

        return when {

            currentTime < sehriTime - (60 * 60 * 1000) ->
                HeroPhase.NIGHT_PRE_SEHRI

            currentTime < sehriTime ->
                HeroPhase.SEHRI_WINDOW

            currentTime in sehriTime until (sehriTime + 60_000) ->
                HeroPhase.SEHRI_ALARM

            currentTime < maghribTime ->
                HeroPhase.FASTING_DAY

            currentTime in maghribTime until (maghribTime + 60_000) ->
                HeroPhase.IFTAR_ALARM

            currentTime > maghribTime ->
                HeroPhase.NIGHT_AFTER_IFTAR

            else -> HeroPhase.FASTING_DAY
        }
    }
}