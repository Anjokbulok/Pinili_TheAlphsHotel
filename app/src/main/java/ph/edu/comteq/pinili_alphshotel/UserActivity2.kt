package ph.edu.comteq.pinili_alphshotel

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ph.edu.comteq.pinili_alphshotel.ui.theme.Pinili_AlphsHotelTheme

class UserActivity2 : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
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
                                        //Title
                                        Text(
                                            text = "The Alphs Hotel",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 20.sp
                                        )
                                        // Logo
                                        Image(
                                            painter = painterResource(id = R.drawable.france_national_flag),
                                            contentDescription = "Flag",
                                            modifier = Modifier
                                                .width(40.dp)
                                                .padding(start = 8.dp)
                                        )
                                    }

                                    //UserIcon
                                    Icon(
                                        imageVector = Icons.Outlined.Person,
                                        contentDescription = "User",
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                            },
                            //Navigation to MainActivity
                            navigationIcon = {
                                val context = LocalContext.current
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
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.primary
                            )
                        )

                    }
                ) { innerPadding ->
                    Profile(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Profile(modifier: Modifier = Modifier) {
    //To horizotal alignment and to center content
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Profile picture
        Image(
            painter = painterResource(id = R.drawable.profile), // Make sure it's named profile.jpg in drawable
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(140.dp)
                .clip(RoundedCornerShape(80.dp))
        )

        // Name
        Text(
            text = "Adrian Christian Pinili",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp)
        )

        // Title
        Text(
            text = "Student",
            fontSize = 18.sp,
            color = Color.Gray,
            modifier = Modifier.padding(top = 4.dp)
        )

        // Description
        Text(
            text = "Adrian Chrstian Pinili taking Bachelor of Science in Information Technology with Subject of Mobile Applications Development (Android)",
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier
                .padding(top = 16.dp, start = 24.dp, end = 24.dp),
            lineHeight = 22.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Pinili_AlphsHotelTheme {
        Profile()
    }
}