package ziedsportdb.test.fdjtest.features.leagues.presentation

import ziedsportdb.test.fdjtest.features.leagues.domain.model.LeagueModel
import javax.inject.Inject

class LeaguesDisplayModelBuilder @Inject constructor() {

    internal fun buildLeaguesDisplayModel(model: List<LeagueModel>): List<LeagueDisplayModel> =
        model.map {
            LeagueDisplayModel(
                idLeague = it.idLeague,
                name = it.name,
            )
        }
}
