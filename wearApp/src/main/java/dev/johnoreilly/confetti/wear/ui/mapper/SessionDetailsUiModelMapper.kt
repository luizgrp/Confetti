package dev.johnoreilly.confetti.wear.ui.mapper

import dev.johnoreilly.confetti.fragment.SessionDetails
import dev.johnoreilly.confetti.isBreak
import dev.johnoreilly.confetti.wear.ui.model.SessionDetailsUiModel

fun SessionDetails.toSessionDetailsUiModel(): SessionDetailsUiModel = SessionDetailsUiModel(
    id = id,
    title = title,
    room = room?.name,
    isBreak = this.isBreak(),
    startsAt = startsAt,
    endsAt = endsAt,
    speakers = speakers.map(SessionDetails.Speaker::toSpeakerDetailsUiModel)
)
