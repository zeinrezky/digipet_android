package com.stellkey.android.repository

import com.haroldadmin.cnradapter.NetworkResponse
import com.stellkey.android.model.*
import com.stellkey.android.model.request.*
import com.stellkey.android.util.Constant
import retrofit2.http.*

interface UserService {

    /* Register Routes */
    @POST("register")
    suspend fun register(@Body register: RegisterEmailRequest): NetworkResponse<ResponseSuccess<RegisterModel>, ResponseError>

    @POST("confirm/resend")
    suspend fun resendCode(@Body resendCode: ResendPINRequest): NetworkResponse<ResponseSuccess<ResendCodeModel>, ResponseError>

    @POST("confirm")
    suspend fun confirmAccount(@Body confirmAccount: RegisterConfirmRequest): NetworkResponse<ConfirmAccountModel, ResponseError>

    @GET("profile-icons")
    suspend fun allProfileIcons(): NetworkResponse<ProfileIconModel, ResponseError>

    @POST("carer/carers/setup")
    suspend fun setupDefaultCarer(
        @Header("Authorization") carerToken: String,
        @Body defaultCarerRequest: DefaultCarerRequest
    ): NetworkResponse<ResponseSuccess<Any>, ResponseError>

    @PUT("carer/carers/changepin")
    suspend fun editMainCarerPIN(
        @Header("Authorization") carerToken: String,
        @Body editMainCarerPINRequest: EditMainCarerPINRequest
    ): NetworkResponse<ResponseSuccess<EditMainCarerPINModel>, ResponseError>

    @POST("carer/kids/create")
    suspend fun createKid(
        @Header("Authorization") carerToken: String,
        @Body createChildRequest: CreateChildRequest
    ): NetworkResponse<ResponseSuccess<CreateKidModel>, ResponseError>

    @GET("carer/global-challenges")
    suspend fun globalChallenge(
        @Header("Authorization") carerToken: String,
        @Query("limit") limit: Int?,
        @Query("page") page: Int?,
        @Query("keyword") keyword: String?,
        @Query("order") order: String?,
        @Query("age") age: Int?,
    ): NetworkResponse<ResponseSuccess<ArrayList<KidGlobalChallengeModel>>, ResponseError>

    @POST("carer/assignments/create-many")
    suspend fun createAssignment(
        @Header("Authorization") carerToken: String,
        @Body createAssignmentRequest: CreateAssignmentRequest
    ): NetworkResponse<ResponseSuccess<Any>, ResponseError>
    /* End of Register Routes */

    /* Login Routes */
    @POST("login")
    suspend fun registerLogin(@Body loginRequest: LoginRequest): NetworkResponse<ResponseSuccess<LoginModel>, ResponseError>

    @POST("carer/login")
    suspend fun carerLogin(@Body carerLoginRequest: CarerLoginRequest): NetworkResponse<ResponseSuccess<Any>, ResponseError>

    @POST("kid/login")
    suspend fun kidLogin(@Body kidLoginRequest: KidLoginRequest): NetworkResponse<ResponseSuccess<LoginModel>, ResponseError>

    @GET("profiles/{param}")
    suspend fun allProfileSelection(@Path("param") loginToken: String): NetworkResponse<ResponseSuccess<AllProfileModel>, ResponseError>
    /* End of Login Routes */

    /* Tasks Routes */
    @GET("carer/assignments/cycle/{param}")
    suspend fun currentCycleAssignment(@Path("param") profileId: Int): NetworkResponse<ResponseSuccess<AllKidsModel.Assignments>, ResponseError>

    @GET("carer/assignments/today/{param}")
    suspend fun todayAssignment(@Path("param") profileId: Int): NetworkResponse<ResponseSuccess<AllKidsModel.Assignments>, ResponseError>

    @GET("carer/assignments/yesterday/{param}")
    suspend fun yesterdayAssignment(@Path("param") profileId: Int): NetworkResponse<ResponseSuccess<AllKidsModel.Assignments>, ResponseError>

    @POST("carer/assignments/confirm")
    suspend fun confirmAssignmentCompletion(@Body assignmentRequest: AssignmentActionRequest): NetworkResponse<ResponseSuccess<Any>, ResponseError>

