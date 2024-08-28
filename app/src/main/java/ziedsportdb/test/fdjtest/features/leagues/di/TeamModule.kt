package ziedsportdb.test.fdjtest.features.leagues.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import ziedsportdb.test.fdjtest.features.leagues.data.datasource.local.TeamLocalDataSource
import ziedsportdb.test.fdjtest.features.leagues.data.datasource.local.TeamLocalDataSourceImpl
import ziedsportdb.test.fdjtest.features.leagues.data.datasource.remote.TeamRemoteDataSource
import ziedsportdb.test.fdjtest.features.leagues.data.datasource.remote.TeamRemoteDataSourceImpl
import ziedsportdb.test.fdjtest.features.leagues.data.repository.TeamRepositoryImpl
import ziedsportdb.test.fdjtest.features.leagues.domain.repository.TeamRepository

@Module
@InstallIn(ViewModelComponent::class)
abstract class TeamModule {

    @Binds
    abstract fun bindTeamRepository(
        teamRepositoryImpl: TeamRepositoryImpl,
    ): TeamRepository

    @Binds
    abstract fun bindTeamRemoteDataSource(
        teamRemoteDataSourceImpl: TeamRemoteDataSourceImpl,
    ): TeamRemoteDataSource

    @Binds
    abstract fun bindTeamLocalDataSource(
        teamLocalDataSourceImpl: TeamLocalDataSourceImpl,
    ): TeamLocalDataSource
}
