package ziedsportdb.test.fdjtest.features.leagues.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ziedsportdb.test.fdjtest.core.DomainModelMapper
import ziedsportdb.test.fdjtest.features.leagues.domain.model.LeagueModel

/**
 * Entity class representing a league in the local database.
 *
 * @property idLeague The unique identifier of the league.
 * @property leagueName The name of the league.
 * @property sport The sport of the league.
 */
@Entity(tableName = "league")
data class LeagueEntity(
    @PrimaryKey val idLeague: String,
    val leagueName: String?,
    val sport: String?,
) : DomainModelMapper<LeagueModel> {

    override fun mapToDomainModel(): LeagueModel =
        LeagueModel(
            idLeague = idLeague,
            name = leagueName ?: "",
            sport = sport ?: "",
        )
}