    @POST("carer/assignments/confirm-without-completion")
    suspend fun confirmAssignmentWithoutCompletion(@Body assignmentRequest: AssignmentActionRequest): NetworkResponse<ResponseSuccess<Any>, ResponseError>

    @POST("carer/assignments/decline")
    suspend fun declineAssignmentCompletion(@Body assignmentRequest: AssignmentActionRequest): NetworkResponse<ResponseSuccess<Any>, ResponseError>

    @POST("carer/assignments/decline-without-completion")
    suspend fun declineAssignmentWithoutCompletion(@Body assignmentRequest: AssignmentActionRequest): NetworkResponse<ResponseSuccess<Any>, ResponseError>

    @HTTP(method = "DELETE", path = "carer/assignments", hasBody = true)
    suspend fun deleteKidAssignment(@Body deleteKidTaskRequest: DeleteChildTaskRequest): NetworkResponse<ResponseSuccess<Any>, ResponseError>

    @GET("carer/global-challenges/grouped")
    suspend fun groupedChallenges(@Query("age") age: Int): NetworkResponse<ResponseSuccess<GroupedChallengesModel>, ResponseError>

    @GET("carer/challenges/{param}")
    suspend fun challengeDetail(@Path("param") challengeId: Int): NetworkResponse<ResponseSuccess<Any>, ResponseError>

    @GET("carer/challenges/categories")
    suspend fun challengeCategory(): NetworkResponse<ResponseSuccess<ArrayList<ChallengeCategoryModel>>, ResponseError>

    @POST("carer/challenges")
    suspend fun postNewCustomChallenge(@Body newCustomTaskRequest: CustomTaskRequest): NetworkResponse<ResponseSuccess<CustomTaskModel>, ResponseError>

    @GET("carer/challenges")
    suspend fun getCustomChallenge(): NetworkResponse<ResponseSuccess<List<CustomChallengeModel>>, ResponseError>

    @PUT("carer/challenges")
    suspend fun putCustomChallenge(@Body request: EditCustomChallenge): NetworkResponse<ResponseSuccess<CustomTaskModel>, ResponseError>

    @DELETE("carer/challenges/{param}")
    suspend fun deleteCustomChallenge(@Path("param") idChallenge: Int): NetworkResponse<ResponseSuccess<ResponseSuccess.SuccessDelete>, ResponseError>

    /* End of Tasks Routes */

    /* Rewards Routes */
    @GET("carer/rewards")
    suspend fun listReward(
        @Query("kid_id") kid_id: Int,
        @Query("star_cost") star_cost: Int?
    ): NetworkResponse<ResponseSuccess<List<RewardModel>>, ResponseError>

    @GET("carer/rewards/global")
    suspend fun listGlobalReward(): NetworkResponse<ResponseSuccess<RewardListModel>, ResponseError>

    @POST("carer/rewards/")
    suspend fun createReward(
        @Body createRewardRequest: CreateRewardRequest
    ): NetworkResponse<ResponseSuccess<Any>, ResponseError>

    @POST("carer/rewards/addcustom")
    suspend fun assignCustomRewardForKids(
        @Body request: CustomRewardAssignKidRequest
    ): NetworkResponse<ResponseSuccess<Any>, ResponseError>

    @POST("carer/rewards/addglobal")
    suspend fun assignGlobalRewardForKids(
        @Body request: GlobalRewardAssignKidRequest
    ): NetworkResponse<ResponseSuccess<Any>, ResponseError>

    @POST("carer/rewards/removeglobal")
    suspend fun removeGlobalRewardFromKids(
        @Body request: GlobalRewardAssignKidRequest
    ) : NetworkResponse<ResponseSuccess<Any>, ResponseError>

    @POST("carer/rewards/removecustom")
    suspend fun removeCustomRewardFromKids(
        @Body request: CustomRewardAssignKidRequest
    ) : NetworkResponse<ResponseSuccess<Any>, ResponseError>

    /* End of Rewards Routes */

    /* Kids Routes */
    @GET("/carer/kids")
    suspend fun listAllKids(): NetworkResponse<ResponseSuccess<ArrayList<AllKidsModel>>, ResponseError>

    @GET("/carer/kids/{param}")
    suspend fun detailKid(@Path("param") profileId: Int): NetworkResponse<ResponseSuccess<AllKidsModel>, ResponseError>

