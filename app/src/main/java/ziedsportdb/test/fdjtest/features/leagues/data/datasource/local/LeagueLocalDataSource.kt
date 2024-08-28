package ziedsportdb.test.fdjtest.features.leagues.data.datasource.local

import ziedsportdb.test.fdjtest.features.leagues.data.entity.LeagueEntity

interface LeagueLocalDataSource {
    suspend fun getAllLeagues(): List<LeagueEntity>
    suspend fun insertAllLeagues(list: List<LeagueEntity>)
    suspend fun deleteAll()
}
