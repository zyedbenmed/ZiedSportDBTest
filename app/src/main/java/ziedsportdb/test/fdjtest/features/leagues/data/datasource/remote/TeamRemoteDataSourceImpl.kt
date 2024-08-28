package ziedsportdb.test.fdjtest.features.leagues.data.datasource.remote

import ziedsportdb.test.fdjtest.features.leagues.data.dto.TeamsResponse
import ziedsportdb.test.fdjtest.features.leagues.data.service.LeaguesApi
import javax.inject.Inject

class TeamRemoteDataSourceImpl @Inject constructor(
    private val leaguesApi: LeaguesApi,
) : TeamRemoteDataSource {

    override suspend fun getTeamsByLeague(leagueName: String): TeamsResponse =
        leaguesApi.getTeamsByLeague(leagueName = leagueName)
}
