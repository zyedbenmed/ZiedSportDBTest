package ziedsportdb.test.fdjtest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import ziedsportdb.test.fdjtest.features.leagues.presentation.LeagueDisplayModel
import ziedsportdb.test.fdjtest.features.leagues.presentation.LeaguesUiState
import ziedsportdb.test.fdjtest.features.leagues.presentation.MainViewModel
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
fun MainScreen(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        mainViewModel.getAllLeagues()
    }
    val leaguesUiState by mainViewModel.leagues.collectAsStateWithLifecycle()
    val searchResults by mainViewModel.searchResults.collectAsStateWithLifecycle()

    when (leaguesUiState) {
        LeaguesUiState.Empty -> {
            Surface(modifier = modifier.fillMaxSize()) {
                Text(text = "Empty Data Set")
            }
        }

        LeaguesUiState.Error -> {
            Surface(modifier = modifier.fillMaxSize()) {
                Text(text = "Error Message")
            }
        }

        LeaguesUiState.Loading -> {
            Surface(modifier = modifier.fillMaxSize()) {
                CircularProgressIndicator()
            }
        }

        is LeaguesUiState.Ready -> {
            SearchScreen(
                modifier = modifier,
                searchQuery = mainViewModel.searchQuery,
                searchResults = searchResults,
                onSearchQueryChanged = { mainViewModel.onSearchQueryChanged(it) },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    modifier: Modifier,
    searchQuery: String,
    searchResults: LeaguesUiState,
    onSearchQueryChanged: (searchQuery: String) -> Unit,
) {
    SearchBar(
        query = searchQuery,
        onQueryChange = {
            onSearchQueryChanged(it)
        },
        onSearch = {},
        placeholder = {
            Text(text = "Search by league")
        },
        active = true,
        onActiveChange = {},
        content = {
            when (searchResults) {
                LeaguesUiState.Empty -> {
                    Text(text = "No matching name")
                }
                LeaguesUiState.Error -> Unit
                LeaguesUiState.Loading -> {
                    CircularProgressIndicator()
                }
                is LeaguesUiState.Ready -> {
                    Leagues(
                        modifier = modifier,
                        leagues = searchResults.leaguesDisplayModel,
                    )
                }
            }
        },
    )
}

@Composable
fun Leagues(
    modifier: Modifier = Modifier,
    leagues: List<LeagueDisplayModel>,
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
                        // TODO call second ws
                    },
                text = league.name,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FDJTestTheme {
    }
}
