//package com.sangeeta.chronomind.ui.activities
//
//
//import androidx.lifecycle.ViewModel
//import com.sangeeta.chronomind.repository.ActivityRepository
//import com.sangeeta.chronomind.ui.model.ActivitySortOption
//import com.sangeeta.chronomind.ui.model.ActivityUiModel
//import com.sangeeta.chronomind.ui.model.AllActivitiesUiState
//import dagger.hilt.android.lifecycle.HiltViewModel
//import javax.inject.Inject
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.update
//
//@HiltViewModel
//class AllActivitiesViewModel @Inject constructor(
//    private val activityRepository: ActivityRepository
//) : ViewModel() {
//
//    private val _uiState = MutableStateFlow(
//        AllActivitiesUiState(
//            isLoading = false,
//            activities = mockActivities()
//        )
//    )
//    val uiState: StateFlow<AllActivitiesUiState> = _uiState.asStateFlow()
//
//    init {
//        applyFilters()
//    }
//
//    fun onSearchQueryChange(query: String) {
//        _uiState.update { it.copy(searchQuery = query) }
//        applyFilters()
//    }
//
//    fun onSortSelected(sortOption: ActivitySortOption) {
//        _uiState.update { it.copy(selectedSort = sortOption) }
//        applyFilters()
//    }
//
//    fun onDeleteActivity(activityId: Int) {
//        _uiState.update { current ->
//            val updated = current.activities.filterNot { it.id == activityId }
//            current.copy(activities = updated)
//        }
//        applyFilters()
//    }
//
//    fun onStartActivity(activityId: Int) {
//        // UI-only phase. Wire real timer start later.
//    }
//
//    private fun applyFilters() {
//        _uiState.update { current ->
//            val filtered = current.activities
//                .filter { activity ->
//                    current.searchQuery.isBlank() ||
//                            activity.name.contains(current.searchQuery.trim(), ignoreCase = true)
//                }
//                .let { list ->
//                    when (current.selectedSort) {
//                        ActivitySortOption.RECENTLY_USED -> {
//                            list.sortedByDescending { sortWeightForLastUsed(it.lastActiveDate) }
//                        }
//                        ActivitySortOption.RECENTLY_ADDED -> {
//                            list.sortedByDescending { it.id }
//                        }
//                        ActivitySortOption.A_TO_Z -> {
//                            list.sortedBy { it.name.lowercase() }
//                        }
//                    }
//                }
//
//            current.copy(filteredActivities = filtered)
//        }
//    }
//
//    private fun sortWeightForLastUsed(lastUsed: String): Int {
//        return when (lastUsed.lowercase()) {
//            "today" -> 4
//            "yesterday" -> 3
//            "2 days ago" -> 2
//            "3 days ago" -> 1
//            else -> 0
//        }
//    }
//
//    private fun mockActivities(): List<ActivityUiModel> {
//        return listOf(
//            ActivityUiModel(
//                id = 1,
//                name = "Creative Projects",
//                icon = "🎨",
//                colorHex = "#FFC328",
//                elapsedSeconds = 0L,
//                targetSeconds = 90 * 60L,
//                isRunning = false,
//                streakDays = 4,
//                lastActiveDate = "Today",
//                continueOnMiss = true
//            ),
//            ActivityUiModel(
//                id = 2,
//                name = "Study & Learning",
//                icon = "📘",
//                colorHex = "#2196F3",
//                elapsedSeconds = 0L,
//                targetSeconds = 60 * 60L,
//                isRunning = false,
//                streakDays = 7,
//                lastActiveDate = "Today",
//                continueOnMiss = true
//            ),
//            ActivityUiModel(
//                id = 3,
//                name = "Coding Practice",
//                icon = "💻",
//                colorHex = "#4CAF50",
//                elapsedSeconds = 0L,
//                targetSeconds = 105 * 60L,
//                isRunning = false,
//                streakDays = 10,
//                lastActiveDate = "Yesterday",
//                continueOnMiss = true
//            ),
//            ActivityUiModel(
//                id = 4,
//                name = "Workout",
//                icon = "🏋️",
//                colorHex = "#8E55EA",
//                elapsedSeconds = 0L,
//                targetSeconds = 60 * 60L,
//                isRunning = false,
//                streakDays = 5,
//                lastActiveDate = "2 days ago",
//                continueOnMiss = true
//            ),
//            ActivityUiModel(
//                id = 5,
//                name = "Meditation",
//                icon = "🧘",
//                colorHex = "#F57C00",
//                elapsedSeconds = 0L,
//                targetSeconds = 30 * 60L,
//                isRunning = false,
//                streakDays = 9,
//                lastActiveDate = "2 days ago",
//                continueOnMiss = true
//            ),
//            ActivityUiModel(
//                id = 6,
//                name = "Writing & Journaling",
//                icon = "✍️",
//                colorHex = "#00ACC1",
//                elapsedSeconds = 0L,
//                targetSeconds = 45 * 60L,
//                isRunning = false,
//                streakDays = 6,
//                lastActiveDate = "3 days ago",
//                continueOnMiss = true
//            ),
//            ActivityUiModel(
//                id = 7,
//                name = "Marketing Work",
//                icon = "📊",
//                colorHex = "#F4511E",
//                elapsedSeconds = 0L,
//                targetSeconds = 120 * 60L,
//                isRunning = false,
//                streakDays = 3,
//                lastActiveDate = "3 days ago",
//                continueOnMiss = true
//            ),
//            ActivityUiModel(
//                id = 8,
//                name = "Client Work",
//                icon = "🗂️",
//                colorHex = "#7E57C2",
//                elapsedSeconds = 0L,
//                targetSeconds = 90 * 60L,
//                isRunning = false,
//                streakDays = 8,
//                lastActiveDate = "5 days ago",
//                continueOnMiss = true
//            )
//        )
//    }
//}





