package com.shkhan.iqra.ui.model

import com.shkhan.iqra.ui.model.HeroPhase

data class HeroState(
    val phase: HeroPhase,
    val title: String,
    val countdownTarget: Long?,
    val targetLabel: String,
    val targetTime: String,
    val secondaryLabel: String?,
    val secondaryTime: String?,
    val nextPrayerLabel: String,
    val nextPrayerTime: String,

    val displayMessage: String,
    val messageType: MessageType,

    val buttonType: HeroButtonType,
    val alarmActive: Boolean,

    val duaTitle: String? = null,
    val duaText: String? = null,
    val duaTranslation: String? = null
)