package ph.edu.comteq.pinili_alphshotel

data class Hotel(
    val hotel_id: Int,
    val hotel_name: String,
    val hotel_rating: Double,
    val hotel_to_ski_distance: Double,
    val hotel_cover_image: String,

    // Added so HotelDetailActivity can use them after loading details JSON
    val guest_reviews: GuestReviews? = null,
    val rooms: List<Room>? = null
)
