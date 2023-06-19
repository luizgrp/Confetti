package dev.johnoreilly.confetti.wear.home

import dev.johnoreilly.confetti.utils.QueryResult
import dev.johnoreilly.confetti.wear.bookmarks.BookmarksUiState
import dev.johnoreilly.confetti.wear.ui.model.ConferenceDayUiModel


data class HomeUiState(
    val conference: String,
    val conferenceName: String,
    val confDates: List<ConferenceDayUiModel>,
    val bookmarksUiState: QueryResult<BookmarksUiState>
)
