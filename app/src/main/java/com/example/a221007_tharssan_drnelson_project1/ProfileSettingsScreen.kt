package com.example.a221007_tharssan_drnelson_project1

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*

@Composable
fun ProfileSettingsScreen(
    donorName: String,
    donorEmail: String,
    donorMatric: String,
    onSaveProfile: (String, String, String) -> Unit,
    onLogout: () -> Unit,
    onBack: () -> Unit
) {

    var isEditing by remember { mutableStateOf(false) }
    var tempName by remember { mutableStateOf(donorName) }
    var tempEmail by remember { mutableStateOf(donorEmail) }
    var tempMatric by remember { mutableStateOf(donorMatric) }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") }

        Column(Modifier.fillMaxWidth().padding(vertical = 24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(Modifier.size(80.dp).background(Color(0xFFEA580C), CircleShape), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Person, null, tint = Color.White, modifier = Modifier.size(40.dp))
            }
            Text("My Profile", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            if (isEditing) {
                Text("Editing Mode", color = Color(0xFFEA580C), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
            Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {

                // Name Field
                EditableProfileRow(
                    icon = Icons.Default.Person,
                    label = "Name",
                    value = tempName,
                    isEditing = isEditing,
                    onValueChange = { tempName = it }
                )

                // Email Field
                EditableProfileRow(
                    icon = Icons.Default.Email,
                    label = "Email",
                    value = tempEmail,
                    isEditing = isEditing,
                    onValueChange = { tempEmail = it }
                )

                // Matric Field
                EditableProfileRow(
                    icon = Icons.Default.Badge,
                    label = "Matric",
                    value = tempMatric,
                    isEditing = isEditing,
                    onValueChange = { tempMatric = it }
                )

                Spacer(Modifier.height(16.dp))

                // Button (Edit/Save)
                Button(
                    onClick = {
                        if (isEditing) {
                            onSaveProfile(tempName, tempEmail, tempMatric)
                        }
                        isEditing = !isEditing
                    },
                    Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(Color(0xFFEA580C)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(if (isEditing) Icons.Default.Save else Icons.Default.Edit, null)
                    Spacer(Modifier.width(8.dp))
                    Text(if (isEditing) "Save Changes" else "Edit Profile Details")
                }

                if (!isEditing) {
                    OutlinedButton(
                        onClick = onLogout,
                        Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Logout Account")
                    }
                }
            }
        }
    }
}

@Composable
fun EditableProfileRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    isEditing: Boolean,
    onValueChange: (String) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Icon(icon, null, tint = Color.Gray, modifier = Modifier.size(24.dp))
        Column(Modifier.padding(start = 12.dp).weight(1f)) {
            Text(label, fontSize = 11.sp, color = Color.Gray)
            if (isEditing) {
                OutlinedTextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = LocalTextStyle.current.copy(fontSize = 14.sp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFEA580C),
                        unfocusedBorderColor = Color.LightGray
                    )
                )
            } else {
                Text(value, fontWeight = FontWeight.Medium, fontSize = 16.sp)
            }
        }
    }
}