    @DELETE("carer/kids/{param}")
    suspend fun deleteKid(
        @Path("param") carerId: Int
    ): NetworkResponse<ResponseSuccess<Any>, ResponseError>

    @PUT("/carer/kids/{param}")
    suspend fun editKid(
        @Path("param") profileId: Int,
        @Body editKidRequest: EditKidRequest
    ): NetworkResponse<ResponseSuccess<Any>, ResponseError>

    @PUT("carer/kids/{param}/pin")
    suspend fun editKidPIN(
        @Path("param") profileId: Int,
        @Body editProfilePINRequest: EditProfilePINRequest
    ): NetworkResponse<ResponseSuccess<Any>, ResponseError>

    @GET("/kid/info")
    suspend fun kidInfo(): NetworkResponse<ResponseSuccess<KidInfoModel>, ResponseError>

    @POST("/kid/assignments/complete")
    suspend fun completeKidTask(@Body kidCompleteTaskRequest: KidCompleteTaskRequest): NetworkResponse<ResponseSuccess<Any>, ResponseError>
    /* End of Kids Routes */

    /* Carers Routes */
    @GET("/carer/carers")
    suspend fun listAllCarers(): NetworkResponse<ResponseSuccess<ArrayList<AllCarersModel>>, ResponseError>

    @POST("carer/carers")
    suspend fun createCarer(
        @Body createCarerRequest: CreateCarerRequest
    ): NetworkResponse<ResponseSuccess<Any>, ResponseError>

    @DELETE("carer/carers/{param}")
    suspend fun deleteCarer(
        @Path("param") carerId: Int
    ): NetworkResponse<ResponseSuccess<Any>, ResponseError>

    @GET("/carer/carers/{param}")
    suspend fun detailCarer(@Path("param") profileId: Int): NetworkResponse<ResponseSuccess<AllCarersModel>, ResponseError>

    @PUT("/carer/carers/{param}")
    suspend fun editCarer(
        @Path("param") profileId: Int,
        @Body editCarerRequest: EditCarerRequest
    ): NetworkResponse<ResponseSuccess<Any>, ResponseError>

    @PUT("carer/carers/{param}/pin")
    suspend fun editCarerPIN(
        @Path("param") profileId: Int,
        @Body editProfilePINRequest: EditProfilePINRequest
    ): NetworkResponse<ResponseSuccess<Any>, ResponseError>

    @PUT("carer/account/password")
    suspend fun editMainCarerPassword(
        @Body editPasswordRequest: EditPasswordRequest
    ): NetworkResponse<ResponseSuccess<Any>, ResponseError>

    @POST("subscription")
    suspend fun postSubscription(
        @Body subscriptionRequest: SubscriptionRequest
    ): NetworkResponse<ResponseSuccess<Any>, ResponseError>
    /* End of Carers Routes */

    /* Log Routes */
    @GET("/carer/logs")
    suspend fun carerLog(@Query("type") type: String?): NetworkResponse<ResponseSuccess<ArrayList<CarerLogModel>>, ResponseError>
    /* End of Log Routes */

    /* Account Routes */
    @GET("/subscription")
    suspend fun subscription(): NetworkResponse<ResponseSuccess<ArrayList<SubscriptionModel>>, ResponseError>

    @POST("carer/logs/clear")
    suspend fun deleteLog(
        @Body deleteLogRequest: DeleteLogRequest
    ): NetworkResponse<ResponseSuccess<Any>, ResponseError>
    /* End of Account Routes */

    /* Locale Routes */
    @PUT("carer/account/locale")
    suspend fun updateCarerLocale(
        @Body updateLocaleRequest: UpdateLocaleRequest
    ): NetworkResponse<ResponseSuccess<LocaleModel>, ResponseError>

    @PUT("kid/settings/locale")
    suspend fun updateKidLocale(
        @Body updateLocaleRequest: UpdateLocaleRequest
    ): NetworkResponse<ResponseSuccess<LocaleModel>, ResponseError>
    /* End of Locale Routes */

}

open class UserRepository(private val userService: UserService) {

    /* Register Routes Functions */
    suspend fun register(register: RegisterEmailRequest): NetworkResponse<ResponseSuccess<RegisterModel>, ResponseError> {
        return userService.register(register)
    }

