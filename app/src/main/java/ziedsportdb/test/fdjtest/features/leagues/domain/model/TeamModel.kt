package ziedsportdb.test.fdjtest.features.leagues.domain.model

data class TeamModel(
    val id: String,
    val teamName: String = "",
    val badge: String = "",
) {
    constructor() : this(id = "", teamName = "", badge = "")
}
