package ziedsportdb.test.fdjtest.features.leagues.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ziedsportdb.test.fdjtest.core.BaseUseCase
import ziedsportdb.test.fdjtest.core.FDJResult
import ziedsportdb.test.fdjtest.di.DefaultDispatcher
import ziedsportdb.test.fdjtest.features.leagues.domain.model.LeagueModel
import ziedsportdb.test.fdjtest.features.leagues.domain.repository.LeagueRepository
import javax.inject.Inject

class GetLeaguesUseCase @Inject constructor(
    private val leagueRepository: LeagueRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
) : BaseUseCase<Flow<FDJResult<List<LeagueModel>>>> {

    override suspend fun invoke(): Flow<FDJResult<List<LeagueModel>>> =
        leagueRepository.getLeagues().map { result ->
            when (result) {
                is FDJResult.Success -> {
                    val sortedList = withContext(defaultDispatcher) {
                        result.data.sortedBy { league -> league.name }
                    }
                    FDJResult.Success(sortedList)
                }
                is FDJResult.Failure -> result
            }
        }
}
