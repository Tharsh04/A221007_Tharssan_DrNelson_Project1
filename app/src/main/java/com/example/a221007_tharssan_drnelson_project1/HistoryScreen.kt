package com.example.a221007_tharssan_drnelson_project1

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a221007_tharssan_drnelson_project1.data.FoodDonation

@Composable
fun HistoryScreen(viewModel: DonorViewModel) {
    val history = viewModel.donationHistory

    Column(Modifier.fillMaxSize()) {
        // Header
        Box(Modifier.fillMaxWidth().background(Color(0xFFEA580C)).padding(24.dp)) {
            Text("My Impact History", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        if (history.isEmpty()) {
            // Empty State
            Column(
                Modifier.fillMaxSize().padding(32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(Icons.Default.History, null, modifier = Modifier.size(80.dp), tint = Color.LightGray)
                Text(
                    "No donations yet",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 16.dp)
                )
                Text(
                    "Every contribution helps fight hunger in Malaysia. Start your journey today!",
                    textAlign = TextAlign.Center,
                    color = Color.Gray,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        } else {
            // List of Donations
            LazyColumn(
                Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        "Total Contributions: ${history.size}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }

                items(history.reversed()) { donation -> // Show newest first
                    DonationHistoryCard(donation)
                }

                item { Spacer(Modifier.height(80.dp)) }
            }
        }
    }
}

@Composable
fun DonationHistoryCard(donation: FoodDonation) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {

            Box(
                Modifier.size(48.dp).background(Color(0xFFFFF7ED), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.VolunteerActivism, null, tint = Color(0xFFEA580C))
            }

            Column(Modifier.padding(start = 16.dp).weight(1f)) {
                Text(donation.charityName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(donation.packageType, fontSize = 13.sp, color = Color.Gray)
                Text("Date: ${donation.date}", fontSize = 11.sp, color = Color.LightGray)
            }

            Column(horizontalAlignment = Alignment.End) {
                Text("+${donation.totalItemCount}", fontWeight = FontWeight.Black, color = Color(0xFF16A34A), fontSize = 18.sp)
                Text("items", fontSize = 10.sp, color = Color.Gray)
            }
        }
    }
}