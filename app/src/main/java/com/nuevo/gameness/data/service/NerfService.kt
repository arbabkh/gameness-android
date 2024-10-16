package com.nuevo.gameness.data.service

import com.nuevo.gameness.data.model.announcements.AnnouncementListResponse
import com.nuevo.gameness.data.model.events.EventListResponse
import com.nuevo.gameness.data.model.events.EventResponse
import com.nuevo.gameness.data.model.games.GameListResponse
import com.nuevo.gameness.data.model.profile.AwardListResponse
import com.nuevo.gameness.data.model.readymessages.*
import com.nuevo.gameness.data.model.SuccessResponse
import com.nuevo.gameness.data.model.email_otp.EmailVerificationResponse
import com.nuevo.gameness.data.model.email_otp.SendEmailRequest
import com.nuevo.gameness.data.model.email_otp.SendEmailResponse
import com.nuevo.gameness.data.model.email_otp.VerifyEmailOtpRequest
import com.nuevo.gameness.data.model.phone_otp.SendSMSRequest
import com.nuevo.gameness.data.model.phone_otp.SendSMSResponse
import com.nuevo.gameness.data.model.phone_otp.VerifySMSOtpRequest
import com.nuevo.gameness.data.model.publicconfigurations.PublicConfigurationsResponse
import com.nuevo.gameness.data.model.refreshtoken.RefreshTokenRequest
import com.nuevo.gameness.data.model.register.GetCountriesResponse
import com.nuevo.gameness.data.model.settings.*
import com.nuevo.gameness.data.model.settings.request.*
import com.nuevo.gameness.data.model.sliders.SliderListResponse
import com.nuevo.gameness.data.model.teamusers.SendTeamInviteRequest
import com.nuevo.gameness.data.model.teamusers.TeamInviteDecisionRequest
import com.nuevo.gameness.data.model.teamusers.UserToTeamInvitedListResponse
import com.nuevo.gameness.data.model.tournamentrefereemessages.SendRefereeMessageResponse
import com.nuevo.gameness.data.model.tournamentrefereemessages.RefereeMessageAsReadRequest
import com.nuevo.gameness.data.model.tournamentrefereemessages.TournamentRefereeUserChatListResponse
import com.nuevo.gameness.data.model.tournamentrooms.*
import com.nuevo.gameness.data.model.tournaments.*
import com.nuevo.gameness.data.model.users.*
import com.nuevo.gameness.utils.Constants
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface NerfService {

    @GET(Constants.SLIDER_LIST)
    suspend fun getSliderList(
        @Query("Sorting") sorting: String?,
        @Query("SkipCount") skipCount: Int?,
        @Query("MaxResultCount") maxResultCount: Int?
    ): Response<SliderListResponse>

    @GET(Constants.GAME_LIST)
    suspend fun getGameList(
        @Query("Sorting") sorting: String?,
        @Query("SkipCount") skipCount: Int?,
        @Query("takeCount") takeCount: Int?
    ): Response<GameListResponse>

    @GET(Constants.MY_GAME_LIST)
    suspend fun getMyGameList(
        @Query("Sorting") sorting: String?,
        @Query("SkipCount") skipCount: Int?,
        @Query("takeCount") takeCount: Int?
    ): Response<GameListResponse>

    @GET(Constants.MY_TEAM_GAME_LIST)
    suspend fun getMyTeamGameList(
        @Query("Sorting") sorting: String?,
        @Query("SkipCount") skipCount: Int?,
        @Query("takeCount") takeCount: Int?
    ):Response<GameListResponse>

    @GET(Constants.MY_TEAM)
    suspend fun myTeam(
    ):Response<MyTeamResponse>

    @POST(Constants.CHANGE_TEAM_INFORMATION)
    suspend fun changeMyTeamInformation(
        @Body request:RequestBody
    ):Response<ResponseBody>

    @POST(Constants.SET_USER_GAMES)
    suspend fun setUserGames(
        @Body request: SetUserGamesRequest
    ):Response<SuccessResponse>

    @GET(Constants.ANNOUNCEMENTS)
    suspend fun getAnnouncements(
        @Query("Sorting") sorting: String?,
        @Query("GameNameFilter") gameNameFilter: String?,
        @Query("BetweenDateFilter") betweenDateFilter: String?,
        @Query("SkipCount") skipCount: Int?,
        @Query("MaxResultCount") maxResultCount: Int?,
        @Query("IsUserGameAnnouncementList") isUserGameAnnouncementList: Boolean?
    ): Response<AnnouncementListResponse>

    @GET(Constants.TOURNAMENTS)
    suspend fun getTournaments(
        @Query("Sorting") sorting: String?,
        @Query("GameNameFilter") gameNameFilter: String?,
        @Query("BetweenDateFilter") betweenDateFilter: String?,
        @Query("GameId") gameId: String?,
        @Query("TeamId") teamId: String?,
        @Query("SkipCount") skipCount: Int?,
        @Query("MaxResultCount") maxResultCount: Int?,
        @Query("TournamentTypeFilter") tournamentTypeFilter: Int?,
        @Query("TournamentStatus") tournamentStatus: Int?,
        @Query("IsUserAppliedForTournaments") isUserAppliedForTournaments: Boolean?,
        @Query("IsApprovedTournaments") isApprovedTournaments: Boolean?,
        @Query("IsTeam") isTeam: Boolean?
    ): Response<TournamentListResponse>

    @GET(Constants.TOURNAMENT_FIXTURE)
    suspend fun getTournamentFixture(
        @Query("tournamentId") tournamentId: String?
    ): Response<TournamentFixtureResponse>

    @GET(Constants.USER)
    suspend fun getUser(): Response<UserResponse>

    @POST(Constants.REGISTER)
    suspend fun register(
        @Body request: RegisterRequest?
    ): Response<RegisterResponse>

    @POST(Constants.SET_USER_GAMES_FOR_REGISTER)
    suspend fun setUserGamesForRegister(
        @Body request: UserGamesForRegisterRequest?
    ): Response<SuccessResponse>

    @POST(Constants.LOGIN)
    suspend fun login(
        @Body request: LoginRequest?
    ): Response<LoginResponse>

    @POST(Constants.CHANGE_USER_INFORMATION)
    suspend fun changeUserInformation(
        @Body request: ChangeUserInformationRequest?
    ):Response<ChangeUserInformationResponse>

    @POST(Constants.CHANGE_PASSWORD)
    suspend fun changePassword(
        @Body request: ChangePasswordRequest?
    ):Response<ChangeUserInformationResponse>

    @POST(Constants.CHANGE_EMAIL)
    suspend fun changeEmail(
        @Body request: ChangeEmailRequest
    ):Response<ChangeUserInformationResponse>

    @POST(Constants.CHANGE_USERNAME)
    suspend fun changeUsername(
        @Body request: ChangeUsernameRequest
    ):Response<ChangeUserInformationResponse>

    @POST(Constants.CREATE_TEAM)
    suspend fun createTeam(
        @Body body: RequestBody
    ): Response<SuccessResponse>

    @POST(Constants.CHANGE_PROFILE_PICTURE)
    suspend fun changeProfilePicture(@Body body: RequestBody): Response<UserResponse>
    @GET(Constants.USER_AWARD_LIST)
    suspend fun userAwardList(
        @Query("MaxResultCount") maxResultCount: Int?,
        @Query("Sorting") sorting: String?,
        @Query("SkipCount") skipCount: Int?
    ): Response<AwardListResponse>

    @GET(Constants.TEAM_AWARD_LIST)
    suspend fun teamAwardList(
        @Query("MaxResultCount") maxResultCount: Int?,
        @Query("Sorting") sorting: String?,
        @Query("SkipCount") skipCount: Int?
    ) :Response<AwardListResponse>

    @GET(Constants.USER_TOURNAMENTS_LIST)
    suspend fun userTournamentsList(
        @Query("MaxResultCount") maxResultCount: Int?,
        @Query("Sorting") sorting: String?,
        @Query("SkipCount") skipCount: Int?,
        @Query("TournamentStatus") tournamentStatus: Int?
    ): Response<TournamentListResponse>

    @GET(Constants.TEAM_TOURNAMENTS_LIST)
    suspend fun teamTournamentsList(
        @Query("MaxResultCount") maxResultCount: Int?,
        @Query("Sorting") sorting: String?,
        @Query("SkipCount") skipCount: Int?,
        @Query("TournamentStatus") tournamentStatus: Int?
    ): Response<TournamentListResponse>

    @GET(Constants.TEAM_USERS)
    suspend fun teamUsers():Response<TeamUsersResponse>

    @GET(Constants.EVENT_LIST)
    suspend fun eventList(
        @Query("GameNameFilter") gameNameFilter: String?,
        @Query("BetweenDateFilter") betweenDateFilter: String?,
        @Query("EventDateFilter") EventDateFilter: String?,
        @Query("MaxResultCount") maxResultCount: Int?,
        @Query("Sorting") sorting: String?,
        @Query("SkipCount") skipCount: Int?
    ):Response<EventListResponse>

    @GET(Constants.EVENT)
    suspend fun getEvent(
        @Query("id") eventID: String?
    ):Response<EventResponse>

    @GET(Constants.NEXT_MATCH)
    suspend fun getNextMatch(
        @Query("tournamentId") tournamentId: String?
    ): Response<NextMatchResponse>

    @GET(Constants.LAST_MATCH)
    suspend fun getLastMatch(
        @Query("tournamentId") tournamentId: String?
    ): Response<LastMatchResponse>

    @GET(Constants.NEXT_MATCH_TEAM_USERS)
    suspend fun getNextMatchTeamUsers(
        @Query("tournamentId") tournamentId: String?
    ): Response<NextMatchTeamUsersResponse>

    @GET(Constants.TEAM_USER_LIST)
    suspend fun teamUserList(
    ):Response<TeamUserListResponse>

    @POST(Constants.CHANGE_TEAM_GAME_USERS)
    suspend fun changeTeamGameUsers(
        @Body request:List<ChangeTeamGameUserRequestElement>
    ):Response<SuccessResponse>

    @POST(Constants.JOIN_TOURNAMENT)
    suspend fun joinTournament(
        @Body request: TournamentIdRequest?
    ): Response<SuccessResponse>

    @GET(Constants.TOURNAMENT)
    suspend fun getTournament(
        @Query("Id") id: String?
    ): Response<TournamentResponse>

    @GET(Constants.USER_TOURNAMENT_COUNT_DOWN)
    suspend fun getUserTournamentCountDown(): Response<UserTournamentCountDownResponse>

    @POST(Constants.SEND_REFEREE_MESSAGE)
    suspend fun sendRefereeMessage(
        @Body request: RequestBody?
    ): Response<SendRefereeMessageResponse>

    @GET(Constants.TOURNAMENT_REFEREE_USER_CHAT_LIST)
    suspend fun getTournamentRefereeUserChatList(
        @Query("TournamentId") tournamentId: String?,
        @Query("Sorting") sorting: String?,
        @Query("SkipCount") skipCount: Int?,
        @Query("MaxResultCount") maxResultCount: Int?
    ): Response<TournamentRefereeUserChatListResponse>

    @GET(Constants.READY_MESSAGE_LIST)
    suspend fun getReadyMessageList(
        @Query("MaxResultCount") maxResultCount: Int?,
        @Query("SkipCount") skipCount: Int?,
        @Query("Sorting") sorting: String?
    ):Response<ReadyMessageResponse>

    @GET(Constants.READY_MESSAGE_HISTORY)
    suspend fun getReadyMessageHistory(
        @Query("TournamentId") tournamentId: String?,
        @Query("RecipientId") recipientId: String?
    ):Response<ReadyMessageHistoryResponse>

    @POST(Constants.SEND_READY_MESSAGE)
    suspend fun sendReadyMessage(
        @Body request: SendReadyMessageRequest
    ):Response<SendReadyMessageResponse>

    @POST(Constants.SET_DEVICE_LANGUAGE)
    suspend fun setDeviceLanguage(
        @Body request:DeviceLanguageRequest
    ):Response<SuccessResponse>

    @GET(Constants.SCOREBOARD)
    suspend fun getScoreboard(
        @Query("TournamentId") tournamentId: String?
    ): Response<ScoreboardResponse>

    @GET(Constants.TOURNAMENT_STAGE)
    suspend fun getTournamentStage(
        @Query("TournamentId") tournamentId: String?
    ): Response<TournamentStageResponse>

    @GET(Constants.READY_MESSAGE_INBOX)
    suspend fun getReadyMessageInbox(
        @Query("TournamentId") tournamentId: String?
    ): Response<ReadyMessageInboxResponse>

    @POST(Constants.SET_REFEREE_MESSAGE_AS_READ)
    suspend fun setRefereeMessageAsRead(
        @Body request: RefereeMessageAsReadRequest?
    ): Response<SuccessResponse>

    @POST(Constants.SET_READY_MESSAGE_AS_READ)
    suspend fun setReadyMessageAsRead(
        @Body request: ReadyMessageAsReadRequest?
    ): Response<SuccessResponse>

    @GET(Constants.CHECK_NEW_REFEREE_MESSAGE)
    suspend fun checkNewRefereeMessage(
        @Query("TournamentId") tournamentId: String?
    ): Response<SuccessResponse>

    @GET(Constants.CHECK_NEW_READY_MESSAGE)
    suspend fun checkNewReadyMessage(
        @Query("TournamentId") tournamentId: String?
    ): Response<SuccessResponse>

    @GET(Constants.USER_TO_TEAM_INVITED_LIST)
    suspend fun getUserToTeamInvitedList(): Response<UserToTeamInvitedListResponse>

    @GET(Constants.USER_LIST_BY_USERNAME)
    suspend fun getUserListByUsername(
        @Query("UserName") userName: String?,
        @Query("SkipCount") skipCount: Int?,
        @Query("MaxResultCount") maxResultCount: Int?,
        @Query("TeamUserExcluded") teamUserExcluded: Boolean?
    ): Response<UserListByUserNameResponse>

    @POST(Constants.SEND_TEAM_INVITE)
    suspend fun sendTeamInvite(
        @Body request: SendTeamInviteRequest?
    ): Response<SuccessResponse>

    @POST(Constants.SEND_PASSWORD_CODE)
    suspend fun sendPasswordCode(
        @Body request: EMailRequest?
    ): Response<SuccessResponse>

    @POST(Constants.VERIFY_PASSWORD_CODE)
    suspend fun verifyPasswordCode(
        @Body request: VerifyPasswordCodeRequest?
    ): Response<VerifyPasswordCodeResponse>

    @POST(Constants.RESET_PASSWORD)
    suspend fun resetPassword(
        @Body request: ResetPasswordRequest?
    ): Response<ResetPasswordResponse>

    @POST(Constants.CANCEL_TOURNAMENT)
    suspend fun cancelTournament(
        @Body request: TournamentIdRequest?
    ): Response<SuccessResponse>

    @GET(Constants.IS_JOINED_TOURNAMENT)
    suspend fun isJoinedTournament(
        @Query("tournamentId") tournamentId: String?
    ): Response<IsJoinedTournamentResponse>

    @GET(Constants.GET_TEAM_INVITE_LIST)
    suspend fun getTeamInviteList(): Response<TeamInvitationResponse>

    @POST(Constants.LEAVE_TEAM)
    suspend fun leaveTheTeam(
    ): Response<SuccessResponse>

    @POST(Constants.TEAM_INVITE_DECISION)
    suspend fun teamInvitationDecision(
        @Body request: TeamInviteDecisionRequest?
    ): Response<SuccessResponse>

    @GET(Constants.GET_PRE_DEFINED_PROFILE_PICTURES)
    suspend fun getPreDefinedProfilePicture(): Response<IconChangeResponse>

    @POST(Constants.SET_PRE_DEFINED_PROFILE_PICTURES)
    suspend fun setProfilePicture(
        @Body request: PreDefinedProfilePictureRequest?
    ): Response<SuccessResponse>

    @POST(Constants.CANCEL_TEAM_INVITATIONS)
    suspend fun cancelTeamInvitations(
        @Body request: CancelTeamInvitationsRequest?
    ): Response<SuccessResponse>

    @POST(Constants.INVITE_USER)
    suspend fun inviteExternalUser(
        @Body request: InviteExternalUserRequest?
    ): Response<SuccessResponse>

    @GET(Constants.GET_SCREENSHOTS)
    suspend fun getScreenShots(): Response<ScreenShotsResponse>

    @POST(Constants.CREATE_MATCH_SCREENSHOTS)
    suspend fun createMatchScreenshot(
        @Body body: RequestBody
    ): Response<CreateMatchScreenShotResponse>

    @GET(Constants.GET_COUNTRY_LIST)
    suspend fun getCountryList(): Response<GetCountriesResponse>

    @POST(Constants.REFRESH_TOKEN)
    suspend fun getRefreshToken(@Body request: RefreshTokenRequest?): Response<LoginResponse>

    @POST(Constants.SEND_SMS)
    suspend fun sendOTPSMS(@Body request: SendSMSRequest?): Response<SendSMSResponse>

    @POST(Constants.VERIFY_SMS_OTP)
    suspend fun verifyOTPSMS(@Body request: VerifySMSOtpRequest?): Response<EmailVerificationResponse>

    @POST(Constants.SEND_EMAIL)
    suspend fun sendOTPEmail(@Body request: SendEmailRequest?): Response<SendEmailResponse>

    @POST(Constants.VERIFY_EMAIL)
    suspend fun verifyOTPEmail(@Body request: VerifyEmailOtpRequest?): Response<EmailVerificationResponse>


    @GET(Constants.PUBLIC_CONFIGURATION)
    suspend fun getPublicConfigurations(): Response<PublicConfigurationsResponse>



}