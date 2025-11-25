package ph.edu.comteq.pinili_alphshotel

data class Booking(
    val firstName: String,
    val lastName: String,
    val checkIn: String,
    val checkOut: String,
    val adults: Int,
    val children: Int,
    val rooms: Int,
    val travelType: String,
    val paymentMethod: String,
    val totalPrice: Double
)
