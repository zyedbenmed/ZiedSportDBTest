package ziedsportdb.test.fdjtest.features.leagues.data.service

import retrofit2.http.GET
import ziedsportdb.test.fdjtest.features.leagues.data.dto.LeaguesResponse

interface LeaguesApi {

    @GET("all_leagues.php")
    suspend fun getLeagues(): LeaguesResponse
}
