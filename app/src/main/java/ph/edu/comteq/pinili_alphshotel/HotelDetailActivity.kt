package ph.edu.comteq.pinili_alphshotel

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.Gson
import ph.edu.comteq.pinili_alphshotel.ui.theme.Pinili_AlphsHotelTheme


data class HotelDetails(
    val hotel_id: Int,
    val hotel_name: String,
    val guest_reviews: GuestReviews,
    val rooms: List<Room>
)

data class GuestReviews(
    val ratings_categories: List<Map<String, Double>>,
    val reviews_objects: List<ReviewObject>
)

data class ReviewObject(
    val username: String,
    val country: String,
    val review_text: String
)

data class Room(
    val room_id: Int,
    val room_type: String,
    val room_bed_type: String,
    val room_total_number_of_guests: Int,
    val room_features: List<String>,
    val room_price_for_one_night: Double
)

class HotelDetailActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get hotel object from intent
        val hotelJson = intent.getStringExtra("hotel")
        val hotel = Gson().fromJson(hotelJson, Hotel::class.java)

        //  Dynamically pick correct JSON file based on hotel_id
        val fileName = "hotels_details.${hotel.hotel_id}.json"

        //  Load JSON safely from assets
        val detailsJson = try {
            assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (e: Exception) {
            // fallback (if file not found)
            e.printStackTrace()
            null
        }

        // Parse JSON if found
        val hotelDetails: HotelDetails? = detailsJson?.let {
            Gson().fromJson(it, HotelDetails::class.java)
        }

        // Set Compose UI
        setContent {
            Pinili_AlphsHotelTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "The Alphs Hotel",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 20.sp
                                        )
                                        Image(
                                            painter = painterResource(id = R.drawable.france_national_flag),
                                            contentDescription = "Flag",
                                            modifier = Modifier
                                                .width(40.dp)
                                                .padding(start = 8.dp)
                                        )
                                    }

                                    val context = LocalContext.current
                                    Icon(
                                        imageVector = Icons.Outlined.Person,
                                        contentDescription = "User",
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clickable {
                                                val intent =
                                                    Intent(context, UserActivity2::class.java)
                                                context.startActivity(intent)
                                            }
                                    )
                                }
                            },
                            navigationIcon = {
                                val context = LocalContext.current
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Back",
                                    modifier = Modifier
                                        .padding(start = 12.dp)
                                        .clickable {
                                            val intent =
                                                Intent(context, MainActivity::class.java)
                                            context.startActivity(intent)
                                        }
                                )
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                ) { innerPadding ->
                    if (hotelDetails != null) {
                        HotelDetailTabs(
                            modifier = Modifier.padding(innerPadding),
                            hotel = hotel,
                            details = hotelDetails
                        )
                    } else {
                        // Show error if file not found
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding),
                            contentAlignment = Alignment.Center
                        ) {

                        }
                    }
                }
            }
        }
    }

    ///tab selection
    @Composable
    fun HotelDetailTabs(modifier: Modifier = Modifier, hotel: Hotel, details: HotelDetails) {
        var selectedTabIndex by remember { mutableStateOf(0) }
        val tabs = listOf("Guest Reviews", "Room Selection")

        Column(modifier = modifier.fillMaxSize()) {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier.fillMaxWidth()
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (selectedTabIndex) {
                0 -> GuestReviewsTab(details.guest_reviews)
                1 -> RoomSelectionTab(hotel, details.rooms) // pass hotel here
            }
        }
    }


    //Guest tab
    @Composable
    fun GuestReviewsTab(guestReviews: GuestReviews) {
        Column(Modifier.padding(16.dp)) {
            Text("Ratings", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))

            guestReviews.ratings_categories.forEach { category ->
                val entry = category.entries.first()
                Text("⭐ ${entry.key}: ${entry.value}")
            }

            Spacer(Modifier.height(16.dp))
            Text("Guest Reviews", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))

            guestReviews.reviews_objects.forEach { review ->
                Text("👤 ${review.username} (${review.country})", fontWeight = FontWeight.Bold)
                Text(review.review_text)
                Spacer(Modifier.height(12.dp))
            }
        }
    }

    //Room selection
    @Composable
    fun RoomSelectionTab(hotel: Hotel, rooms: List<Room>) {

        val context = LocalContext.current

        Column(Modifier.padding(16.dp)) {
            Text("Available Rooms", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))

            rooms.forEach { room ->

                Column(
                    modifier = Modifier
                        .clickable {

                            val hotelJson = Gson().toJson(hotel)
                            val roomJson = Gson().toJson(room)

                            val intent = Intent(context, BookingConfirm::class.java)
                            intent.putExtra("hotel", hotelJson)
                            intent.putExtra("room", roomJson)

                            context.startActivity(intent)
                        }
                        .padding(12.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        "🛏 ${room.room_type} - €${room.room_price_for_one_night}/night",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text("Beds: ${room.room_bed_type}")
                    Text("Guests: ${room.room_total_number_of_guests}")
                    Text("Features: ${room.room_features.joinToString(", ")}")
                }

                Spacer(Modifier.height(16.dp))
            }
        }
    }
}
