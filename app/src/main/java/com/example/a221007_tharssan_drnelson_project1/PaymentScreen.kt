package com.example.a221007_tharssan_drnelson_project1

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a221007_tharssan_drnelson_project1.data.FoodDonation
import kotlinx.coroutines.delay

@Composable
fun PaymentScreen(
    totalAmount: String,
    donationData: FoodDonation?,
    onComplete: () -> Unit,
    onCancel: () -> Unit
) {
    var cardNumber by remember { mutableStateOf("") }
    var expiry by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    var isProcessing by remember { mutableStateOf(false) }
    var showReceipt by remember { mutableStateOf(false) }
    var cardError by remember { mutableStateOf(false) }

    LaunchedEffect(isProcessing) {
        if (isProcessing) {
            delay(2000)
            isProcessing = false
            showReceipt = true
        }
    }

    Crossfade(targetState = showReceipt, label = "") { isVisible ->
        if (isVisible) {
            // Receipt
            Column(Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Card(shape = RoundedCornerShape(16.dp), elevation = CardDefaults.cardElevation(4.dp)) {
                    Column(Modifier.padding(24.dp)) {
                        Text("Donation Receipt", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        HorizontalDivider(Modifier.padding(vertical = 12.dp))

                        ReceiptRowItem("Donor", donationData?.donorName ?: "N/A")
                        ReceiptRowItem("Charity", donationData?.charityName ?: "N/A")
                        ReceiptRowItem("Value", totalAmount)

                        Surface(color = Color(0xFFDCFCE7), modifier = Modifier.padding(top = 16.dp).fillMaxWidth()) {
                            Text("Success! Thank you for supporting SDG 2.", Modifier.padding(8.dp), fontSize = 12.sp, color = Color(0xFF14532D))
                        }
                    }
                }
                Button(onClick = onComplete, Modifier.padding(top = 24.dp).fillMaxWidth(), colors = ButtonDefaults.buttonColors(Color(0xFFEA580C))) {
                    Text("Finish")
                }
            }
        } else {
            // Payment Page
            Column(Modifier.fillMaxSize().padding(24.dp).verticalScroll(rememberScrollState()), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Secure Payment", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFFEA580C))

                Surface(color = Color(0xFFFFF7ED), modifier = Modifier.padding(vertical = 24.dp).fillMaxWidth()) {
                    Text(totalAmount, fontSize = 32.sp, fontWeight = FontWeight.Black, color = Color(0xFFEA580C), modifier = Modifier.padding(16.dp))
                }

                OutlinedTextField(
                    value = cardNumber,
                    onValueChange = { if (it.length <= 16) { cardNumber = it; cardError = false } },
                    label = { Text("Card Number (16 Digits)") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = cardError,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    supportingText = { if(cardError) Text("Must be 16 digits") }
                )

                Row(Modifier.padding(top = 12.dp)) {
                    OutlinedTextField(
                        value = expiry,
                        onValueChange = { input ->
                            val clean = input.filter { it.isDigit() }
                            expiry = if (clean.length >= 3) "${clean.take(2)}/${clean.drop(2).take(2)}" else clean
                        },
                        label = { Text("MM/YY") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Spacer(Modifier.width(8.dp))
                    OutlinedTextField(
                        value = cvv,
                        onValueChange = { if (it.length <= 3) cvv = it },
                        label = { Text("CVV") },
                        modifier = Modifier.weight(1f),
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }

                if (isProcessing) {
                    CircularProgressIndicator(Modifier.padding(top = 32.dp), color = Color(0xFFEA580C))
                } else {
                    Button(
                        onClick = { if (cardNumber.length == 16) isProcessing = true else cardError = true },
                        modifier = Modifier.padding(top = 32.dp).fillMaxWidth().height(50.dp),
                        colors = ButtonDefaults.buttonColors(Color(0xFFEA580C))
                    ) { Text("Confirm Payment") }
                    TextButton(onClick = onCancel) { Text("Cancel", color = Color.Gray) }
                }
            }
        }
    }
}

@Composable
fun ReceiptRowItem(label: String, value: String) {
    Row(Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = Color.Gray)
        Text(value, fontWeight = FontWeight.Bold)
    }
}