    suspend fun resendCode(resendRequest: ResendPINRequest): NetworkResponse<ResponseSuccess<ResendCodeModel>, ResponseError> {
        return userService.resendCode(resendRequest)
    }

    suspend fun confirmAccount(confirmRequest: RegisterConfirmRequest): NetworkResponse<ConfirmAccountModel, ResponseError> {
        return userService.confirmAccount(confirmRequest)
    }

    suspend fun getAllProfileIcons(): NetworkResponse<ProfileIconModel, ResponseError> {
        return userService.allProfileIcons()
    }

    suspend fun postSetupDefaultCarer(
        carerToken: String,
        defaultCarerRequest: DefaultCarerRequest
    ): NetworkResponse<ResponseSuccess<Any>, ResponseError> {
        return userService.setupDefaultCarer(carerToken, defaultCarerRequest)
    }

    /*suspend fun putCarerPIN(
        carerToken: String,
        editCarerPINRequest: EditCarerPINRequest
    ): NetworkResponse<ResponseSuccess<EditMainCarerPINModel>, ResponseError> {
        return userService.editMainCarerPIN(carerToken, editCarerPINRequest)
    }*/

    suspend fun postCreateKid(
        carerToken: String,
        createChildRequest: CreateChildRequest
    ): NetworkResponse<ResponseSuccess<CreateKidModel>, ResponseError> {
        return userService.createKid(carerToken, createChildRequest)
    }

    suspend fun getGlobalChallenge(
        carerToken: String,
        age: Int?
    ): NetworkResponse<ResponseSuccess<ArrayList<KidGlobalChallengeModel>>, ResponseError> {
        return userService.globalChallenge(
            carerToken = carerToken,
            limit = 20,
            page = 1,
            keyword = null,
            order = Constant.SortBy.ASC,
            age = age
        )
    }

    suspend fun postCreateAssignment(
        carerToken: String,
        createAssignmentRequest: CreateAssignmentRequest
    ): NetworkResponse<ResponseSuccess<Any>, ResponseError> {
        return userService.createAssignment(carerToken, createAssignmentRequest)
    }

    suspend fun postRegisterLogin(loginRequest: LoginRequest): NetworkResponse<ResponseSuccess<LoginModel>, ResponseError> {
        return userService.registerLogin(loginRequest)
    }
    /* End of Register Routes Functions */

    /* Login Routes Functions */
    suspend fun postCarerLogin(carerLoginRequest: CarerLoginRequest): NetworkResponse<ResponseSuccess<Any>, ResponseError> {
        return userService.carerLogin(carerLoginRequest)
    }

    suspend fun postKidLogin(kidLoginRequest: KidLoginRequest): NetworkResponse<ResponseSuccess<LoginModel>, ResponseError> {
        return userService.kidLogin(kidLoginRequest)
    }

    suspend fun getAllProfileSelection(loginToken: String): NetworkResponse<ResponseSuccess<AllProfileModel>, ResponseError> {
        return userService.allProfileSelection(loginToken)
    }
    /* End of Login Routes Functions */

    /* Tasks Routes Functions */
    suspend fun getCurrentCycleAssignment(profileId: Int): NetworkResponse<ResponseSuccess<AllKidsModel.Assignments>, ResponseError> {
        return userService.currentCycleAssignment(profileId = profileId)
    }

    suspend fun getTodayAssignment(profileId: Int): NetworkResponse<ResponseSuccess<AllKidsModel.Assignments>, ResponseError> {
        return userService.todayAssignment(profileId = profileId)
    }

    suspend fun getYesterdayAssignment(profileId: Int): NetworkResponse<ResponseSuccess<AllKidsModel.Assignments>, ResponseError> {
        return userService.yesterdayAssignment(profileId = profileId)
    }

    suspend fun postConfirmAssignmentCompletion(assignmentRequest: AssignmentActionRequest): NetworkResponse<ResponseSuccess<Any>, ResponseError> {
        return userService.confirmAssignmentCompletion(assignmentRequest = assignmentRequest)
    }

    suspend fun postConfirmAssignmentWithoutCompletion(assignmentRequest: AssignmentActionRequest): NetworkResponse<ResponseSuccess<Any>, ResponseError> {
        return userService.confirmAssignmentWithoutCompletion(assignmentRequest = assignmentRequest)
    }

