package es.proyectoMad

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
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
            Log.d(TAG, "Bar no es null en bardetailsActivity")
            findViewById<TextView>(R.id.textViewBarName).text = "Bar Name: ${barEntity.name}"
            findViewById<TextView>(R.id.textViewCuisine).text = "Cuisine: ${barEntity.cuisine ?: "N/A"}"
            findViewById<TextView>(R.id.textViewOpeningHours).text = "Opening Hours: ${barEntity.openingHours ?: "N/A"}"
            findViewById<TextView>(R.id.textViewTakeaway).text = "Takeaway: ${if (barEntity.takeaway) "Yes" else "No"}"
            findViewById<TextView>(R.id.textViewDelivery).text = "Delivery: ${if (barEntity.delivery) "Yes" else "No"}"
            findViewById<TextView>(R.id.textViewAddress).text = "Address: ${barEntity.address ?: "N/A"}"
            findViewById<TextView>(R.id.textViewVegetarianFriendly).text = "Vegetarian Friendly: ${if (barEntity.vegetarianFriendly) "Yes" else "No"}"
            findViewById<TextView>(R.id.textViewVeganFriendly).text = "Vegan Friendly: ${if (barEntity.veganFriendly) "Yes" else "No"}"
            findViewById<TextView>(R.id.textViewStartDate).text = "Start Date: ${barEntity.startDate ?: "N/A"}"
            findViewById<TextView>(R.id.textViewSmokingAllowed).text = "Smoking Allowed: ${if (barEntity.smokingAllowed) "Yes" else "No"}"
            findViewById<TextView>(R.id.textViewImage).text = "Image: ${barEntity.image ?: "N/A"}"
            findViewById<TextView>(R.id.textViewWebsite).text = "Website: ${barEntity.website ?: "N/A"}"
            findViewById<TextView>(R.id.textViewPhoneNumber).text = "Phone Number: ${barEntity.phoneNumber ?: "N/A"}"
            findViewById<TextView>(R.id.textViewMichelinStars).text = "Michelin Stars: ${barEntity.michelinStars?.toString() ?: "N/A"}"
            findViewById<TextView>(R.id.textViewKitchenOpeningHours).text = "Kitchen Opening Hours: ${barEntity.kitchenOpeningHours ?: "N/A"}"
        } else {
            // Manejar el caso donde barEntity es nulo, por ejemplo, mostrar un mensaje de error con Toast
            Log.d(TAG, "Bar es null en bardetailsactivity")
        }
    }
}
