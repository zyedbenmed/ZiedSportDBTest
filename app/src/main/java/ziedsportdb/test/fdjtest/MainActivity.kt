package ziedsportdb.test.fdjtest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import dagger.hilt.android.AndroidEntryPoint
import ziedsportdb.test.fdjtest.features.leagues.presentation.LeagueDisplayModel
import ziedsportdb.test.fdjtest.features.leagues.presentation.LeaguesUiState
import ziedsportdb.test.fdjtest.features.leagues.presentation.MainViewModel
import ziedsportdb.test.fdjtest.features.leagues.presentation.TeamDisplayModel
import ziedsportdb.test.fdjtest.features.leagues.presentation.TeamsUiState
import ziedsportdb.test.fdjtest.ui.theme.FDJTestTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FDJTestTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(
                        modifier = Modifier.padding(paddingValues = innerPadding),
                    )
                }
            }
        }
    }
}

@Composable
private fun MainScreen(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        mainViewModel.getAllLeagues()
    }
    val leaguesUiState by mainViewModel.leaguesUiState.collectAsStateWithLifecycle()
    val searchResults by mainViewModel.searchResultsUiState.collectAsStateWithLifecycle()
    val teamsUiState by mainViewModel.teamsUiState.collectAsStateWithLifecycle()

    var isSearchActive by rememberSaveable { mutableStateOf(false) }

    when (leaguesUiState) {
        LeaguesUiState.Empty -> {
            Surface(modifier = modifier.fillMaxSize()) {
                Text(text = stringResource(id = R.string.empty_state_message))
            }
        }
        LeaguesUiState.Error -> {
            Surface(modifier = modifier.fillMaxSize()) {
                Text(text = stringResource(id = R.string.generic_error_message))
            }
        }
        LeaguesUiState.Loading -> {
            Surface(modifier = modifier.fillMaxSize()) {
                CircularProgressIndicator()
            }
        }
        is LeaguesUiState.Ready -> {
            Column(
                modifier = modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                SearchScreen(
                    modifier = modifier,
                    searchQuery = mainViewModel.searchQuery,
                    searchResults = searchResults,
                    onSearchQueryChanged = { mainViewModel.onSearchQueryChanged(it) },
                    isSearchActive = isSearchActive,
                    onSearchActiveChanged = { isActive ->
                        isSearchActive = isActive
                    },
                    onLeagueClicked = { league ->
                        isSearchActive = false
                        mainViewModel.onLeagueClicked(leagueName = league.name)
                    },
                )
                Spacer(modifier = Modifier.height(10.dp))
                when (val state = teamsUiState) {
                    TeamsUiState.Empty,
                    TeamsUiState.Error,
                    TeamsUiState.Idle,
                    TeamsUiState.Loading,
                    -> Unit
                    is TeamsUiState.Ready -> {
                        Teams(teamsDisplayModel = state.teamsDisplayModel)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchScreen(
    modifier: Modifier,
    searchQuery: String,
    searchResults: LeaguesUiState,
    onSearchQueryChanged: (searchQuery: String) -> Unit,
    isSearchActive: Boolean,
    onSearchActiveChanged: (isActive: Boolean) -> Unit,
    onLeagueClicked: (league: LeagueDisplayModel) -> Unit,
) {
    SearchBar(
        query = searchQuery,
        onQueryChange = {
            onSearchQueryChanged(it)
        },
        onSearch = {},
        placeholder = {
            Text(text = stringResource(id = R.string.search_bar_placeholder))
        },
        active = isSearchActive,
        onActiveChange = onSearchActiveChanged,
        content = {
            when (searchResults) {
                LeaguesUiState.Error -> Unit
                LeaguesUiState.Empty -> {
                    Text(text = stringResource(id = R.string.empty_state_query))
                }
                LeaguesUiState.Loading -> {
                    CircularProgressIndicator()
                }
                is LeaguesUiState.Ready -> {
                    Leagues(
                        modifier = modifier,
                        leagues = searchResults.leaguesDisplayModel,
                        onLeagueClicked = onLeagueClicked,
                    )
                }
            }
        },
    )
}

@Composable
private fun Leagues(
    modifier: Modifier = Modifier,
    leagues: List<LeagueDisplayModel>,
    onLeagueClicked: (league: LeagueDisplayModel) -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = 20.dp),
    ) {
        items(leagues) { league ->
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 10.dp)
                    .clickable {
                        onLeagueClicked(league)
                    },
                text = league.name,
            )
        }
    }
}

@Composable
private fun Teams(
    teamsDisplayModel: List<TeamDisplayModel>,
) {
    LazyVerticalGrid(
        modifier = Modifier.padding(horizontal = 20.dp),
        columns = GridCells.Fixed(2),
        content = {
            items(teamsDisplayModel) { team ->
                Column(
                    modifier = Modifier.padding(20.dp),
                ) {
                    AsyncImage(
                        model = team.badge,
                        contentDescription = team.teamName,
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = team.teamName,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
fun LeaguePreview() {
    FDJTestTheme {
        Leagues(
            leagues = listOf(
                LeagueDisplayModel(idLeague = "1", name = "Ligue 1"),
                LeagueDisplayModel(idLeague = "2", name = "Ligue 2"),
            ),
            onLeagueClicked = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TeamsPreview() {
    FDJTestTheme {
        Teams(
            teamsDisplayModel = listOf(
                TeamDisplayModel(
                    teamName = "Gent",
                    badge = "",
                ),
                TeamDisplayModel(
                    teamName = "Gent",
                    badge = "",
                ),
                TeamDisplayModel(
                    teamName = "Gent",
                    badge = "",
                ),
                TeamDisplayModel(
                    teamName = "Gent",
                    badge = "",
                ),
            ),
        )
    }
}
