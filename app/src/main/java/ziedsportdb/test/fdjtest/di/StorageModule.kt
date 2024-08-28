package ziedsportdb.test.fdjtest.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ziedsportdb.test.fdjtest.features.leagues.data.database.LeagueDao
import ziedsportdb.test.fdjtest.features.leagues.data.database.LeagueDatabase
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object StorageModule {

    private const val PREFERENCE_NAME = "FDJ_shared_preferences"

    @Singleton
    @Provides
    fun providesLeagueDao(dataBase: LeagueDatabase): LeagueDao =
        dataBase.leagueDao()

    @Singleton
    @Provides
    fun provideLeagueDb(@ApplicationContext context: Context): LeagueDatabase =
        Room.databaseBuilder(
            context,
            LeagueDatabase::class.java,
            "league-db",
        ).build()

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
}