    suspend fun postDeclineAssignmentCompletion(assignmentRequest: AssignmentActionRequest): NetworkResponse<ResponseSuccess<Any>, ResponseError> {
        return userService.declineAssignmentCompletion(assignmentRequest = assignmentRequest)
    }

    suspend fun postDeclineAssignmentWithoutCompletion(assignmentRequest: AssignmentActionRequest): NetworkResponse<ResponseSuccess<Any>, ResponseError> {
        return userService.declineAssignmentWithoutCompletion(assignmentRequest = assignmentRequest)
    }

    suspend fun deleteKidAssignment(deleteKidTaskRequest: DeleteChildTaskRequest): NetworkResponse<ResponseSuccess<Any>, ResponseError> {
        return userService.deleteKidAssignment(deleteKidTaskRequest = deleteKidTaskRequest)
    }

    suspend fun getGroupedChallenges(age: Int): NetworkResponse<ResponseSuccess<GroupedChallengesModel>, ResponseError> {
        return userService.groupedChallenges(age = age)
    }

    suspend fun getDetailChallenges(challengeId: Int): NetworkResponse<ResponseSuccess<Any>, ResponseError> {
        return userService.challengeDetail(challengeId = challengeId)
    }

    suspend fun getChallengeCategory(): NetworkResponse<ResponseSuccess<ArrayList<ChallengeCategoryModel>>, ResponseError> {
        return userService.challengeCategory()
    }

    suspend fun postNewCustomChallenge(request: CustomTaskRequest): NetworkResponse<ResponseSuccess<CustomTaskModel>, ResponseError> {
        return userService.postNewCustomChallenge(request)
    }

    suspend fun getListCustomChallenge(): NetworkResponse<ResponseSuccess<List<CustomChallengeModel>>, ResponseError> {
        return userService.getCustomChallenge()
    }

    suspend fun putCustomChallenge(request: EditCustomChallenge): NetworkResponse<ResponseSuccess<CustomTaskModel>, ResponseError> {
        return userService.putCustomChallenge(request)
    }

    suspend fun deleteCustomChallenge(idChallenge: Int): NetworkResponse<ResponseSuccess<ResponseSuccess.SuccessDelete>, ResponseError> {
        return userService.deleteCustomChallenge(idChallenge)
    }
    /* End of Tasks Routes Functions */

    /* Rewards Routes Functions */
    suspend fun getListReward(
        profileId: Int,
        starCost: Int?
    ): NetworkResponse<ResponseSuccess<List<RewardModel>>, ResponseError> {
        return userService.listReward(kid_id = profileId, star_cost = starCost)
    }

    suspend fun getListGlobalReward(): NetworkResponse<ResponseSuccess<RewardListModel>, ResponseError> {
        return userService.listGlobalReward()
    }

    suspend fun postCreateReward(createRewardRequest: CreateRewardRequest): NetworkResponse<ResponseSuccess<Any>, ResponseError> {
        return userService.createReward(createRewardRequest)
    }

    suspend fun assignCustomRewardForKids(request: CustomRewardAssignKidRequest): NetworkResponse<ResponseSuccess<Any>, ResponseError> {
        return userService.assignCustomRewardForKids(request)
    }

    suspend fun assignGlobalRewardForKids(request: GlobalRewardAssignKidRequest): NetworkResponse<ResponseSuccess<Any>, ResponseError> {
        return userService.assignGlobalRewardForKids(request)
    }

    suspend fun unassignCustomRewardForKids(request: CustomRewardAssignKidRequest): NetworkResponse<ResponseSuccess<Any>, ResponseError> {
        return userService.removeCustomRewardFromKids(request)
    }

    suspend fun unassignGlobalRewardForKids(request: GlobalRewardAssignKidRequest): NetworkResponse<ResponseSuccess<Any>, ResponseError> {
        return userService.removeGlobalRewardFromKids(request)
    }

    /* End of Rewards Routes Functions */

    /* Kids Routes Functions */
    suspend fun getListAllKids(): NetworkResponse<ResponseSuccess<ArrayList<AllKidsModel>>, ResponseError> {
        return userService.listAllKids()
    }

    suspend fun getDetailKid(profileId: Int): NetworkResponse<ResponseSuccess<AllKidsModel>, ResponseError> {
        return userService.detailKid(profileId = profileId)
    }

