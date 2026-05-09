package com.example.a221007_tharssan_drnelson_project1

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import com.example.a221007_tharssan_drnelson_project1.data.FoodDonation
import com.example.a221007_tharssan_drnelson_project1.data.charityCampaigns
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

data class FoodPackageItem(val id: String, val name: String, val description: String, val items: List<String>, val color: Color)

@Composable
fun DonateFoodScreen(
    donorName: String,
    donorEmail: String,
    onDonate: (FoodDonation) -> Unit,
    onBack: () -> Unit
) {
    var selectedPackage by remember { mutableStateOf<String?>(null) }
    var selectedCharity by remember { mutableStateOf("") }
    var itemQuantities by remember { mutableStateOf<Map<String, Int>>(emptyMap()) }

    val foodPackages = listOf(
        FoodPackageItem("emergency", "Emergency Food Package", "Essential items for immediate relief", listOf("Canned goods", "Rice & pasta", "Dried beans", "Bottled water"), Color(0xFFDC2626)),
        FoodPackageItem("basic", "Basic Grocery Package", "Weekly staples for a family", listOf("Fresh vegetables", "Fruits", "Bread", "Milk", "Eggs"), Color(0xFF16A34A)),
        FoodPackageItem("baby", "Baby Food Package", "Nutrition essentials for infants", listOf("Infant formula", "Baby cereal", "Pureed fruits", "Baby snacks"), Color(0xFFDB2777))
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") }
                Text("Back", color = Color.Gray)
            }
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Box(modifier = Modifier.size(60.dp).background(Color(0xFFEA580C), CircleShape), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.CardGiftcard, null, tint = Color.White, modifier = Modifier.size(32.dp))
                }
                Text("Donate Food Packages", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text("Step 1: Customise your donation", fontSize = 14.sp, color = Color.Gray)
            }
        }

        item {
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Select Charity", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    charityCampaigns.forEach { charity ->
                        Row(Modifier.fillMaxWidth().clickable { selectedCharity = charity.id }.padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(selected = selectedCharity == charity.id, onClick = { selectedCharity = charity.id }, colors = RadioButtonDefaults.colors(selectedColor = Color(0xFFEA580C)))
                            Text(charity.organizationName, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }
        }

        item { Text("Choose Package Type", fontWeight = FontWeight.Bold, fontSize = 14.sp) }

        items(foodPackages.size) { index ->
            val pkg = foodPackages[index]
            val isSelected = selectedPackage == pkg.id
            Card(
                modifier = Modifier.fillMaxWidth().clickable { selectedPackage = pkg.id; itemQuantities = pkg.items.associateWith { 1 } },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = if (isSelected) pkg.color.copy(alpha = 0.1f) else Color.White)
            ) {
                Row(modifier = Modifier.fillMaxWidth().border(2.dp, if (isSelected) pkg.color else Color.Transparent, RoundedCornerShape(12.dp)).padding(16.dp)) {
                    Column(Modifier.weight(1f)) {
                        Text(pkg.name, fontWeight = FontWeight.Bold)
                        Text(pkg.description, fontSize = 12.sp, color = Color.Gray)
                    }
                    if (isSelected) Icon(Icons.Default.CheckCircle, null, tint = pkg.color)
                }
            }
        }

        item {
            val currentPkg = foodPackages.find { it.id == selectedPackage }
            if (currentPkg != null && selectedCharity.isNotEmpty()) {
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Customize Package Contents", fontWeight = FontWeight.Bold)
                        currentPkg.items.forEach { item ->
                            Row(Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Text(item, modifier = Modifier.weight(1f))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(onClick = {
                                        val c = itemQuantities[item] ?: 1
                                        if (c > 1) itemQuantities = itemQuantities + (item to c - 1)
                                    }) { Icon(Icons.Default.Remove, null) }
                                    Text((itemQuantities[item] ?: 1).toString(), fontWeight = FontWeight.Bold)
                                    IconButton(onClick = {
                                        val c = itemQuantities[item] ?: 1
                                        itemQuantities = itemQuantities + (item to c + 1)
                                    }) { Icon(Icons.Default.Add, null) }
                                }
                            }
                        }
                    }
                }
                Button(
                    onClick = {
                        val charity = charityCampaigns.find { it.id == selectedCharity } ?: return@Button
                        onDonate(
                            FoodDonation(
                                id = UUID.randomUUID().toString(),
                                donorName = donorName,
                                donorEmail = donorEmail,
                                charityName = charity.organizationName,
                                packageType = currentPkg.name,
                                items = itemQuantities,
                                totalItemCount = itemQuantities.values.sum(),
                                date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                                    Date()
                                )
                            )
                        )
                    },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFFEA580C)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Proceed to Secure Payment →", modifier = Modifier.padding(8.dp))
                }
            }
        }
    }
}

@Composable
fun ImpactSummaryScreen(donation: FoodDonation?, onFinish: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(shape = RoundedCornerShape(24.dp), elevation = CardDefaults.cardElevation(8.dp)) {
            Column(Modifier.padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.ReceiptLong, null, tint = Color(0xFFEA580C), modifier = Modifier.size(64.dp))
                Text("Donation Receipt", fontSize = 22.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 16.dp))

                HorizontalDivider(Modifier.padding(vertical = 16.dp), thickness = 1.dp, color = Color.LightGray)

                ReceiptRow("Donor Name", donation?.donorName ?: "")
                ReceiptRow("Charity", donation?.charityName ?: "")
                ReceiptRow("Items Shared", "${donation?.totalItemCount} Units")

                Surface(color = Color(0xFFDCFCE7), shape = RoundedCornerShape(12.dp), modifier = Modifier.padding(top = 24.dp)) {
                    Text("Transaction Successful! Your contribution supports SDG 2: Zero Hunger.", modifier = Modifier.padding(16.dp), fontSize = 12.sp, textAlign = TextAlign.Center, color = Color(0xFF14532D))
                }
            }
        }
        Button(
            onClick = onFinish,
            modifier = Modifier.padding(top = 32.dp).fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(Color(0xFFEA580C))
        ) { Text("Finish & Go Home") }
    }
}

@Composable
fun ReceiptRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = Color.Gray, fontSize = 14.sp)
        Text(value, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }
}