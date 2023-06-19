package dev.johnoreilly.confetti.wear.ui.model

import kotlinx.datetime.LocalDateTime

data class SessionDetailsUiModel(
    val id: String,
    val title: String,
    val room: String?,
    val isBreak: Boolean,
    val startsAt: LocalDateTime,
    val endsAt: LocalDateTime,
    val speakers: List<SpeakerDetailsUiModel>,
    val bookmarkNow: LocalDateTime
)
