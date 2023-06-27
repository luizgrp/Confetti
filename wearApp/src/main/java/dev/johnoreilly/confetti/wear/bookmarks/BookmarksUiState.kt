package dev.johnoreilly.confetti.wear.bookmarks

import dev.johnoreilly.confetti.wear.ui.model.SessionDetailsUiModel
import kotlinx.datetime.LocalDateTime

data class BookmarksUiState(
    val conference: String,
    val upcoming: List<SessionDetailsUiModel>,
    val past: List<SessionDetailsUiModel>,
    val now: LocalDateTime
) {
    val hasUpcomingBookmarks: Boolean
        get() = upcoming.isNotEmpty()
}
