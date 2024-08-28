package ziedsportdb.test.fdjtest.features.leagues.data.datasource.local

import ziedsportdb.test.fdjtest.features.leagues.data.database.LeagueDao
import ziedsportdb.test.fdjtest.features.leagues.data.entity.LeagueEntity
import javax.inject.Inject

class LeagueLocalDataSourceImpl @Inject constructor(
    private val leagueDao: LeagueDao,
) : LeagueLocalDataSource {

    override suspend fun getAllLeagues(): List<LeagueEntity> =
        leagueDao.getAllLeagues()

    override suspend fun insertAllLeagues(list: List<LeagueEntity>) {
        leagueDao.insertAllLeagues(leaguesList = list)
    }

    override suspend fun deleteAll() {
        leagueDao.deleteAllLeagues()
    }
}
