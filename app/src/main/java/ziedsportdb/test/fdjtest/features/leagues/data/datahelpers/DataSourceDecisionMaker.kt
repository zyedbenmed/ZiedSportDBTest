package ziedsportdb.test.fdjtest.features.leagues.data.datahelpers

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataSourceDecisionMaker @Inject constructor(
    private val checkConnection: CheckConnection,
    private val syncStatusManager: SyncStatusManager,
) {

    fun shouldFetchLeaguesFromRemote(): Boolean {
        return checkConnection.isConnected() && syncStatusManager.shouldSyncLeagues()
    }

    fun shouldFetchTeamsFromRemote(leagueName: String): Boolean {
        return checkConnection.isConnected() && syncStatusManager.shouldSyncTeams(leagueName)
    }
}
