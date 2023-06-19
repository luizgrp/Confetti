@file:OptIn(ExperimentalCoroutinesApi::class)

package dev.johnoreilly.confetti.wear.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.cache.normalized.FetchPolicy
import dev.johnoreilly.confetti.ConfettiRepository
import dev.johnoreilly.confetti.GetConferenceDataQuery
import dev.johnoreilly.confetti.auth.Authentication
import dev.johnoreilly.confetti.utils.ClientQuery.toUiState
import dev.johnoreilly.confetti.utils.QueryResult
import dev.johnoreilly.confetti.wear.bookmarks.BookmarksUiState
import dev.johnoreilly.confetti.wear.bookmarks.BookmarksViewModel.Companion.toUiState
import dev.johnoreilly.confetti.wear.home.navigation.ConferenceHomeDestination
import dev.johnoreilly.confetti.wear.ui.model.ConferenceDayUiModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(
    savedStateHandle: SavedStateHandle,
    repository: ConfettiRepository,
    authentication: Authentication,
) : ViewModel() {
    private val conference: String =
        ConferenceHomeDestination.fromNavArgs(savedStateHandle)

    private val bookmarksUiState: StateFlow<QueryResult<BookmarksUiState>> =
        bookmarksUiStateFlow(authentication, repository, conference)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), QueryResult.Loading)

    val uiState: StateFlow<QueryResult<HomeUiState>> =
        homeUiStateFlow(repository, conference, bookmarksUiState)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), QueryResult.Loading)

    companion object {
        fun homeUiStateFlow(
            repository: ConfettiRepository, conference: String,
            bookmarksUiStateFlow: StateFlow<QueryResult<BookmarksUiState>>
        ) = repository.conferenceHomeData(conference).toUiState {
            it.toUiState()
        }.combine(bookmarksUiStateFlow) { conferenceHomeData, bookmarksUiState ->
            if (conferenceHomeData is QueryResult.Success) {
                QueryResult.Success(conferenceHomeData.result.copy(bookmarksUiState = bookmarksUiState))
            } else {
                conferenceHomeData
            }
        }


        fun bookmarksUiStateFlow(
            authentication: Authentication,
            repository: ConfettiRepository,
            conference: String
        ) =
            authentication.currentUser.flatMapLatest { user ->
                if (user != null) {
                    repository.bookmarkedSessionsQuery(
                        conference,
                        user.uid,
                        user,
                        FetchPolicy.CacheFirst
                    ).toUiState {
                        it.toUiState()
                    }
                } else {
                    flowOf(QueryResult.None)
                }
            }

        private fun GetConferenceDataQuery.Data.toUiState() =
            HomeUiState(
                config.id,
                config.name,
                config.days.map { ConferenceDayUiModel(config.id, it) },
                QueryResult.None
            )
    }
}

