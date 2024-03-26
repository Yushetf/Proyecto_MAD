package es.proyectoMad

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import es.proyectoMad.persistence.room.BarEntity
import es.upm.btb.helloworldkt.R

class BarDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bar_details)

        // Obtener los datos del bar de los extras del Intent
        val barEntity: BarEntity? = intent.getParcelableExtra("barEntity")

        // Mostrar los datos del bar en la interfaz de usuario si barEntity no es nulo
        if (barEntity != null) {
            findViewById<TextView>(R.id.textViewBarName).text = barEntity.name
            findViewById<TextView>(R.id.textViewCuisine).text = barEntity.cuisine ?: "N/A"
            findViewById<TextView>(R.id.textViewOpeningHours).text = barEntity.openingHours ?: "N/A"
            findViewById<TextView>(R.id.textViewTakeaway).text = if (barEntity.takeaway) "Yes" else "No"
            findViewById<TextView>(R.id.textViewDelivery).text = if (barEntity.delivery) "Yes" else "No"
            findViewById<TextView>(R.id.textViewOutdoorSeating).text = if (barEntity.outdoorSeating) "Yes" else "No"
            findViewById<TextView>(R.id.textViewReservation).text = if (barEntity.reservation) "Yes" else "No"
            findViewById<TextView>(R.id.textViewAddress).text = barEntity.address ?: "N/A"
            findViewById<TextView>(R.id.textViewWheelchairAccessible).text = if (barEntity.wheelchairAccessible) "Yes" else "No"
            findViewById<TextView>(R.id.textViewVegetarianFriendly).text = if (barEntity.vegetarianFriendly) "Yes" else "No"
            findViewById<TextView>(R.id.textViewVeganFriendly).text = if (barEntity.veganFriendly) "Yes" else "No"
            findViewById<TextView>(R.id.textViewStartDate).text = barEntity.startDate ?: "N/A"
            findViewById<TextView>(R.id.textViewInternetAccess).text = if (barEntity.internetAccess) "Yes" else "No"
            findViewById<TextView>(R.id.textViewSmokingAllowed).text = if (barEntity.smokingAllowed) "Yes" else "No"
            findViewById<TextView>(R.id.textViewImage).text = barEntity.image ?: "N/A"
            findViewById<TextView>(R.id.textViewWebsiteMenu).text = barEntity.websiteMenu ?: "N/A"
            findViewById<TextView>(R.id.textViewWebsite).text = barEntity.website ?: "N/A"
            findViewById<TextView>(R.id.textViewPhoneNumber).text = barEntity.phoneNumber ?: "N/A"
            findViewById<TextView>(R.id.textViewMichelinStars).text = barEntity.michelinStars?.toString() ?: "N/A"
            findViewById<TextView>(R.id.textViewKitchenOpeningHours).text = barEntity.kitchenOpeningHours ?: "N/A"
        } else {
            // Manejar el caso donde barEntity es nulo, por ejemplo, mostrar un mensaje de error con Toast
            Toast.makeText(this, "Bar details not found", Toast.LENGTH_SHORT).show()
        }
    }
}
