package com.dicoding.picodiploma.loginwithanimation.data.pref

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserSession(user: UserEntity)

    @Query("SELECT * FROM user_session LIMIT 1")
    suspend fun getUserSession(): UserEntity?

    @Query("DELETE FROM user_session")
    suspend fun clearSession()

    @Query("SELECT email FROM user_session WHERE isLogin = 1 LIMIT 1")
    suspend fun getEmail(): String?

    @Query("SELECT name FROM user_session WHERE isLogin = 1 LIMIT 1")
    suspend fun getName(): String?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistoryPrediction(historyPrediction: HistoryPredictionEntity)

    @Query("SELECT * FROM history_prediction WHERE id = :id LIMIT 1")
    suspend fun getPredictionById(id: String): HistoryPredictionEntity?

    @Query("DELETE FROM history_prediction")
    suspend fun clearHistoryPredictions()

    @Query("SELECT * FROM history_prediction")
    suspend fun getAllHistoryPredictions(): List<HistoryPredictionEntity>
}
