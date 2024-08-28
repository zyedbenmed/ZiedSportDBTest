package ziedsportdb.test.fdjtest.features.leagues.domain.repository

import kotlinx.coroutines.flow.Flow
import ziedsportdb.test.fdjtest.core.FDJResult
import ziedsportdb.test.fdjtest.features.leagues.domain.model.LeagueModel

interface LeagueRepository {
    suspend fun getLeagues(): Flow<FDJResult<List<LeagueModel>>>
}
