package ziedsportdb.test.fdjtest.features.leagues.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import ziedsportdb.test.fdjtest.core.FDJResult
import ziedsportdb.test.fdjtest.features.leagues.data.datahelpers.DataSourceDecisionMaker
import ziedsportdb.test.fdjtest.features.leagues.data.datasource.local.TeamLocalDataSource
import ziedsportdb.test.fdjtest.features.leagues.data.datasource.remote.TeamRemoteDataSource
import ziedsportdb.test.fdjtest.features.leagues.domain.model.TeamModel
import ziedsportdb.test.fdjtest.features.leagues.domain.repository.TeamRepository
import javax.inject.Inject

class TeamRepositoryImpl @Inject constructor(
    private val teamRemoteDataSource: TeamRemoteDataSource,
    private val teamLocalDataSource: TeamLocalDataSource,
    private val dataSourceDecisionMaker: DataSourceDecisionMaker,
) : TeamRepository {

    override suspend fun getTeamsByLeague(leagueName: String): Flow<FDJResult<List<TeamModel>>> {
        if (dataSourceDecisionMaker.shouldFetchTeamsFromRemote(leagueName)) {
            try {
                val remoteRawData = teamRemoteDataSource.getTeamsByLeague(leagueName).teams
                val listOfTeamsEntity = remoteRawData?.mapNotNull { teamResponse ->
                    teamResponse?.mapToDbEntity()
                }
                listOfTeamsEntity?.let { list ->
                    teamLocalDataSource.insertTeams(list = list)
                }
            } catch (e: Exception) {
                Timber.e("error $e")
            }
        }

        return flow {
            emit(
                FDJResult.Success(
                    teamLocalDataSource.getAllTeamsByLeague(leagueName).map { leagueEntity ->
                        leagueEntity.mapToDomainModel()
                    },
                ),
            )
        }
    }
}
