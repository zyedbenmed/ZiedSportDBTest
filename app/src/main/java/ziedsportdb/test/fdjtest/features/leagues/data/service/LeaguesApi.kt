package ziedsportdb.test.fdjtest.features.leagues.data.service

import retrofit2.http.GET
import retrofit2.http.Query
import ziedsportdb.test.fdjtest.features.leagues.data.dto.LeaguesResponse
import ziedsportdb.test.fdjtest.features.leagues.data.dto.TeamsResponse

interface LeaguesApi {

    @GET("all_leagues.php")
    suspend fun getLeagues(): LeaguesResponse

    @GET("search_all_teams.php")
    suspend fun getTeamsByLeague(@Query("l") leagueName: String): TeamsResponse
}
