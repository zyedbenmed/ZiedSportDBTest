package ziedsportdb.test.fdjtest.features.leagues.data.datahelpers

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferencesAccessor @Inject constructor(
    private val sharedPreferences: SharedPreferences,
) {

    companion object {
        private const val LEAGUE_LAST_SYNC_TIMESTAMP_KEY = "league_last_sync_timestamp"
        private const val LEAGUE_NAMES_WITH_SAVED_TEAMS = "league_names_with_saved_teams"
    }

    var leagueLastSyncTimestamp: Long
        get() = sharedPreferences.getLong(LEAGUE_LAST_SYNC_TIMESTAMP_KEY, 0)
        set(value) = sharedPreferences.edit().putLong(LEAGUE_LAST_SYNC_TIMESTAMP_KEY, value).apply()
}
