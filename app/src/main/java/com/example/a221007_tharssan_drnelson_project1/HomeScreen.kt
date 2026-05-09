package com.example.a221007_tharssan_drnelson_project1

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import coil.compose.rememberAsyncImagePainter
import com.example.a221007_tharssan_drnelson_project1.data.CharityCampaign
import com.example.a221007_tharssan_drnelson_project1.data.charityCampaigns

@Composable
fun HomeScreen(viewModel: DonorViewModel, onNavigate: (String) -> Unit) {
    DonorHomeView(viewModel, onNavigate)
}

@Composable
fun DonorHomeView(viewModel: DonorViewModel, onNavigate: (String) -> Unit) {
    val user = viewModel.currentUser

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            // Impact Dashboard
            Box(
                Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFEA580C))
                    .padding(24.dp)
            ) {
                Column {
                    val firstName = user?.name?.split(" ")?.firstOrNull() ?: "Donor"

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "FeedForward",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "Heart Logo",
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }


                    // Welcome Message
                    Text(
                        text = "Hello $firstName!",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    // Display Matric Number
                    Text(
                        text = "Matric Number: ${user?.matric ?: "A221007"}",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(top = 2.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "Global SDG 2 Progress",
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp
                    )

                    // Impact Counter Card
                    Card(
                        Modifier.padding(top = 8.dp).fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.2f))
                    ) {
                        Row(
                            Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Restaurant, null, tint = Color.White, modifier = Modifier.size(32.dp))
                            Column(Modifier.padding(start = 12.dp)) {
                                Text("Total Meals Shared", color = Color.White, fontSize = 12.sp)
                                Text(
                                    "${viewModel.totalMealsCount}",
                                    color = Color.White,
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }
                    }
                }
            }

            Text(
                text = "ACTIVE CAMPAIGNS",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp),
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        items(charityCampaigns) { campaign ->
            CharityGoalCard(campaign = campaign, onDonate = { onNavigate("donate-food") })
        }

        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}

@Composable
fun CharityGoalCard(campaign: CharityCampaign, onDonate: () -> Unit) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            Box {
                Image(
                    painter = rememberAsyncImagePainter(campaign.imageUrl),
                    contentDescription = null,
                    modifier = Modifier.height(160.dp).fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )

                // Urgent Status Badge
                if (campaign.isUrgent) {
                    Surface(
                        color = Color.Red,
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier.padding(8.dp).align(Alignment.TopEnd)
                    ) {
                        Text(
                            "URGENT",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Surface(
                    color = Color.Black.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(topEnd = 12.dp),
                    modifier = Modifier.align(Alignment.BottomStart)
                ) {
                    Text(
                        text = campaign.organizationName,
                        color = Color.White,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Column(Modifier.padding(16.dp)) {
                Text(campaign.name, fontSize = 20.sp, fontWeight = FontWeight.Bold)

                Text(
                    text = campaign.description,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    maxLines = if (isExpanded) Int.MAX_VALUE else 2,
                    overflow = TextOverflow.Ellipsis
                )

                if (isExpanded) {
                    HorizontalDivider(Modifier.padding(vertical = 12.dp), thickness = 0.5.dp, color = Color.LightGray)

                    Text("Top Wishlist Items:", fontWeight = FontWeight.Bold, color = Color(0xFF9A3412), fontSize = 12.sp)
                    Row(
                        modifier = Modifier.padding(top = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        campaign.wishlist.forEach { item ->
                            SuggestionChip(onClick = {}, label = { Text(item, fontSize = 10.sp) })
                        }
                    }

                    Spacer(Modifier.height(8.dp))
                    Text("Mission & Goals:", fontWeight = FontWeight.Bold, color = Color(0xFF9A3412), fontSize = 14.sp)
                    Text(
                        text = campaign.fullMission,
                        fontSize = 13.sp,
                        color = Color.DarkGray,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    // SDG 2 Factoid Card
                    Surface(
                        color = Color(0xFFFFF7ED),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.padding(top = 12.dp).fillMaxWidth()
                    ) {
                        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Info, null, tint = Color(0xFFEA580C), modifier = Modifier.size(16.dp))
                            Text(
                                text = campaign.factoid,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(start = 8.dp),
                                color = Color(0xFF9A3412),
                                lineHeight = 15.sp
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = { isExpanded = !isExpanded }) {
                        Text(if (isExpanded) "Less" else "Learn More", color = Color(0xFFEA580C))
                        Icon(
                            imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = null,
                            tint = Color(0xFFEA580C)
                        )
                    }

                    Button(
                        onClick = onDonate,
                        colors = ButtonDefaults.buttonColors(Color(0xFFEA580C)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Donate Now")
                    }
                }
            }
        }
    }
}