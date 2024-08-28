package ziedsportdb.test.fdjtest.features.leagues.data.datasource.remote

import ziedsportdb.test.fdjtest.features.leagues.data.dto.LeaguesResponse

interface LeagueRemoteDataSource {
    suspend fun getLeagues(): LeaguesResponse
}
