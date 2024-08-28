package ziedsportdb.test.fdjtest.features.leagues.data.dto

import com.google.gson.annotations.SerializedName
import ziedsportdb.test.fdjtest.core.DataBaseEntityMapper
import ziedsportdb.test.fdjtest.features.leagues.data.entity.TeamEntity

data class TeamsResponse(
    val teams: List<TeamResponse?>?,
)

data class TeamResponse(
    val idTeam: String?,
    val strTeam: String?,
    val idSoccerXML: String? = null,
    @SerializedName("idAPIfootball")
    val idApiFootball: String? = null,
    val intLoved: String? = null,
    val strTeamAlternate: String? = null,
    val strTeamShort: String? = null,
    val intFormedYear: String? = null,
    val strSport: String? = null,
    val strLeague: String? = null,
    val idLeague: String? = null,
    val strLeague2: String? = null,
    val idLeague2: String? = null,
    val strLeague3: String? = null,
    val idLeague3: String? = null,
    val strLeague4: String? = null,
    val idLeague4: String? = null,
    val strLeague5: String? = null,
    val idLeague5: String? = null,
    val strLeague6: String? = null,
    val idLeague6: String? = null,
    val strLeague7: String? = null,
    val idLeague7: String? = null,
    val strDivision: String? = null,
    val idVenue: String? = null,
    val strStadium: String? = null,
    val strKeywords: String? = null,
    val strRSS: String? = null,
    val strLocation: String? = null,
    val intStadiumCapacity: String? = null,
    val strWebsite: String? = null,
    val strFacebook: String? = null,
    val strTwitter: String? = null,
    val strInstagram: String? = null,
    val strYoutube: String? = null,
    val strDescriptionEN: String? = null,
    val strCountry: String? = null,
    val strBadge: String? = null,
    val strLogo: String? = null,
    val strBanner: String? = null,
    val strEquipment: String? = null,
    val strLocked: String? = null,
) : DataBaseEntityMapper<TeamEntity> {

    override fun mapToDbEntity(): TeamEntity {
        return TeamEntity(
            teamId = idTeam ?: "",
            teamName = strTeam ?: "",
            teamBadge = strBadge ?: "",
            leagueName = this.strLeague,
            leagueName2 = this.strLeague2,
            leagueName3 = this.strLeague3,
            leagueName4 = this.strLeague4,
            leagueName5 = this.strLeague5,
            leagueName6 = this.strLeague6,
            leagueName7 = this.strLeague7,
        )
    }
}
