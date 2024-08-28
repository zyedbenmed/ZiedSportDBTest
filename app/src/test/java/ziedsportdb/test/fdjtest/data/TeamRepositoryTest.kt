package ziedsportdb.test.fdjtest.data

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import ziedsportdb.test.fdjtest.core.FDJResult
import ziedsportdb.test.fdjtest.features.leagues.data.datahelpers.DataSourceDecisionMaker
import ziedsportdb.test.fdjtest.features.leagues.data.datasource.local.TeamLocalDataSource
import ziedsportdb.test.fdjtest.features.leagues.data.datasource.remote.TeamRemoteDataSource
import ziedsportdb.test.fdjtest.features.leagues.data.dto.TeamResponse
import ziedsportdb.test.fdjtest.features.leagues.data.dto.TeamsResponse
import ziedsportdb.test.fdjtest.features.leagues.data.entity.TeamEntity
import ziedsportdb.test.fdjtest.features.leagues.data.repository.TeamRepositoryImpl
import ziedsportdb.test.fdjtest.features.leagues.domain.model.TeamModel
import ziedsportdb.test.fdjtest.features.leagues.domain.repository.TeamRepository

class TeamRepositoryTest {

    private val teamRemoteDataSource: TeamRemoteDataSource = mockk()
    private val teamLocalDataSource: TeamLocalDataSource = mockk()
    private val dataSourceDecisionMaker: DataSourceDecisionMaker = mockk()

    private val teamRepositoryImpl: TeamRepository = TeamRepositoryImpl(
        teamRemoteDataSource = teamRemoteDataSource,
        teamLocalDataSource = teamLocalDataSource,
        dataSourceDecisionMaker = dataSourceDecisionMaker,
    )

    @Before
    fun before() {
        MockKAnnotations.init(this)
    }

    private val localDbList = listOf(
        TeamEntity(teamId = "1", teamName = "PSG", leagueName = "Ligue 1"),
        TeamEntity(teamId = "2", teamName = "Lille", leagueName = "ligue 1"),
    )

    private val remoteList = listOf(
        TeamResponse(idTeam = "1", strTeam = "PSG", strLeague = "ligue 1"),
        TeamResponse(idTeam = "2", strTeam = "Lille", strLeague = "ligue 1"),
    )

    private val remoteNullResponse = TeamsResponse(teams = null)

    private val remoteResponseWithData = TeamsResponse(teams = remoteList)

    private val serverException = Exception("Internal Server Error")

    @Test
    fun `given shouldFetchFromRemote is true and remote throws exception, when get teams, then local db will not be updated`() {
        coEvery { dataSourceDecisionMaker.shouldFetchTeamsFromRemote(any()) } returns true
        coEvery { teamRemoteDataSource.getTeamsByLeague(any()) } throws serverException

        runBlocking {
            teamRepositoryImpl.getTeamsByLeague(anyString())
        }

        coVerify { teamRemoteDataSource.getTeamsByLeague(any()) }
        coVerify(inverse = true) { teamLocalDataSource.insertTeams(any()) }
    }

    @Test
    fun `given shouldFetchFromRemote is true and remote throws exception, when get teams, then get data from local db`() {
        coEvery { dataSourceDecisionMaker.shouldFetchTeamsFromRemote(any()) } returns true
        coEvery { teamRemoteDataSource.getTeamsByLeague(any()) } throws serverException
        coEvery { teamLocalDataSource.getAllTeamsByLeague(any()) } returns localDbList

        var actualResult: List<TeamModel>? = null
        runBlocking {
            teamRepositoryImpl.getTeamsByLeague(anyString()).collect {
                actualResult = (it as FDJResult.Success).data
            }
        }

        Assert.assertEquals(localDbList.map { it.mapToDomainModel() }, actualResult)
    }

    @Test
    fun `given shouldFetchFromRemote is true and remote return null, when get teams, then local db will not be updated`() {
        coEvery { dataSourceDecisionMaker.shouldFetchTeamsFromRemote(any()) } returns true
        coEvery { teamRemoteDataSource.getTeamsByLeague(any()) } returns remoteNullResponse

        runBlocking {
            teamRepositoryImpl.getTeamsByLeague(anyString())
        }

        coVerify(inverse = true) { teamLocalDataSource.insertTeams(any()) }
    }

    @Test
    fun `given shouldFetchFromRemote is true, when get teams, then local db will be updated`() {
        coEvery { dataSourceDecisionMaker.shouldFetchTeamsFromRemote(any()) } returns true
        coEvery { teamRemoteDataSource.getTeamsByLeague(any()) } returns remoteResponseWithData
        coEvery { teamLocalDataSource.insertTeams(any()) } returns Unit

        runBlocking {
            teamRepositoryImpl.getTeamsByLeague(anyString())
        }

        coVerify { teamRemoteDataSource.getTeamsByLeague(any()) }
        coVerify(exactly = 1) { teamLocalDataSource.insertTeams(any()) }
    }

    @Test
    fun `given shouldFetchFromRemote is false, when get teams, then get data from local db without calling remote`() {
        coEvery { dataSourceDecisionMaker.shouldFetchTeamsFromRemote(any()) } returns false
        coEvery { teamLocalDataSource.getAllTeamsByLeague(any()) } returns localDbList

        var actualResult: List<TeamModel>? = null
        runBlocking {
            teamRepositoryImpl.getTeamsByLeague(anyString()).collect {
                actualResult = (it as FDJResult.Success).data
            }
        }

        coVerify(inverse = true) { teamRemoteDataSource.getTeamsByLeague(any()) }
        coVerify { teamLocalDataSource.getAllTeamsByLeague(any()) }
        Assert.assertEquals(localDbList.map { it.mapToDomainModel() }, actualResult)
    }
}