package com.sangeeta.chronomind.ui.activities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sangeeta.chronomind.repository.ActivityRepository
import com.sangeeta.chronomind.ui.model.ActivitySortOption
import com.sangeeta.chronomind.ui.model.AllActivitiesUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.sangeeta.chronomind.local.db.entity.ActivityEntity
import com.sangeeta.chronomind.ui.mapper.toUiModel

import com.sangeeta.chronomind.ui.model.ActivityUiModel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted

@HiltViewModel
class AllActivitiesViewModel @Inject constructor(
    private val activityRepository: ActivityRepository
) : ViewModel() {

    private val _searchQuery  = MutableStateFlow("")
    private val _sortOption   = MutableStateFlow(ActivitySortOption.RECENTLY_USED)

    val uiState: StateFlow<AllActivitiesUiState> = combine(
        activityRepository.observeAll(),
        _searchQuery,
        _sortOption
    ) { entities: List<ActivityEntity>, query: String, sort: ActivitySortOption ->
        val all = entities.map { entity -> entity.toUiModel() }

        val filtered = all
            .filter { activity ->
                query.isBlank() || activity.name.contains(query.trim(), ignoreCase = true)
            }
            .let { list: List<ActivityUiModel> ->
                when (sort) {
                    ActivitySortOption.RECENTLY_USED  -> {
                        list.sortedByDescending { activity ->
                            lastUsedWeight(activity.lastActiveDate)
                        }
                    }
                    ActivitySortOption.RECENTLY_ADDED -> {
                        list.sortedByDescending { activity -> activity.id }
                    }
                    ActivitySortOption.A_TO_Z -> {
                        list.sortedBy { activity -> activity.name.lowercase() }
                    }
                }
            }

        AllActivitiesUiState(
            isLoading = false,
            activities = all,
            filteredActivities = filtered,
            searchQuery = query,
            selectedSort = sort
        )
    }.stateIn(
        scope         = viewModelScope,
        started       = SharingStarted.WhileSubscribed(5_000),
        initialValue  = AllActivitiesUiState(isLoading = true)
    )

    fun onSearchQueryChange(query: String) { _searchQuery.value = query }
    fun onSortSelected(option: ActivitySortOption) { _sortOption.value = option }

    fun onDeleteActivity(activityId: Int) {
        viewModelScope.launch {
            val entity = activityRepository.observeById(activityId).firstOrNull() ?: return@launch
            activityRepository.delete(entity)
        }
    }

    fun onStartActivity(activityId: Int) {
        viewModelScope.launch {
            activityRepository.stopAll()
            val entity = activityRepository.observeById(activityId).firstOrNull() ?: return@launch
            activityRepository.updateTimer(activityId, entity.elapsedSeconds, running = true)
        }
    }

    private fun lastUsedWeight(label: String): Int = when (label.lowercase()) {
        "today"     -> 4
        "yesterday" -> 3
        else        -> 0
    }
}