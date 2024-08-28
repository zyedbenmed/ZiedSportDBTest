package ziedsportdb.test.fdjtest.features.leagues.data.datasource.local

import ziedsportdb.test.fdjtest.features.leagues.data.database.LeagueDao
import ziedsportdb.test.fdjtest.features.leagues.data.entity.TeamEntity
import javax.inject.Inject

class TeamLocalDataSourceImpl @Inject constructor(
    private val leagueDao: LeagueDao,
) : TeamLocalDataSource {

    override suspend fun getAllTeamsByLeague(leagueName: String): List<TeamEntity> {
        return leagueDao.getTeamsByLeague(nameOfLeague = leagueName)
    }

    override suspend fun insertTeams(list: List<TeamEntity>) {
        leagueDao.insertAvailableTeams(teamList = list)
    }
}
