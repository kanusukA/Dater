package com.example.dater.devTools

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkQuery
import com.example.dater.Data.Journey.domain.repository.JourneyRepository
import com.example.dater.Data.Reminder.domain.repository.ReminderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class DevToolsViewModel @Inject constructor(
    private val reminderRepository: ReminderRepository,
    private val journeyRepository: JourneyRepository,
    private val application: Application

    ) : ViewModel() {

    val reminders = reminderRepository.getReminders().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )
    val journeys = journeyRepository.getJourney().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )

    private val work = WorkManager.getInstance(application.applicationContext)

    private val _selectedState: MutableStateFlow<DevToolState> = MutableStateFlow(DevToolState.Journeys)
    val selectedState: StateFlow<DevToolState> = _selectedState

    private val workState: MutableStateFlow<WorkStatus> =  MutableStateFlow(WorkStatus.ENQUEUE)

    private val _workStatus: MutableStateFlow<String> =  MutableStateFlow("")
    val workStatus: StateFlow<String> = _workStatus

    private val _workStatusList: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    val workStatusList: StateFlow<List<String>> = _workStatusList

    fun changeState(state: DevToolState){
        _selectedState.update { state }
    }

    fun changeWorkType(workStatus: WorkStatus){
        when(workStatus){
            WorkStatus.RUNNING -> {
                try {
                    _workStatus.update { work.getWorkInfos(WorkQuery.fromStates(WorkInfo.State.RUNNING)).get().size.toString() }

                }catch (e : Exception){
                    println("Dev Tool Error")
                }
            }
            WorkStatus.CANCELED -> {
                try {
                    _workStatus.update { work.getWorkInfos(WorkQuery.fromStates(WorkInfo.State.CANCELLED)).get().size.toString() }
                }catch (e : Exception){
                    println("Dev Tool Error")
                }
            }
            WorkStatus.FAILED -> {
                try {
                    _workStatus.update { work.getWorkInfos(WorkQuery.fromStates(WorkInfo.State.FAILED)).get().size.toString() }
                }catch (e : Exception){
                    println("Dev Tool Error")
                }
            }
            WorkStatus.COMPLETED -> {
                try {
                    _workStatus.update { work.getWorkInfos(WorkQuery.fromStates(WorkInfo.State.SUCCEEDED)).get().size.toString() }
                }catch (e : Exception){
                    println("Dev Tool Error")
                }
            }
            WorkStatus.ENQUEUE -> {
                try {
                    _workStatus.update { work.getWorkInfos(WorkQuery.fromStates(WorkInfo.State.ENQUEUED)).get().size.toString() }
                }catch (e : Exception){
                    println("Dev Tool Error")
                }
            }
        }
    }
}

enum class WorkStatus{
    RUNNING,
    CANCELED,
    FAILED,
    COMPLETED,
    ENQUEUE
}

sealed class DevToolState(){
    object Journeys: DevToolState()
    object Reminder: DevToolState()
    object Work: DevToolState()
}