    suspend fun deleteKid(
        kidId: Int,
    ): NetworkResponse<ResponseSuccess<Any>, ResponseError> {
        return userService.deleteKid(kidId)
    }

    suspend fun editKid(
        profileId: Int,
        editKidRequest: EditKidRequest
    ): NetworkResponse<ResponseSuccess<Any>, ResponseError> {
        return userService.editKid(profileId, editKidRequest)
    }

    suspend fun editKidPIN(
        profileId: Int,
        editProfilePINRequest: EditProfilePINRequest
    ): NetworkResponse<ResponseSuccess<Any>, ResponseError> {
        return userService.editKidPIN(profileId, editProfilePINRequest)
    }

    suspend fun getKidInfo(): NetworkResponse<ResponseSuccess<KidInfoModel>, ResponseError> {
        return userService.kidInfo()
    }

    suspend fun kidCompleteTask(kidCompleteTaskRequest: KidCompleteTaskRequest): NetworkResponse<ResponseSuccess<Any>, ResponseError> {
        return userService.completeKidTask(kidCompleteTaskRequest)
    }
    /* End of Kids Routes Functions */

    /* Carers Routes Functions */
    suspend fun getListAllCarers(): NetworkResponse<ResponseSuccess<ArrayList<AllCarersModel>>, ResponseError> {
        return userService.listAllCarers()
    }

    suspend fun postCreateCarer(
        createCarerRequest: CreateCarerRequest
    ): NetworkResponse<ResponseSuccess<Any>, ResponseError> {
        return userService.createCarer(createCarerRequest)
    }

    suspend fun deleteCarer(carerId: Int): NetworkResponse<ResponseSuccess<Any>, ResponseError> {
        return userService.deleteCarer(carerId)
    }

    suspend fun getDetailCarer(profileId: Int): NetworkResponse<ResponseSuccess<AllCarersModel>, ResponseError> {
        return userService.detailCarer(profileId)
    }

    suspend fun editCarer(
        profileId: Int,
        editCarerRequest: EditCarerRequest
    ): NetworkResponse<ResponseSuccess<Any>, ResponseError> {
        return userService.editCarer(profileId, editCarerRequest)
    }

    suspend fun editCarerPIN(
        profileId: Int,
        editProfilePINRequest: EditProfilePINRequest
    ): NetworkResponse<ResponseSuccess<Any>, ResponseError> {
        return userService.editCarerPIN(profileId, editProfilePINRequest)
    }

    suspend fun editMainCarerPassword(
        editPasswordRequest: EditPasswordRequest
    ): NetworkResponse<ResponseSuccess<Any>, ResponseError> {
        return userService.editMainCarerPassword(editPasswordRequest)
    }

    suspend fun postSubscription(
        subscriptionRequest: SubscriptionRequest
    ): NetworkResponse<ResponseSuccess<Any>, ResponseError> {
        return userService.postSubscription(subscriptionRequest)
    }
    /* End of Carers Routes Functions */

    /* Account Routes Functions */
    suspend fun getSubscription(): NetworkResponse<ResponseSuccess<ArrayList<SubscriptionModel>>, ResponseError> {
        return userService.subscription()
    }

    suspend fun deleteLog(deleteLogRequest: DeleteLogRequest): NetworkResponse<ResponseSuccess<Any>, ResponseError> {
        return userService.deleteLog(deleteLogRequest)
    }

    /* End of Account Routes Functions */
    suspend fun getCarerLog(type: String?): NetworkResponse<ResponseSuccess<ArrayList<CarerLogModel>>, ResponseError> {
        return userService.carerLog(type = type)
    }
    /* Log Routes Functions */

    /* End of Log Routes Functions */

    /* Locale Routes Functions */
    suspend fun putCarerLocale(
        localeRequest: UpdateLocaleRequest
    ): NetworkResponse<ResponseSuccess<LocaleModel>, ResponseError> {
        return userService.updateCarerLocale(localeRequest)
    }

    suspend fun putKidLocale(
        localeRequest: UpdateLocaleRequest
    ): NetworkResponse<ResponseSuccess<LocaleModel>, ResponseError> {
        return userService.updateKidLocale(localeRequest)
    }

    /* End of Locale Routes Functions */

}