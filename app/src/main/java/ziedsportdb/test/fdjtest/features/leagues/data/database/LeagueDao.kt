package ziedsportdb.test.fdjtest.features.leagues.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ziedsportdb.test.fdjtest.features.leagues.data.entity.LeagueEntity
import ziedsportdb.test.fdjtest.features.leagues.data.entity.TeamEntity

@Dao
interface LeagueDao {

    @Query("SELECT * FROM league")
    fun getAllLeagues(): List<LeagueEntity>

    @Insert
    fun insertAllLeagues(leaguesList: List<LeagueEntity>)

    @Query("DELETE FROM league")
    fun deleteAllLeagues()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAvailableTeams(teamList: List<TeamEntity>)

    @Query("SELECT * FROM team WHERE (:nameOfLeague IN (leagueName, leagueName2, leagueName3, leagueName4, leagueName5, leagueName6, leagueName7)) ORDER BY teamName ASC")
    fun getTeamsByLeague(nameOfLeague: String): List<TeamEntity>
}
