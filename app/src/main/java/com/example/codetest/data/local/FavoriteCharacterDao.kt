package com.example.codetest.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteCharacterDao {

    @Query("SELECT * FROM favorite_characters ORDER BY name ASC")
    fun observeAll(): Flow<List<FavoriteCharacterEntity>>

    @Query("SELECT id FROM favorite_characters")
    fun observeFavoriteIds(): Flow<List<Int>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(character: FavoriteCharacterEntity)

    @Query("DELETE FROM favorite_characters WHERE id = :characterId")
    suspend fun deleteById(characterId: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_characters WHERE id = :characterId)")
    suspend fun isFavorite(characterId: Int): Boolean
}