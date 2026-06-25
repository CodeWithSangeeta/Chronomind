package com.sangeeta.chronomind.ui.activities

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sangeeta.chronomind.repository.ActivityRepository
import com.sangeeta.chronomind.service.TimerForegroundService
import com.sangeeta.chronomind.ui.mapper.toUiModel
import com.sangeeta.chronomind.ui.model.ActivityUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface AllActivitiesEvent {
    data object NavigateToHome : AllActivitiesEvent
}

@HiltViewModel
class AllActivitiesViewModel @Inject constructor(
    private val activityRepository: ActivityRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val searchQuery = MutableStateFlow("")
    private val sortOption = MutableStateFlow(ActivitySortOption.RECENTLY_USED)

    private val _events = Channel<AllActivitiesEvent>(Channel.BUFFERED)
    val events: Flow<AllActivitiesEvent> = _events.receiveAsFlow()

    val uiState: StateFlow<AllActivitiesUiState> = combine(
        activityRepository.observeAll(),
        searchQuery,
        sortOption
    ) { entities, query, sort ->
        val all = entities.map { it.toUiModel() }
        val filtered = all
            .filter { activity ->
                query.isBlank() || activity.name.contains(query.trim(), ignoreCase = true)
            }
            .let { list ->
                when (sort) {
                    ActivitySortOption.RECENTLY_USED  -> list.sortedByDescending { lastUsedWeight(it.lastActiveDate) }
                    ActivitySortOption.RECENTLY_ADDED -> list.sortedByDescending { it.id }
                    ActivitySortOption.A_TO_Z         -> list.sortedBy { it.name.lowercase() }
                }
            }
        
        AllActivitiesUiState(
            isLoading = false,
            activities = all,
            filteredActivities = filtered,
            searchQuery = query,
            selectedSort = sort,
            isEmpty = all.isEmpty(),
            isSearchEmpty = all.isNotEmpty() && filtered.isEmpty()
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AllActivitiesUiState(isLoading = true)
    )

    fun onSearchQueryChange(query: String) { searchQuery.value = query }

    fun onSortSelected(option: ActivitySortOption) { sortOption.value = option }

    fun onDeleteActivity(activityId: Int) {
        viewModelScope.launch {
            val entity = activityRepository.observeById(activityId).firstOrNull() ?: return@launch
            activityRepository.delete(entity)
        }
    }

    fun onPlayClick(activityId: Int) {
        viewModelScope.launch {
            val running = activityRepository.observeRunning().firstOrNull()
            if (running != null && running.id != activityId) {
                activityRepository.pauseActivity(running)
                context.startService(TimerForegroundService.pauseIntent(context))
            }
            val target = activityRepository.observeById(activityId).firstOrNull() ?: return@launch
            activityRepository.startActivity(target)
            context.startService(TimerForegroundService.startIntent(context))
            _events.send(AllActivitiesEvent.NavigateToHome)
        }
    }

    private fun lastUsedWeight(label: String): Int = when (label.trim().lowercase()) {
        "today"     -> 100
        "yesterday" -> 90
        else        -> 0
    }
}
