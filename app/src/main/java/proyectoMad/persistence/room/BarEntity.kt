package proyectoMad.persistence.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bar")
data class BarEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String
)
