package com.example.template.ux.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.template.ext.stateInDefault
import com.example.template.model.datastore.AppPreferenceDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel
@Inject constructor(
    private val prefs: AppPreferenceDataSource,
) : ViewModel() {

    private val previousQueriesFlow = prefs.previousQueriesFlow.stateInDefault(viewModelScope, emptyList())
    private val queryTextFlow = MutableStateFlow("")
    private val searchTextFlow = MutableStateFlow("")

    private val filteredListFlow = searchTextFlow.map { searchText ->
        Log.i("SMT", "filteredListFlow($searchText)")
        randomNames.filter { it.contains(searchText) }
    }.stateInDefault(viewModelScope, emptyList())

    private val suggestedListFlow = combine(queryTextFlow, previousQueriesFlow) { queryText, previousQueries ->
        Log.i("SMT", "suggestedListFlow($queryText, ${previousQueries})")
        if (queryText.isBlank()) return@combine previousQueries
        previousQueries.filter { it.contains(queryText) }
    }.stateInDefault(viewModelScope, emptyList())

    val uiState = SearchUiState(
        filteredListFlow = filteredListFlow,
        queryTextFlow = queryTextFlow,
        suggestionListFlow = suggestedListFlow,
        onQueryChange = { queryTextFlow.value = it },
        onSearch = ::onSearch
    )

    private fun onSearch(searchQuery: String) {
        Log.i("SMT", "onSearch($searchQuery)")
        viewModelScope.launch {
            val mutableList = previousQueriesFlow.value.toMutableList()
            if (searchQuery.isNotBlank() && !mutableList.contains(searchQuery)) {
                mutableList.add(searchQuery)
                prefs.setPreviousQueries(mutableList)
            }
        }
        searchTextFlow.value = searchQuery
    }

    companion object {
        private val randomNames = listOf(
            "Emily Johnson",
            "Alexander Martinez",
            "Olivia Thompson",
            "Ethan Davis",
            "Sophia Wilson",
            "Liam Anderson",
            "Ava Hernandez",
            "Noah Miller",
            "Mia Taylor",
            "William Brown",
            "Isabella Jackson",
            "James White",
            "Charlotte Garcia",
            "Benjamin Smith",
            "Amelia Lee",
            "Oliver Thomas",
            "Harper Martin",
            "Elijah Rodriguez",
            "Evelyn Martinez",
            "Lucas Johnson",
            "Scarlett Davis",
            "Henry Taylor",
            "Grace Wilson",
            "Samuel Anderson",
            "Avery Hernandez",
            "Daniel Miller",
            "Sofia Brown",
            "Michael Jackson",
            "Lily Garcia",
            "Aiden Smith",
            "Abigail Lee",
            "David Thomas",
            "Victoria Martin",
            "Joseph Robinson",
            "Chloe Rodriguez",
            "Jackson Johnson",
            "Ella Davis",
            "Sebastian Taylor",
            "Emily Wilson",
            "Gabriel Anderson",
            "Elizabeth Hernandez",
            "Carter Miller",
            "Addison Brown",
            "John Jackson",
            "Natalie Garcia",
            "Andrew Smith",
            "Zoey Lee",
            "Matthew Thomas",
            "Lillian Martin",
            "Christopher Robinson"
        )
    }
}