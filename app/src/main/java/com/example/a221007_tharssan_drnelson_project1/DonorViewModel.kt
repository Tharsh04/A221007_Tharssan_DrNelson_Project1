package com.example.a221007_tharssan_drnelson_project1

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.a221007_tharssan_drnelson_project1.data.FoodDonation
import com.example.a221007_tharssan_drnelson_project1.data.User
import com.example.a221007_tharssan_drnelson_project1.data.charityCampaigns

class DonorViewModel : ViewModel() {


    var registeredUser by mutableStateOf<User?>(null)
    var currentUser by mutableStateOf<User?>(null)

    // Global list of donations
    val donationHistory = mutableStateListOf<FoodDonation>()

    // Tracks the very last donation
    var lastDonation by mutableStateOf<FoodDonation?>(null)

    // Calculation for the Impact Dashboard
    val totalMealsCount: Int
        get() {
            val initial = charityCampaigns.sumOf { it.mealsShared }
            val session = donationHistory.sumOf { it.totalItemCount }
            return initial + session
        }

    fun register(user: User) {
        registeredUser = user
    }

    fun login(email: String, pass: String): Boolean {
        return if (registeredUser?.email == email && registeredUser?.password == pass) {
            currentUser = registeredUser
            true
        } else false
    }

    fun logout() {
        currentUser = null
    }

    // Updates profile details
    fun updateProfile(newName: String, newEmail: String, newMatric: String) {
        currentUser?.let { user ->
            currentUser = user.copy(
                name = newName,
                email = newEmail,
                matric = newMatric
            )
        }
    }

    fun addDonation(donation: FoodDonation) {
        donationHistory.add(donation)
        lastDonation = donation
    }
}