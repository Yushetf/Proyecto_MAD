package es.proyectoMad.persistence.room

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bar")
data class BarEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val cuisine: String?, // Tipo de cocina
    val openingHours: String?, // Horario de apertura
    val takeaway: Boolean = false, // Ofrece comida para llevar
    val delivery: Boolean = false, // Ofrece servicio de entrega
    val address: String?, // Dirección
    val vegetarianFriendly: Boolean = false, // Amigable con vegetarianos
    val veganFriendly: Boolean = false, // Amigable con veganos
    val startDate: String?, // Fecha de apertura
    val smokingAllowed: Boolean = false, // Se permite fumar
    val image: String?, // URL de la foto
    val website: String?, // Sitio web
    val phoneNumber: String?, // Número de teléfono
    val michelinStars: Int?, // Número de estrellas Michelin
    val kitchenOpeningHours: String? // Horario de apertura de la cocina
)
    : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString() ?: "",
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(name)
        parcel.writeString(cuisine)
        parcel.writeString(openingHours)
        parcel.writeByte(if (takeaway) 1 else 0)
        parcel.writeByte(if (delivery) 1 else 0)
        parcel.writeString(address)
        parcel.writeByte(if (vegetarianFriendly) 1 else 0)
        parcel.writeByte(if (veganFriendly) 1 else 0)
        parcel.writeString(startDate)
        parcel.writeByte(if (smokingAllowed) 1 else 0)
        parcel.writeString(image)
        parcel.writeString(website)
        parcel.writeString(phoneNumber)
        michelinStars?.let { parcel.writeInt(it) }
        parcel.writeString(kitchenOpeningHours)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BarEntity> {
        override fun createFromParcel(parcel: Parcel): BarEntity {
            return BarEntity(parcel)
        }

        override fun newArray(size: Int): Array<BarEntity?> {
            return arrayOfNulls(size)
        }
    }
}