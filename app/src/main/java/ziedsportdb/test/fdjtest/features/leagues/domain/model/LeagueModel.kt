package ziedsportdb.test.fdjtest.features.leagues.domain.model

data class LeagueModel(
    val idLeague: String,
    val name: String,
    val sport: String,
) {
    constructor() : this(idLeague = "", name = "", sport = "")
}
