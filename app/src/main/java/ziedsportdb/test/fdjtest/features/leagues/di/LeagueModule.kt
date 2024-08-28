package ziedsportdb.test.fdjtest.features.leagues.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import ziedsportdb.test.fdjtest.features.leagues.data.datasource.local.LeagueLocalDataSource
import ziedsportdb.test.fdjtest.features.leagues.data.datasource.local.LeagueLocalDataSourceImpl
import ziedsportdb.test.fdjtest.features.leagues.data.datasource.remote.LeagueRemoteDataSource
import ziedsportdb.test.fdjtest.features.leagues.data.datasource.remote.LeagueRemoteDataSourceImpl
import ziedsportdb.test.fdjtest.features.leagues.data.repository.LeagueRepositoryImpl
import ziedsportdb.test.fdjtest.features.leagues.domain.repository.LeagueRepository

@Module
@InstallIn(ViewModelComponent::class)
abstract class LeagueModule {

    @Binds
    abstract fun bindLeagueRepository(
        leagueRepositoryImpl: LeagueRepositoryImpl,
    ): LeagueRepository

    @Binds
    abstract fun bindLeagueRemoteDataSource(
        leagueRemoteDataSourceImpl: LeagueRemoteDataSourceImpl,
    ): LeagueRemoteDataSource

    @Binds
    abstract fun bindLeagueLocalDataSource(
        leagueLocalDataSourceImpl: LeagueLocalDataSourceImpl,
    ): LeagueLocalDataSource
}
