package com.dicoding.picodiploma.loginwithanimation.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.dicoding.picodiploma.loginwithanimation.data.pref.HistoryPredictionEntity
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserDao
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserEntity

@Database(entities = [UserEntity::class, HistoryPredictionEntity::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE user_session ADD COLUMN name TEXT NOT NULL DEFAULT ''")
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
            CREATE TABLE IF NOT EXISTS `history_prediction` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `userId` TEXT NOT NULL,
                `result` TEXT NOT NULL,
                `explanation` TEXT NOT NULL,
                `suggestion` TEXT NOT NULL,
                `confidenceScore` REAL NOT NULL,
                `imageUrl` TEXT NOT NULL,
                `createdAt` TEXT NOT NULL
            )
            """.trimIndent()
                )
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).addMigrations(MIGRATION_1_2, MIGRATION_2_3 as Migration).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
