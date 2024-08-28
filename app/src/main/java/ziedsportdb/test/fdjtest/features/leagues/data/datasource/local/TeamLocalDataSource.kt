package ziedsportdb.test.fdjtest.features.leagues.data.datasource.local

import ziedsportdb.test.fdjtest.features.leagues.data.entity.TeamEntity

interface TeamLocalDataSource {
    suspend fun getAllTeamsByLeague(leagueName: String): List<TeamEntity>
    suspend fun insertTeams(list: List<TeamEntity>)
}
