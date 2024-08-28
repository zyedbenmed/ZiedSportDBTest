package ziedsportdb.test.fdjtest.features.leagues.data.datahelpers

import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * A class that manages the synchronization status for leagues and teams.
 *
 * @property sharedPreferencesAccessor is used to access the shared preferences.
 */
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

    /**
     * Returns whether a synchronization is needed based on the last synchronization timestamp and the synchronization interval.
     *
     * @param lastSyncTimestamp The last synchronization timestamp.
     * @param setLastSyncTimestamp A function to set the last synchronization timestamp.
     * @return `true` if a synchronization is needed, `false` otherwise.
     */
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
}
