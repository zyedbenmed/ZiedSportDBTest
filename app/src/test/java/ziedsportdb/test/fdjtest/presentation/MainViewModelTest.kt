package ziedsportdb.test.fdjtest.presentation

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import ziedsportdb.test.fdjtest.core.FDJResult
import ziedsportdb.test.fdjtest.features.leagues.domain.model.LeagueModel
import ziedsportdb.test.fdjtest.features.leagues.domain.model.TeamModel
import ziedsportdb.test.fdjtest.features.leagues.domain.usecase.GetLeaguesUseCase
import ziedsportdb.test.fdjtest.features.leagues.domain.usecase.GetTeamsByLeagueUseCase
import ziedsportdb.test.fdjtest.features.leagues.presentation.LeagueDisplayModel
import ziedsportdb.test.fdjtest.features.leagues.presentation.LeaguesDisplayModelBuilder
import ziedsportdb.test.fdjtest.features.leagues.presentation.LeaguesUiState
import ziedsportdb.test.fdjtest.features.leagues.presentation.MainViewModel
import ziedsportdb.test.fdjtest.features.leagues.presentation.TeamDisplayModel
import ziedsportdb.test.fdjtest.features.leagues.presentation.TeamsDisplayModelBuilder
import ziedsportdb.test.fdjtest.features.leagues.presentation.TeamsUiState

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    private val getLeaguesUseCaseMock: GetLeaguesUseCase = mockk()
    private val getTeamsByLeagueUseCase: GetTeamsByLeagueUseCase = mockk()
    private val leaguesDisplayModelBuilder: LeaguesDisplayModelBuilder = mockk()
    private val teamsDisplayModelBuilder: TeamsDisplayModelBuilder = mockk()
    private val testDispatcher = UnconfinedTestDispatcher()

    private val mainViewModel: MainViewModel =
        MainViewModel(
            getLeaguesUseCase = getLeaguesUseCaseMock,
            getTeamsByLeagueUseCase = getTeamsByLeagueUseCase,
            leaguesDisplayModelBuilder = leaguesDisplayModelBuilder,
            teamsDisplayModelBuilder = teamsDisplayModelBuilder,
            dispatcherIO = testDispatcher,
        )

    private val errorMessage = "Error Message"

    @Before
    fun before() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `given result is Failure, when get leagues, then error uiState must equal error`() {
        coEvery { getLeaguesUseCaseMock.invoke() } returns flow {
            emit(FDJResult.Failure(Throwable(errorMessage)))
        }

        mainViewModel.getAllLeagues()

        coVerify { getLeaguesUseCaseMock.invoke() }
        Assert.assertEquals(LeaguesUiState.Error, mainViewModel.leaguesUiState.value)
    }

    @Test
    fun `given result is Success and list is empty, when get leagues, then uiState must equal Empty`() {
        val leaguesList = listOf<LeagueModel>()

        coEvery { getLeaguesUseCaseMock.invoke() } returns flow {
            emit(FDJResult.Success(leaguesList))
        }

        mainViewModel.getAllLeagues()

        coVerify { getLeaguesUseCaseMock.invoke() }
        Assert.assertEquals(LeaguesUiState.Empty, mainViewModel.leaguesUiState.value)
    }

    @Test
    fun `given result is Success and list empty, when get leagues, then uiState must equal Ready`() {
        val leaguesList = listOf(LeagueModel())
        val leaguesDisplayModel = listOf(LeagueDisplayModel(idLeague = "", name = ""))

        coEvery { getLeaguesUseCaseMock.invoke() } returns flow {
            emit(FDJResult.Success(leaguesList))
        }
        coEvery { leaguesDisplayModelBuilder.buildLeaguesDisplayModel(leaguesList) }.returns(leaguesDisplayModel)

        mainViewModel.getAllLeagues()

        testDispatcher.scheduler.advanceUntilIdle()

        Assert.assertEquals(
            LeaguesUiState.Ready(leaguesDisplayModel = leaguesDisplayModel),
            mainViewModel.leaguesUiState.value,
        )
    }

    @Test
    fun `given result is Failure, when onLeagueClicked, then error uiState must equal error`() {
        coEvery { getTeamsByLeagueUseCase.invoke(any()) } returns flow {
            emit(FDJResult.Failure(Throwable(errorMessage)))
        }

        mainViewModel.onLeagueClicked(anyString())

        coVerify { getTeamsByLeagueUseCase.invoke(any()) }
        Assert.assertEquals(TeamsUiState.Error, mainViewModel.teamsUiState.value)
    }

    @Test
    fun `given result is Success and list empty, when onLeagueClicked, then error uiState must equal Empty`() {
        val teamList = listOf<TeamModel>()

        coEvery { getTeamsByLeagueUseCase.invoke(any()) } returns flow {
            emit(FDJResult.Success(teamList))
        }

        mainViewModel.onLeagueClicked(anyString())

        coVerify { getTeamsByLeagueUseCase.invoke(any()) }
        Assert.assertEquals(TeamsUiState.Empty, mainViewModel.teamsUiState.value)
    }

    @Test
    fun `given result is Success and list has items, when onLeagueClicked, then uiState must equal Ready`() {
        val teamList = listOf(TeamModel())
        val teamDisplayModel = listOf(TeamDisplayModel(teamName = "", badge = ""))

        coEvery { getTeamsByLeagueUseCase.invoke(any()) } returns flow {
            emit(FDJResult.Success(teamList))
        }
        coEvery { teamsDisplayModelBuilder.buildTeamsDisplayModel(teamList) }.returns(teamDisplayModel)

        mainViewModel.onLeagueClicked(anyString())

        Assert.assertEquals(
            TeamsUiState.Ready(teamsDisplayModel = teamDisplayModel),
            mainViewModel.teamsUiState.value,
        )
        Assert.assertEquals("", mainViewModel.searchQuery)
    }

    @Test
    fun `given a new query, when onSearchQueryChanged, then searchQuery is updated`() {
        val newQuery = "Test Query"

        Assert.assertEquals("", mainViewModel.searchQuery)

        mainViewModel.onSearchQueryChanged(newQuery)

        Assert.assertEquals(newQuery, mainViewModel.searchQuery)
    }

    @Test
    fun `given initial state, searchResultsUiState should be Loading`() {
        Assert.assertEquals(LeaguesUiState.Loading, mainViewModel.searchResultsUiState.value)
    }
}
