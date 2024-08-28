package ziedsportdb.test.fdjtest.features.leagues.domain.repository

import kotlinx.coroutines.flow.Flow
import ziedsportdb.test.fdjtest.core.FDJResult
import ziedsportdb.test.fdjtest.features.leagues.domain.model.TeamModel

interface TeamRepository {
    suspend fun getTeamsByLeague(leagueName: String): Flow<FDJResult<List<TeamModel>>>
}
