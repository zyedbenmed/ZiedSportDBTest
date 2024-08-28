package ziedsportdb.test.fdjtest.features.leagues.data.dto

import ziedsportdb.test.fdjtest.core.DataBaseEntityMapper
import ziedsportdb.test.fdjtest.features.leagues.data.entity.LeagueEntity

data class LeaguesResponse(
    val leagues: List<LeagueResponse?>?,
)

/**
 * A data class representing a single league from a [LeaguesResponse].
 *
 * @property idLeague The ID of the league.
 * @property strLeague The name of the league.
 * @property strSport The sport of the league.
 * @property strLeagueAlternate The alternate name of the league.
 */
data class LeagueResponse(
    val idLeague: String?,
    val strLeague: String?,
    val strSport: String?,
    val strLeagueAlternate: String?,
) : DataBaseEntityMapper<LeagueEntity> {

    override fun mapToDbEntity(): LeagueEntity =
        LeagueEntity(
            idLeague = idLeague ?: "",
            leagueName = strLeague ?: "",
            sport = strSport ?: "",
        )
}
