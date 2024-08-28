package ziedsportdb.test.fdjtest.features.leagues.data.datasource.remote

import ziedsportdb.test.fdjtest.features.leagues.data.dto.LeaguesResponse
import ziedsportdb.test.fdjtest.features.leagues.data.service.LeaguesApi
import javax.inject.Inject

class LeagueRemoteDataSourceImpl @Inject constructor(
    private val leaguesApi: LeaguesApi,
) : LeagueRemoteDataSource {

    override suspend fun getLeagues(): LeaguesResponse =
        leaguesApi.getLeagues()
}
