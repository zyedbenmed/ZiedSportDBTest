package ziedsportdb.test.fdjtest.features.leagues.presentation

sealed interface TeamsUiState {
    data object Idle : TeamsUiState
    data object Loading : TeamsUiState
    data object Error : TeamsUiState
    data object Empty : TeamsUiState
    data class Ready(val teamsDisplayModel: List<TeamDisplayModel>) : TeamsUiState
}

data class TeamDisplayModel(
    val teamName: String,
    val badge: String,
)
