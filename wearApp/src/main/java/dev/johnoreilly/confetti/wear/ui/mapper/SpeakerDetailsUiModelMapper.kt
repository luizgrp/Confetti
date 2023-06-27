package dev.johnoreilly.confetti.wear.ui.mapper

import dev.johnoreilly.confetti.fragment.SessionDetails
import dev.johnoreilly.confetti.wear.ui.model.SpeakerDetailsUiModel

fun SessionDetails.Speaker.toSpeakerDetailsUiModel(): SpeakerDetailsUiModel =
    SpeakerDetailsUiModel(
        id = id,
        name = speakerDetails.name
    )
