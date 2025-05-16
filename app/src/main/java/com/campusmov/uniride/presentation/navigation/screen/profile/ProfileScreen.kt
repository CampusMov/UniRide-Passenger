package com.campusmov.uniride.presentation.navigation.screen.profile

sealed class ProfileScreen(val route: String) {
    object RegisterProfileFullName     : ProfileScreen("register_full_name")
    object RegisterProfileTerms        : ProfileScreen("register_terms_and_conditions")
    object RegisterProfileListItems    : ProfileScreen("register_list_items")
    object RegisterProfilePersonalInfo : ProfileScreen("register_personal_info")
    object RegisterProfileContactInfo  : ProfileScreen("register_contact_info")
    object RegisterProfileAcademicInfo : ProfileScreen("register_academic_info")
    object ProfileInfo                 : ProfileScreen("profile_info")
}
