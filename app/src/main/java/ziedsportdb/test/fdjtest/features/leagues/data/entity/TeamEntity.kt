package ziedsportdb.test.fdjtest.features.leagues.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ziedsportdb.test.fdjtest.core.DomainModelMapper
import ziedsportdb.test.fdjtest.features.leagues.domain.model.TeamModel

@Entity(tableName = "team")
data class TeamEntity(
    @PrimaryKey val teamId: String,
    val teamName: String = "",
    val teamBadge: String = "",
    val leagueName: String?,
    val leagueName2: String? = null,
    val leagueName3: String? = null,
    val leagueName4: String? = null,
    val leagueName5: String? = null,
    val leagueName6: String? = null,
    val leagueName7: String? = null,
) : DomainModelMapper<TeamModel> {

    override fun mapToDomainModel(): TeamModel =
        TeamModel(
            id = teamId,
            teamName = teamName,
            badge = teamBadge,
        )
}
