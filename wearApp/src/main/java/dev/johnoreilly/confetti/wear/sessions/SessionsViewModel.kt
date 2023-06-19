package dev.johnoreilly.confetti.wear.sessions

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.johnoreilly.confetti.ConfettiRepository
import dev.johnoreilly.confetti.GetSessionsQuery
import dev.johnoreilly.confetti.navigation.ConferenceDayKey
import dev.johnoreilly.confetti.toTimeZone
import dev.johnoreilly.confetti.utils.ClientQuery.toUiState
import dev.johnoreilly.confetti.utils.QueryResult
import dev.johnoreilly.confetti.utils.nowAtTimeZone
import dev.johnoreilly.confetti.wear.sessions.navigation.SessionsDestination
import dev.johnoreilly.confetti.wear.ui.mapper.toSessionDetailsUiModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.LocalDateTime
import java.util.TreeMap

class SessionsViewModel(
    savedStateHandle: SavedStateHandle,
    repository: ConfettiRepository,
) : ViewModel() {
    private val conferenceDay: ConferenceDayKey =
        SessionsDestination.fromNavArgs(savedStateHandle)

    val uiState: StateFlow<QueryResult<SessionsUiState>> =
        repository.sessionsQuery(conferenceDay.conference)
            .toUiState {
                buildUiState(it)
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), QueryResult.Loading)

    private fun buildUiState(data: GetSessionsQuery.Data): SessionsUiState {
        val sessionList = data.sessions.nodes.map { it.sessionDetails }
        val now = LocalDateTime.nowAtTimeZone(data.config.timezone.toTimeZone())
        val sessions = sessionList.filter {
            it.startsAt.date == conferenceDay.date
        }.map { it.toSessionDetailsUiModel(now) }
        val sessionsByTime =
            sessions.groupByTo(TreeMap()) { it.startsAt }.map {
                SessionAtTime(it.key, it.value)
            }
        return SessionsUiState(
            conferenceDay,
            sessionsByTime,
            now
        )
    }
}
