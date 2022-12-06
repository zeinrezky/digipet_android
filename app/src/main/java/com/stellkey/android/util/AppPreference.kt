package com.stellkey.android.util

import com.orhanobut.hawk.Hawk
import com.stellkey.android.helper.extension.emptyBoolean
import com.stellkey.android.helper.extension.emptyInt
import com.stellkey.android.helper.extension.emptyString

class AppPreference {

    companion object {
        private const val isFirstTime = "isFirstTime"
        private const val loginToken = "loginToken"
        private const val mainCarerLoginToken = "mainCarerLoginToken"
        private const val accountId = "accountId"
        private const val tempCarerToken = "tempCarerToken"
        private const val tempKidToken = "tempKidToken"
        private const val tempChildName = "tempChildName"
        private const val tempChildProfilePIN = "tempChildProfilePIN"
        private const val tempChildAge = "tempChildAge"
        private const val tempChildId = "tempChildId"
        private const val tempSelectedGlobalChallengeId = "tempSelectedGlobalChallengeId"
        private const val profileIconId = "profileIconId"
        private const val profileIcon = "profileIcon"
        private const val profileType = "profileType"
        private const val tempEmail = "tempEmail"
        private const val tempPassword = "tempPassword"
        private const val tempLocale = "tempLocale"
        private const val tempTimezone = "tempTimezone"
        private const val isCompleteLogin = "isCompleteLogin"
        private const val tempCarerName = "tempCarerName"
        private const val tempCarerPIN = "tempCarerPIN"
        private const val selectedCarerId = "selectedCarerId"
        private const val isEditProfile = "isEditProfile"
        private const val tempProfileList = "tempProfileList"
        private const val isKidLogin = "isKidLogin"

        //Add Task Temp Data
        private const val tempSelectedChallengeCategoryId = "tempSelectedChallengeCategoryId"
        private const val tempSelectedChallengeName = "tempSelectedChallengeName"
        private const val tempSelectedChallengeIcon = "tempSelectedChallengeIcon"
        private const val tempStartDate = "tempStartDate"
        private const val hasActiveCycle = "hasActiveCycle"

        fun deleteAll() {
            Hawk.delete(isFirstTime)
            Hawk.delete(loginToken)
            Hawk.delete(mainCarerLoginToken)
            Hawk.delete(accountId)
            Hawk.delete(tempCarerToken)
            Hawk.delete(tempKidToken)
            Hawk.delete(tempChildName)
            Hawk.delete(tempChildProfilePIN)
            Hawk.delete(tempChildAge)
            Hawk.delete(tempChildId)
            Hawk.delete(tempSelectedGlobalChallengeId)
            Hawk.delete(profileIconId)
            Hawk.delete(profileIcon)
            Hawk.delete(profileType)
            Hawk.delete(tempEmail)
            Hawk.delete(tempPassword)
            Hawk.delete(tempLocale)
            Hawk.delete(tempTimezone)
            Hawk.delete(isCompleteLogin)
            Hawk.delete(selectedCarerId)
            Hawk.delete(isEditProfile)
            Hawk.delete(tempProfileList)
            Hawk.delete(isKidLogin)
        }

        fun deleteProfileIcon() {
            Hawk.delete(profileIconId)
            Hawk.delete(profileIcon)
        }

        fun deleteTempLoginData() {
            Hawk.delete(tempEmail)
            Hawk.delete(tempPassword)
            Hawk.delete(tempLocale)
            Hawk.delete(tempTimezone)
            Hawk.delete(tempChildName)
            Hawk.delete(tempChildProfilePIN)
            Hawk.delete(tempChildAge)
            Hawk.delete(tempChildId)
            Hawk.delete(tempSelectedGlobalChallengeId)
            Hawk.delete(profileIconId)
            Hawk.delete(profileIcon)
        }

        fun deleteTempCreateMemberData() {
            Hawk.delete(profileIconId)
            Hawk.delete(profileIcon)
            Hawk.delete(profileType)
            Hawk.delete(tempChildName)
            Hawk.delete(tempChildProfilePIN)
            Hawk.delete(tempChildAge)
            Hawk.delete(tempChildId)
            Hawk.delete(tempSelectedGlobalChallengeId)
            Hawk.delete(tempCarerName)
            Hawk.delete(tempCarerPIN)
            Hawk.delete(selectedCarerId)
        }

        fun deleteAddTaskData() {
            Hawk.delete(tempSelectedGlobalChallengeId)
            Hawk.delete(tempSelectedChallengeCategoryId)
            Hawk.delete(tempSelectedChallengeName)
            Hawk.delete(tempSelectedChallengeIcon)
            Hawk.delete(tempChildId)
            Hawk.delete(tempStartDate)
            Hawk.delete(hasActiveCycle)
        }

        fun putLoginToken(value: String) {
            Hawk.put(loginToken, value)
        }

        fun getLoginToken(): String {
            return (Hawk.get(loginToken, emptyString))
        }

        fun putMainCarerLoginToken(value: String) {
            Hawk.put(mainCarerLoginToken, value)
        }

        fun getMainCarerLoginToken(): String {
            return (Hawk.get(mainCarerLoginToken, emptyString))
        }

        fun putCarerToken(value: String) {
            Hawk.put(tempCarerToken, value)
        }

        fun getCarerToken(): String {
            return (Hawk.get(tempCarerToken, emptyString))
        }

        fun putKidToken(value: String) {
            Hawk.put(tempKidToken, value)
        }

        fun getKidToken(): String {
            return (Hawk.get(tempKidToken, emptyString))
        }

        fun putFirstTime(value: Boolean) {
            Hawk.put(isFirstTime, value)
        }

        fun isFirstTime(): Boolean {
            return (Hawk.get(isFirstTime, emptyBoolean))
        }

        fun putAccountId(value: Int) {
            Hawk.put(accountId, value)
        }

        fun getAccountId(): Int {
            return (Hawk.get(accountId, emptyInt))
        }

        fun putTempChildName(value: String) {
            Hawk.put(tempChildName, value)
        }

        fun getTempChildName(): String {
            return (Hawk.get(tempChildName, emptyString))
        }

        fun putTempChildProfilePIN(value: String) {
            Hawk.put(tempChildProfilePIN, value)
        }

        fun getTempChildProfilePIN(): String {
            return (Hawk.get(tempChildProfilePIN, emptyString))
        }

        fun putTempChildAge(value: Int) {
            Hawk.put(tempChildAge, value)
        }

        fun getTempChildAge(): Int {
            return (Hawk.get(tempChildAge, emptyInt))
        }

        fun putTempChildId(value: Int) {
            Hawk.put(tempChildId, value)
        }

        fun getTempChildId(): Int {
            return (Hawk.get(tempChildId, emptyInt))
        }

        fun putTempSelectedGlobalId(value: Int) {
            Hawk.put(tempSelectedGlobalChallengeId, value)
        }

        fun getTempSelectedGlobalId(): Int {
            return (Hawk.get(tempSelectedGlobalChallengeId, emptyInt))
        }

        fun putProfileIconId(value: Int) {
            Hawk.put(profileIconId, value)
        }

        fun getProfileIconId(): Int {
            return (Hawk.get(profileIconId, emptyInt))
        }

        fun putProfileIcon(value: String) {
            Hawk.put(profileIcon, value)
        }

        fun getProfileIcon(): String {
            return (Hawk.get(profileIcon, emptyString))
        }

        fun putProfileType(value: String) {
            Hawk.put(profileType, value)
        }

        fun getProfileType(): String {
            return (Hawk.get(profileType, emptyString))
        }

        fun putTempEmail(value: String) {
            Hawk.put(tempEmail, value)
        }

        fun getTempEmail(): String {
            return (Hawk.get(tempEmail, emptyString))
        }

        fun putTempPassword(value: String) {
            Hawk.put(tempPassword, value)
        }

        fun getTempPassword(): String {
            return (Hawk.get(tempPassword, emptyString))
        }

        fun putTempLocale(value: String) {
            Hawk.put(tempLocale, value)
        }

        fun getTempLocale(): String {
            return (Hawk.get(tempLocale, emptyString))
        }

        fun putTempTimezone(value: Int) {
            Hawk.put(tempTimezone, value)
        }

        fun getTempTimezone(): Int {
            return (Hawk.get(tempTimezone, emptyInt))
        }

        fun putCompleteLogin(value: Boolean) {
            Hawk.put(isCompleteLogin, value)
        }

        fun isCompleteLogin(): Boolean {
            return (Hawk.get(isCompleteLogin, emptyBoolean))
        }

        fun putTempCarerPIN(value: String) {
            Hawk.put(tempCarerPIN, value)
        }

        fun getTempCarerPIN(): String {
            return (Hawk.get(tempCarerPIN, emptyString))
        }

        fun putTempCarerName(value: String) {
            Hawk.put(tempCarerName, value)
        }

        fun getTempCarerName(): String {
            return (Hawk.get(tempCarerName, emptyString))
        }

        fun putSelectedCarerId(value: Int) {
            Hawk.put(selectedCarerId, value)
        }

        fun getSelectedCarerId(): Int {
            return (Hawk.get(selectedCarerId, emptyInt))
        }

        fun putEditProfile(value: Boolean) {
            Hawk.put(isEditProfile, value)
        }

        fun isEditProfile(): Boolean {
            return (Hawk.get(isEditProfile, emptyBoolean))
        }

        fun putTempSelectedCategoryId(value: Int) {
            Hawk.put(tempSelectedChallengeCategoryId, value)
        }

        fun getTempSelectedCategoryId(): Int {
            return (Hawk.get(tempSelectedChallengeCategoryId, emptyInt))
        }

        fun putTempSelectedChallengeName(value: String) {
            Hawk.put(tempSelectedChallengeName, value)
        }

        fun getTempSelectedChallengeName(): String {
            return (Hawk.get(tempSelectedChallengeName, emptyString))
        }

        fun putTempSelectedChallengeIcon(value: String) {
            Hawk.put(tempSelectedChallengeIcon, value)
        }

        fun getTempSelectedChallengeIcon(): String {
            return (Hawk.get(tempSelectedChallengeIcon, emptyString))
        }

        fun putTempChallengeStartDate(value: String) {
            Hawk.put(tempStartDate, value)
        }

        fun getTempChallengeStartDate(): String {
            return (Hawk.get(tempStartDate, emptyString))
        }

        fun putActiveCycle(value: Boolean) {
            Hawk.put(hasActiveCycle, value)
        }

        fun hasActiveCycle(): Boolean {
            return (Hawk.get(hasActiveCycle, emptyBoolean))
        }

        fun putKidLogin(value: Boolean) {
            Hawk.put(isKidLogin, value)
        }

        fun isKidLogin(): Boolean {
            return (Hawk.get(isKidLogin, emptyBoolean))
        }
    }
}