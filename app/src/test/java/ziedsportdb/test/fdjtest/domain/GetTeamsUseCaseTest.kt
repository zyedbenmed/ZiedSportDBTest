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
import org.mockito.ArgumentMatchers.anyString
import ziedsportdb.test.fdjtest.core.FDJResult
import ziedsportdb.test.fdjtest.features.leagues.domain.model.TeamModel
import ziedsportdb.test.fdjtest.features.leagues.domain.repository.TeamRepository
import ziedsportdb.test.fdjtest.features.leagues.domain.usecase.GetTeamsByLeagueUseCase

@OptIn(ExperimentalCoroutinesApi::class)
class GetTeamsUseCaseTest {

    private val teamRepository: TeamRepository = mockk()

    private val testDispatcher = UnconfinedTestDispatcher()

    private val getTeamsByLeagueUseCase: GetTeamsByLeagueUseCase =
        GetTeamsByLeagueUseCase(
            teamRepository = teamRepository,
            defaultDispatcher = testDispatcher,
        )

    @Before
    fun before() {
        MockKAnnotations.init()
    }

    @Test
    fun `given result is Failure, when call getTeamsByLeague, then must emit the error`() {
        val exceptedErrorMessage = "Error Message"

        coEvery { teamRepository.getTeamsByLeague(any()) } returns flow {
            emit(
                FDJResult.Failure(Throwable(exceptedErrorMessage)),
            )
        }

        var actualErrorMessage: String? = null
        runBlocking {
            getTeamsByLeagueUseCase.invoke(anyString()).collect {
                actualErrorMessage = (it as FDJResult.Failure<*>).error?.message
            }
        }

        coVerify { teamRepository.getTeamsByLeague(any()) }
        Assert.assertEquals(exceptedErrorMessage, actualErrorMessage)
    }

    @Test
    fun `given result is Success, when call getTeamsByLeague, then must emit sorted and filtered list`() {
        val listReturnedByRepository = listOf(
            TeamModel(id = "1", teamName = "Angers", badge = ""),
            TeamModel(id = "2", teamName = "Brest", badge = ""),
            TeamModel(id = "3", teamName = "Nice", badge = ""),
            TeamModel(id = "4", teamName = "PSG", badge = ""),
        )

        val exceptedListReturnedByUseCase = listOf(
            TeamModel(id = "4", teamName = "PSG", badge = ""),
            TeamModel(id = "2", teamName = "Brest", badge = ""),
        )

        coEvery { teamRepository.getTeamsByLeague(any()) } returns flow {
            emit(
                FDJResult.Success(listReturnedByRepository),
            )
        }

        var actualList = listOf<TeamModel>()
        runBlocking {
            getTeamsByLeagueUseCase.invoke(anyString()).collect {
                actualList = ((it as FDJResult.Success<*>).data) as List<TeamModel>
            }
        }

        coVerify { teamRepository.getTeamsByLeague(any()) }
        Assert.assertEquals(exceptedListReturnedByUseCase, actualList)
    }
}
