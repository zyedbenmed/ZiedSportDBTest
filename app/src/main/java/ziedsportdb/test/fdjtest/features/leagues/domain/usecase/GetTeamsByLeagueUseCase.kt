package ziedsportdb.test.fdjtest.features.leagues.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ziedsportdb.test.fdjtest.core.BaseUseCaseWithRequest
import ziedsportdb.test.fdjtest.core.FDJResult
import ziedsportdb.test.fdjtest.di.DefaultDispatcher
import ziedsportdb.test.fdjtest.features.leagues.domain.model.TeamModel
import ziedsportdb.test.fdjtest.features.leagues.domain.repository.TeamRepository
import javax.inject.Inject

class GetTeamsByLeagueUseCase @Inject constructor(
    private val teamRepository: TeamRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
) : BaseUseCaseWithRequest<String, Flow<FDJResult<List<TeamModel>>>> {

    override suspend fun invoke(request: String): Flow<FDJResult<List<TeamModel>>> =
        teamRepository.getTeamsByLeague(leagueName = request).map { result ->
            when (result) {
                is FDJResult.Success -> {
                    val sortedList = withContext(defaultDispatcher) {
                        result.data.sortedByDescending { team -> team.teamName }
                            .filterIndexed { index, _ -> index % 2 == 0 }
                    }
                    FDJResult.Success(sortedList)
                }
                is FDJResult.Failure -> result
            }
        }
}
