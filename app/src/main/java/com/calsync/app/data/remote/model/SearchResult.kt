package com.calsync.app.data.remote.model
data class SearchResult(
    val events: List<EventDto>,
    val tasks: List<TaskDto>
)
