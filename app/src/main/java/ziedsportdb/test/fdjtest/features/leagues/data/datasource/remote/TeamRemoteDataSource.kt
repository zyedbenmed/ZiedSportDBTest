package ziedsportdb.test.fdjtest.features.leagues.data.datasource.remote

import ziedsportdb.test.fdjtest.features.leagues.data.dto.TeamsResponse

interface TeamRemoteDataSource {
    suspend fun getTeamsByLeague(leagueName: String): TeamsResponse
}
