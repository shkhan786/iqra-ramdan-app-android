package com.shkhan.iqra.ui.model

data class UserPhaseFlags(
    val sehriDoneEarly: Boolean = false,
    val sehriAlarmStopped: Boolean = false,
    val iftarAlarmStopped: Boolean = false,
    val fastCompleted: Boolean = false
)