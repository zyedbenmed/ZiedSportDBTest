package ziedsportdb.test.fdjtest.features.leagues.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ziedsportdb.test.fdjtest.core.FDJResult
import ziedsportdb.test.fdjtest.di.IoDispatcher
import ziedsportdb.test.fdjtest.features.leagues.domain.usecase.GetLeaguesUseCase
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getLeaguesUseCase: GetLeaguesUseCase,
    private val leaguesDisplayModelBuilder: LeaguesDisplayModelBuilder,
    @IoDispatcher private val dispatcherIO: CoroutineDispatcher,
) : ViewModel() {

    private val _leagues = MutableStateFlow<LeaguesUiState>(LeaguesUiState.Loading)
    val leagues = _leagues.asStateFlow()

    var searchQuery by mutableStateOf("")
        private set

    val searchResults: StateFlow<LeaguesUiState> =
        snapshotFlow { searchQuery }
            .combine(leagues) { searchQuery, leagues ->
                when {
                    (searchQuery.isNotEmpty() && leagues is LeaguesUiState.Ready) -> {
                        LeaguesUiState.Ready(
                            leagues.leaguesDisplayModel.filter { league ->
                                league.name.contains(searchQuery, ignoreCase = true)
                            },
                        )
                    }

                    else -> leagues
                }
            }.stateIn(
                scope = viewModelScope,
                initialValue = LeaguesUiState.Loading,
                started = SharingStarted.WhileSubscribed(5_000),
            )

    fun getAllLeagues() {
        viewModelScope.launch(dispatcherIO) {
            getLeaguesUseCase.invoke().collect { result ->
                _leagues.value = when (result) {
                    is FDJResult.Success -> {
                        if (result.data.isEmpty()) {
                            LeaguesUiState.Empty
                        } else {
                            LeaguesUiState.Ready(
                                leaguesDisplayModel = leaguesDisplayModelBuilder.buildLeaguesDisplayModel(
                                    model = result.data,
                                ),
                            )
                        }
                    }

                    is FDJResult.Failure -> LeaguesUiState.Error
                }
            }
        }
    }

    fun onSearchQueryChanged(newQuery: String) {
        searchQuery = newQuery
    }
}
