package ziedsportdb.test.fdjtest.features.leagues.data.datahelpers

import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SyncStatusManager @Inject constructor(
    private val sharedPreferencesAccessor: SharedPreferencesAccessor,
) {

    companion object {
        private val SYNC_INTERVAL = TimeUnit.DAYS.toMillis(30)
    }

    fun shouldSyncLeagues(): Boolean {
        return shouldSync(sharedPreferencesAccessor.leagueLastSyncTimestamp) {
            sharedPreferencesAccessor.leagueLastSyncTimestamp = it
        }
    }

    fun shouldSyncTeams(leagueName: String): Boolean {
        return !isLeagueHasSavedTeams(leagueName) || shouldSync(sharedPreferencesAccessor.teamLastSyncTimestamp) {
            sharedPreferencesAccessor.teamLastSyncTimestamp = it
        }
    }

    private fun shouldSync(lastSyncTimestamp: Long, setLastSyncTimestamp: (Long) -> Unit): Boolean {
        return try {
            val currentTimeStamp = System.currentTimeMillis()
            val isSyncNeeded = currentTimeStamp - lastSyncTimestamp > SYNC_INTERVAL
            if (isSyncNeeded) {
                setLastSyncTimestamp(currentTimeStamp)
            }
            isSyncNeeded
        } catch (e: Exception) {
            Timber.e("Couldn't determine if shouldSync. $e")
            false
        }
    }

    private fun isLeagueHasSavedTeams(leagueName: String): Boolean {
        if (!sharedPreferencesAccessor.leagueNamesWithSavedTeams.contains(leagueName)) {
            sharedPreferencesAccessor.leagueNamesWithSavedTeams = listOf(leagueName)
            return false
        }
        return true
    }
}
