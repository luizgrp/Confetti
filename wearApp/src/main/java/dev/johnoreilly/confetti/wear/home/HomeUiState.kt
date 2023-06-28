package dev.johnoreilly.confetti.wear.home

import dev.johnoreilly.confetti.utils.QueryResult
import dev.johnoreilly.confetti.wear.bookmarks.BookmarksUiState
import kotlinx.datetime.LocalDate


data class HomeUiState(
    val conference: String,
    val conferenceName: String,
    val confDates: List<LocalDate>,
    val bookmarksUiState: QueryResult<BookmarksUiState>
)
