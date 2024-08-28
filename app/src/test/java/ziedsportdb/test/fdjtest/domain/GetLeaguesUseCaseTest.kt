package ziedsportdb.test.fdjtest.domain

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import ziedsportdb.test.fdjtest.core.FDJResult
import ziedsportdb.test.fdjtest.features.leagues.domain.model.LeagueModel
import ziedsportdb.test.fdjtest.features.leagues.domain.repository.LeagueRepository
import ziedsportdb.test.fdjtest.features.leagues.domain.usecase.GetLeaguesUseCase

@OptIn(ExperimentalCoroutinesApi::class)
class GetLeaguesUseCaseTest {

    private val leagueRepository: LeagueRepository = mockk()

    private val testDispatcher = UnconfinedTestDispatcher()

    private val getLeaguesUseCase: GetLeaguesUseCase =
        GetLeaguesUseCase(
            leagueRepository = leagueRepository,
            defaultDispatcher = testDispatcher,
        )

    @Before
    fun before() {
        MockKAnnotations.init()
    }

    @Test
    fun `given result is Failure, when call getLeagues, then must emit the error`() {
        val exceptedErrorMessage = "Error Message"

        coEvery { leagueRepository.getLeagues() } returns flow {
            emit(
                FDJResult.Failure(Throwable(exceptedErrorMessage)),
            )
        }

        var actualErrorMessage: String? = null
        runBlocking {
            getLeaguesUseCase.invoke().collect {
                actualErrorMessage = (it as FDJResult.Failure<*>).error?.message
            }
        }

        coVerify { leagueRepository.getLeagues() }
        Assert.assertEquals(exceptedErrorMessage, actualErrorMessage)
    }

    @Test
    fun `given result is Success, when call getLeagues, then must emit sorted list by name`() {
        val exceptedList = listOf(
            LeagueModel("1", "Zambia ligue 1", "football"),
            LeagueModel("2", "Austria ligue 1", "football"),
        )

        coEvery { leagueRepository.getLeagues() } returns flow {
            emit(
                FDJResult.Success(exceptedList),
            )
        }

        var actualList = listOf<LeagueModel>()
        runBlocking {
            getLeaguesUseCase.invoke().collect {
                actualList = ((it as FDJResult.Success<*>).data) as List<LeagueModel>
            }
        }

        coVerify { leagueRepository.getLeagues() }
        Assert.assertEquals(exceptedList.sortedBy { it.name }, actualList)
    }
}
