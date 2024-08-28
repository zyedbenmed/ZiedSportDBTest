package ziedsportdb.test.fdjtest.features.leagues.presentation

sealed interface LeaguesUiState {
    data object Loading : LeaguesUiState
    data object Error : LeaguesUiState
    data object Empty : LeaguesUiState
    data class Ready(val leaguesDisplayModel: List<LeagueDisplayModel>) : LeaguesUiState
}

data class LeagueDisplayModel(
    val idLeague: String,
    val name: String,
)
