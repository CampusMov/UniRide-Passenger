package com.campusmov.uniride.presentation.navigation.screen.profile

sealed class ProfileScreen(val route: String) {
    object RegisterProfileFullName: ProfileScreen("profile/register/full_name")
    object RegisterProfileAcceptTerms: ProfileScreen("profile/register/accept_terms")
    object RegisterProfilePersonalInfo: ProfileScreen("profile/register/personal_info")
    object RegisterProfileContactInfo: ProfileScreen("profile/register/contact_info")
    object RegisterProfileAcademicInfo: ProfileScreen("profile/register/academic_info")
    object RegisterProfileListItems: ProfileScreen("profile/register/list_items")
    object ProfileInfo: ProfileScreen("profile/info")
}