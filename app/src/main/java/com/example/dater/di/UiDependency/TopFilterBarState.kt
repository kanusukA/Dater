package com.example.dater.di.UiDependency

import com.example.dater.ui.components.TopFilterBar.TopFilterBarSearchState
import com.example.dater.ui.components.TopFilterBar.TopFilterBarType
import com.example.dater.ui.components.TopFilterBar.util.TopFilterBarJourneyType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TopFilterBarState @Inject constructor() {

    private val _topFilterBarState: MutableStateFlow<TopFilterBarType> =
        MutableStateFlow(TopFilterBarType.JourneyType(TopFilterBarJourneyType.Ascending))
    val state: StateFlow<TopFilterBarType> = _topFilterBarState

    private val _topFilterBarSearchState: MutableStateFlow<TopFilterBarSearchState> =
        MutableStateFlow(TopFilterBarSearchState(false,""))
    val searchState: StateFlow<TopFilterBarSearchState> = _topFilterBarSearchState

    fun changeState(topFilterBarState: TopFilterBarType){
        _topFilterBarState.update { topFilterBarState }
    }

    fun changeSearchState(boolean: Boolean){
        _topFilterBarSearchState.update { currentState -> currentState.copy(showState = boolean) }
    }

    fun changeSearchText(string: String){
        _topFilterBarSearchState.update { currentState -> currentState.copy(searchText = string) }

    }

}