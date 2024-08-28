package ziedsportdb.test.fdjtest.features.leagues.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ziedsportdb.test.fdjtest.features.leagues.data.entity.LeagueEntity
import ziedsportdb.test.fdjtest.features.leagues.data.entity.TeamEntity

@Database(entities = [LeagueEntity::class, TeamEntity::class], version = 1)
abstract class LeagueDatabase : RoomDatabase() {

    abstract fun leagueDao(): LeagueDao
}
