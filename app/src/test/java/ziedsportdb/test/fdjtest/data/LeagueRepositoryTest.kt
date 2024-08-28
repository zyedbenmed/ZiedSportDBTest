package ziedsportdb.test.fdjtest.data

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import ziedsportdb.test.fdjtest.core.FDJResult
import ziedsportdb.test.fdjtest.features.leagues.data.datahelpers.DataSourceDecisionMaker
import ziedsportdb.test.fdjtest.features.leagues.data.datasource.local.LeagueLocalDataSource
import ziedsportdb.test.fdjtest.features.leagues.data.datasource.remote.LeagueRemoteDataSource
import ziedsportdb.test.fdjtest.features.leagues.data.dto.LeagueResponse
import ziedsportdb.test.fdjtest.features.leagues.data.dto.LeaguesResponse
import ziedsportdb.test.fdjtest.features.leagues.data.entity.LeagueEntity
import ziedsportdb.test.fdjtest.features.leagues.data.repository.LeagueRepositoryImpl
import ziedsportdb.test.fdjtest.features.leagues.domain.model.LeagueModel
import ziedsportdb.test.fdjtest.features.leagues.domain.repository.LeagueRepository

class LeagueRepositoryTest {

    private val leagueRemoteDataSource: LeagueRemoteDataSource = mockk()
    private val leagueLocalDataSource: LeagueLocalDataSource = mockk()
    private val dataSourceDecisionMaker: DataSourceDecisionMaker = mockk()

    private val leagueRepositoryImpl: LeagueRepository = LeagueRepositoryImpl(
        leagueRemoteDataSource = leagueRemoteDataSource,
        leagueLocalDataSource = leagueLocalDataSource,
        dataSourceDecisionMaker = dataSourceDecisionMaker,
    )

    @Before
    fun before() {
        MockKAnnotations.init(this)
    }

    // Data for testing the repository
    private val localDbList = listOf(
        LeagueEntity("1", "ligue1", "foot"),
        LeagueEntity("2", "ligue2", "foot"),
    )

    private val remoteList = listOf(
        LeagueResponse("1", "ligue1", "foot", ""),
        LeagueResponse("2", "ligue2", "foot", ""),
    )

    private val remoteNullResponse = LeaguesResponse(leagues = null)

    private val remoteResponseWithData = LeaguesResponse(leagues = remoteList)

    private val serverException = Exception("Internal Server Error")

    @Test
    fun `given shouldFetchFromRemote is true and remote throws exception, when get leagues, then local db will not be updated`() {
        coEvery { dataSourceDecisionMaker.shouldFetchLeaguesFromRemote() } returns true
        coEvery { leagueRemoteDataSource.getLeagues() } throws serverException

        runBlocking {
            leagueRepositoryImpl.getLeagues()
        }

        coVerify { leagueRemoteDataSource.getLeagues() }

        coVerify(inverse = true) { leagueLocalDataSource.deleteAll() }
        coVerify(inverse = true) { leagueLocalDataSource.insertAllLeagues(any()) }
    }

    @Test
    fun `given shouldFetchFromRemote is true and remote throws exception, when get leagues, then get data from local db`() {
        coEvery { dataSourceDecisionMaker.shouldFetchLeaguesFromRemote() } returns true
        coEvery { leagueRemoteDataSource.getLeagues() } throws serverException
        coEvery { leagueLocalDataSource.getAllLeagues() } returns localDbList

        var actualResult: List<LeagueModel>? = null
        runBlocking {
            leagueRepositoryImpl.getLeagues().collect {
                actualResult = (it as FDJResult.Success).data
            }
        }

        Assert.assertEquals(localDbList.map { it.mapToDomainModel() }, actualResult)
    }

    @Test
    fun `given shouldFetchFromRemote is true and remote return null, when get leagues, then local db will not be updated`() {
        coEvery { dataSourceDecisionMaker.shouldFetchLeaguesFromRemote() } returns true
        coEvery { leagueRemoteDataSource.getLeagues() } returns remoteNullResponse

        runBlocking {
            leagueRepositoryImpl.getLeagues()
        }

        coVerify(inverse = true) { leagueLocalDataSource.deleteAll() }
        coVerify(inverse = true) { leagueLocalDataSource.insertAllLeagues(any()) }
    }

    @Test
    fun `given shouldFetchFromRemote is true, when get leagues, then local db will be updated`() {
        coEvery { dataSourceDecisionMaker.shouldFetchLeaguesFromRemote() } returns true
        coEvery { leagueRemoteDataSource.getLeagues() } returns remoteResponseWithData
        coEvery { leagueLocalDataSource.deleteAll() } returns Unit
        coEvery { leagueLocalDataSource.insertAllLeagues(any()) } returns Unit

        runBlocking {
            leagueRepositoryImpl.getLeagues()
        }

        coVerify { leagueRemoteDataSource.getLeagues() }
        coVerify(exactly = 1) { leagueLocalDataSource.deleteAll() }
        coVerify(exactly = 1) { leagueLocalDataSource.insertAllLeagues(any()) }
    }

    @Test
    fun `given shouldFetchFromRemote is false, when get leagues, then get data from local db without calling remote`() {
        coEvery { dataSourceDecisionMaker.shouldFetchLeaguesFromRemote() } returns false
        coEvery { leagueLocalDataSource.getAllLeagues() } returns localDbList

        var actualResult: List<LeagueModel>? = null
        runBlocking {
            leagueRepositoryImpl.getLeagues().collect {
                actualResult = (it as FDJResult.Success).data
            }
        }

        coVerify(inverse = true) { leagueRemoteDataSource.getLeagues() }
        coVerify { leagueLocalDataSource.getAllLeagues() }
        Assert.assertEquals(localDbList.map { it.mapToDomainModel() }, actualResult)
    }
}
