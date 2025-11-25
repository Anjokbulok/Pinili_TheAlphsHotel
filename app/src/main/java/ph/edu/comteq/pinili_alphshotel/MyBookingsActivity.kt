package ph.edu.comteq.pinili_alphshotel

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.Gson
import ph.edu.comteq.pinili_alphshotel.ui.theme.Pinili_AlphsHotelTheme
import java.text.NumberFormat
import java.util.*

class MyBookingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Pinili_AlphsHotelTheme {
                MyBookingsScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBookingsScreen() {
    val context = LocalContext.current
    var bookings by remember { mutableStateOf(loadBookings(context)) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Bookings", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .clickable {
                                val intent = Intent(context, MainActivity::class.java)
                                context.startActivity(intent)
                            }
                    )
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("List of my bookings", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
            Spacer(Modifier.height(12.dp))

            if (bookings.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No bookings yet.", fontSize = 16.sp)
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    itemsIndexed(bookings) { index, booking ->
                        BookingItem(index + 1, booking)
                    }
                }
            }
        }
    }
}

@Composable
fun BookingItem(number: Int, booking: Booking) {
    // Format price in Peso
    val pesoFormat = NumberFormat.getCurrencyInstance(Locale("en", "PH")).apply {
        currency = Currency.getInstance("PHP")
        maximumFractionDigits = 2
    }
    val formattedPrice = pesoFormat.format(booking.totalPrice)

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text("$number. ${booking.firstName} ${booking.lastName}",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp)

            Text("📅 ${booking.checkIn}  →  ${booking.checkOut}")
            Text("👥 Adults: ${booking.adults}, Children: ${booking.children}")
            Text("🛏 Rooms: ${booking.rooms}")
            Text("🏷 Type: ${booking.travelType}")
            Text("💳 Payment: ${booking.paymentMethod}")

            Text(
                "💰 $formattedPrice",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
    }
}

// ---------------------------------------------------------------------
// LOAD BOOKINGS FROM SHARED PREFERENCES
// ---------------------------------------------------------------------
fun loadBookings(context: Context): List<Booking> {
    val prefs = context.getSharedPreferences("bookings", Context.MODE_PRIVATE)
    val json = prefs.getString("list", "[]")
    val gson = Gson()

    return try {
        gson.fromJson(json, Array<Booking>::class.java).toList()
    } catch (e: Exception) {
        emptyList()
    }
}