package es.proyectoMad.persistence.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface IBarDao {
    @Insert
    fun insertBar(bar: BarEntity)

    @Query("SELECT * FROM bar")
    fun getAllBars(): List<BarEntity>

    @Query("DELETE FROM bar")
    fun deleteAllBars()
}
