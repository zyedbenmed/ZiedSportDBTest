package ziedsportdb.test.fdjtest.features.leagues.presentation

import ziedsportdb.test.fdjtest.features.leagues.domain.model.TeamModel
import javax.inject.Inject

class TeamsDisplayModelBuilder @Inject constructor() {

    internal fun buildTeamsDisplayModel(model: List<TeamModel>): List<TeamDisplayModel> =
        model.map {
            TeamDisplayModel(
                teamName = it.teamName,
                badge = it.badge,
            )
        }
}
