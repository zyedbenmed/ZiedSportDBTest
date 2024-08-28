package ziedsportdb.test.fdjtest.features.leagues.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import ziedsportdb.test.fdjtest.core.FDJResult
import ziedsportdb.test.fdjtest.features.leagues.data.datahelpers.DataSourceDecisionMaker
import ziedsportdb.test.fdjtest.features.leagues.data.datasource.local.LeagueLocalDataSource
import ziedsportdb.test.fdjtest.features.leagues.data.datasource.remote.LeagueRemoteDataSource
import ziedsportdb.test.fdjtest.features.leagues.domain.model.LeagueModel
import ziedsportdb.test.fdjtest.features.leagues.domain.repository.LeagueRepository
import javax.inject.Inject

/**
 * Implementation of [LeagueRepository] which handles fetching league data from local and remote data sources.
 *
 * @property leagueRemoteDataSource the remote data source to fetch data from
 * @property leagueLocalDataSource the local data source to fetch data from
 * @property dataSourceDecisionMaker the decision maker which decides whether to fetch from remote or local data source
 */
class LeagueRepositoryImpl @Inject constructor(
    private val leagueRemoteDataSource: LeagueRemoteDataSource,
    private val leagueLocalDataSource: LeagueLocalDataSource,
    private val dataSourceDecisionMaker: DataSourceDecisionMaker,
) : LeagueRepository {

    override suspend fun getLeagues(): Flow<FDJResult<List<LeagueModel>>> {
        if (dataSourceDecisionMaker.shouldFetchLeaguesFromRemote()) {
            try {
                val remoteRawData = leagueRemoteDataSource.getLeagues().leagues
                val leaguesEntity = remoteRawData?.mapNotNull { leagueResponse ->
                    leagueResponse?.mapToDbEntity()
                }
                leaguesEntity?.let { list ->
                    leagueLocalDataSource.deleteAll()
                    leagueLocalDataSource.insertAllLeagues(list = list)
                }
            } catch (e: Exception) {
                Timber.e("error $e")
            }
        }

        // return data fetched from local data source
        return flow {
            emit(
                FDJResult.Success(
                    leagueLocalDataSource.getAllLeagues().map { leagueEntity ->
                        leagueEntity.mapToDomainModel()
                    },
                ),
            )
        }
    }
}
