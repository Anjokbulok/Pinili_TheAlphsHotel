package ph.edu.comteq.pinili_alphshotel

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import java.text.SimpleDateFormat
import java.util.*



class BookingConfirm : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val hotelJson = intent.getStringExtra("hotel")
        val roomJson = intent.getStringExtra("room")
        val hotel = Gson().fromJson(hotelJson, Hotel::class.java)
        val room = Gson().fromJson(roomJson, Room::class.java)

        setContent {
            Pinili_AlphsHotelTheme {
                BookingConfirmScreen(hotel, room)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingConfirmScreen(hotel: Hotel, room: Room) {
    val context = LocalContext.current

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var checkIn by remember { mutableStateOf("") }
    var checkOut by remember { mutableStateOf("") }
    var adults by remember { mutableStateOf("1") }
    var children by remember { mutableStateOf("0") }
    var rooms by remember { mutableStateOf(1) }
    var travelType by remember { mutableStateOf("Sightseeing") }
    var paymentMethod by remember { mutableStateOf("Cash") }
    var price by remember { mutableStateOf(room.room_price_for_one_night) }

    var showError by remember { mutableStateOf<String?>(null) }
    var showConfirm by remember { mutableStateOf(false) }

    // Converts input in allowed formats to "Tue, Sep 10, 2024" style
    fun convertDate(input: String): String? {
        val inputPatterns = listOf("MM/dd/yyyy", "MM-dd-yyyy", "MMM dd yyyy")
        val outputFormat = SimpleDateFormat("EEE, MMM dd, yyyy", Locale.US)
        for (pattern in inputPatterns) {
            try {
                val sdf = SimpleDateFormat(pattern, Locale.US)
                sdf.isLenient = false
                val parsed = sdf.parse(input.trim())
                if (parsed != null) {
                    return outputFormat.format(parsed)
                }
            } catch (e: Exception) {
                // skip, try next pattern
            }
        }
        return null
    }

    fun calculateRooms() {
        val a = adults.toIntOrNull() ?: 0
        val c = children.toIntOrNull() ?: 0
        val total = a + c
        rooms = if (total == 0) 1 else (total + room.room_total_number_of_guests - 1) / room.room_total_number_of_guests
    }

    fun calculatePrice() {
        val start = convertDate(checkIn)?.let {
            SimpleDateFormat("EEE, MMM dd, yyyy", Locale.US).parse(it)
        }
        val end = convertDate(checkOut)?.let {
            SimpleDateFormat("EEE, MMM dd, yyyy", Locale.US).parse(it)
        }
        if (start != null && end != null) {
            val days = ((end.time - start.time) / (1000 * 60 * 60 * 24)).toInt()
            var base = room.room_price_for_one_night * rooms * days
            if (travelType == "Business") base += 150
            price = base
        }
    }

    fun saveBooking(context: Context, booking: Booking) {
        val prefs = context.getSharedPreferences("bookings", Context.MODE_PRIVATE)
        val gson = Gson()
        val listJson = prefs.getString("list", "[]")
        val list: MutableList<Booking> = try {
            gson.fromJson(listJson, Array<Booking>::class.java).toMutableList()
        } catch (e: Exception) {
            mutableListOf()
        }
        list.add(booking)
        prefs.edit().putString("list", gson.toJson(list)).apply()
    }

    LaunchedEffect(adults, children) { calculateRooms() }
    LaunchedEffect(checkIn, checkOut, rooms, travelType) { calculatePrice() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Booking Confirm", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Text("You are going to reserve...", fontSize = 12.sp)
                    }
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .clickable { (context as ComponentActivity).finish() }
                    )
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(16.dp)
                .padding(padding)
                .fillMaxSize()
        ) {
            val scroll = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scroll),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Hotel: ${hotel.hotel_name}", fontWeight = FontWeight.Bold)
                Text("Room: ${room.room_type}")

                OutlinedTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = { Text("First Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = { Text("Last Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = checkIn,
                    onValueChange = { checkIn = it },
                    label = { Text("Check-in Date") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = checkOut,
                    onValueChange = { checkOut = it },
                    label = { Text("Check-out Date") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = adults,
                    onValueChange = { adults = it },
                    label = { Text("Adults") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = children,
                    onValueChange = { children = it },
                    label = { Text("Children") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Rooms Needed: $rooms", fontSize = 18.sp, fontWeight = FontWeight.Bold)

                Text("Travel for business?")
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = travelType == "Sightseeing", onClick = { travelType = "Sightseeing" })
                    Spacer(Modifier.width(8.dp))
                    Text("For sightseeing")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = travelType == "Business", onClick = { travelType = "Business" })
                    Spacer(Modifier.width(8.dp))
                    Text("+ ₱150 For business with a meeting room")
                }

                Text("Which way to pay?")
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = paymentMethod == "Cash", onClick = { paymentMethod = "Cash" })
                    Spacer(Modifier.width(8.dp))
                    Text("Cash")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = paymentMethod == "Credit card", onClick = { paymentMethod = "Credit card" })
                    Spacer(Modifier.width(8.dp))
                    Text("Credit card")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = paymentMethod == "E‑Pay", onClick = { paymentMethod = "E‑Pay" })
                    Spacer(Modifier.width(8.dp))
                    Text("E‑Pay")
                }

                Text("₱ ${price.toInt()}", fontSize = 24.sp, fontWeight = FontWeight.Bold)

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        val inDate = convertDate(checkIn)
                        val outDate = convertDate(checkOut)
                        if (firstName.isBlank() || lastName.isBlank() || inDate == null || outDate == null) {
                            showError = "Please fill all fields correctly."
                        } else {
                            showConfirm = true
                        }
                    }
                ) { Text("Book now") }

                if (showError != null) {
                    AlertDialog(
                        onDismissRequest = { showError = null },
                        title = { Text("Error") },
                        text = { Text(showError!!) },
                        confirmButton = {
                            TextButton(onClick = { showError = null }) { Text("OK") }
                        }
                    )
                }
                if (showConfirm) {
                    AlertDialog(
                        onDismissRequest = { showConfirm = false },
                        title = { Text("Confirm Booking") },
                        text = { Text("Are you sure you want to book this room?") },
                        confirmButton = {
                            TextButton(onClick = {
                                val inDateFinal = convertDate(checkIn)!!
                                val outDateFinal = convertDate(checkOut)!!
                                saveBooking(context, Booking(
                                    firstName,
                                    lastName,
                                    inDateFinal,
                                    outDateFinal,
                                    adults.toInt(),
                                    children.toInt(),
                                    rooms,
                                    travelType,
                                    paymentMethod,
                                    price
                                ))
                                showConfirm = false
                                context.startActivity(Intent(context, MyBookingsActivity::class.java))
                            }) { Text("Yes") }
                        },
                        dismissButton = {
                            TextButton(onClick = { showConfirm = false }) { Text("No") }
                        }
                    )
                }
            }
        }
    }
}
