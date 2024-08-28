package ziedsportdb.test.fdjtest.features.leagues.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ziedsportdb.test.fdjtest.features.leagues.data.entity.LeagueEntity

@Dao
interface LeagueDao {

    @Query("SELECT * FROM league")
    fun getAllLeagues(): List<LeagueEntity>

    @Insert
    fun insertAllLeagues(leaguesList: List<LeagueEntity>)

    @Query("DELETE FROM league")
    fun deleteAllLeagues()
}
