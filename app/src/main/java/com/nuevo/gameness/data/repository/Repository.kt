package com.nuevo.gameness.data.repository

import com.nuevo.gameness.data.model.email_otp.SendEmailRequest
import com.nuevo.gameness.data.model.email_otp.VerifyEmailOtpRequest
import com.nuevo.gameness.data.model.phone_otp.SendSMSRequest
import com.nuevo.gameness.data.model.phone_otp.VerifySMSOtpRequest
import com.nuevo.gameness.data.model.settings.request.*
import com.nuevo.gameness.data.model.tournamentrefereemessages.RefereeMessageAsReadRequest
import com.nuevo.gameness.data.model.readymessages.ReadyMessageAsReadRequest
import com.nuevo.gameness.data.model.readymessages.SendReadyMessageRequest
import com.nuevo.gameness.data.model.refreshtoken.RefreshTokenRequest
import com.nuevo.gameness.data.model.teamusers.SendTeamInviteRequest
import com.nuevo.gameness.data.model.teamusers.TeamInviteDecisionRequest
import com.nuevo.gameness.data.model.tournaments.TournamentIdRequest
import com.nuevo.gameness.data.model.users.*
import com.nuevo.gameness.data.service.NerfService
import dagger.hilt.android.scopes.ActivityRetainedScoped
import okhttp3.RequestBody
import javax.inject.Inject

@ActivityRetainedScoped
class Repository @Inject constructor(private val apiService: NerfService) {

    suspend fun getSliderList(
        sorting: String?,
        skipCount: Int?,
        maxResultCount: Int?
    ) = apiService.getSliderList(sorting, skipCount, maxResultCount)

    suspend fun getGameList(
        sorting: String? = null,
        skipCount: Int? = null,
        takeCount: Int? = null
    ) = apiService.getGameList(sorting, skipCount, takeCount)

    suspend fun getMyGameList(
        sorting: String? = null,
        skipCount: Int? = null,
        takeCount: Int? = null
    ) = apiService.getMyGameList(sorting, skipCount, takeCount)

    suspend fun getMyTeamGameList(
        sorting: String? = null,
        skipCount: Int? = null,
        takeCount: Int? = null
    ) = apiService.getMyTeamGameList(sorting, skipCount, takeCount)

    suspend fun myTeam(
    ) = apiService.myTeam()

    suspend fun changeMyTeamInformation(
        request: RequestBody
    ) = apiService.changeMyTeamInformation(request)

    suspend fun setUserGames(
        request: SetUserGamesRequest
    ) = apiService.setUserGames(request)

    suspend fun getAnnouncements(
        sorting: String? = null,
        gameNameFilter: String? = null,
        betweenDateFilter: String? = null,
        skipCount: Int? = null,
        maxResultCount: Int? = null,
        isUserGameAnnouncementList: Boolean? = null
    ) = apiService.getAnnouncements(
        sorting,
        gameNameFilter,
        betweenDateFilter,
        skipCount,
        maxResultCount,
        isUserGameAnnouncementList
    )

    suspend fun getTournaments(
        sorting: String? = null,
        gameNameFilter: String? = null,
        betweenDateFilter: String? = null,
        gameId: String? = null,
        teamId: String? = null,
        skipCount: Int? = null,
        maxResultCount: Int? = null,
        tournamentTypeFilter: Int? = null,
        tournamentStatus: Int? = null,
        isUserAppliedForTournaments: Boolean? = null,
        isApprovedTournaments: Boolean? = null,
        isTeam: Boolean? = null
    ) = apiService.getTournaments(
        sorting,
        gameNameFilter,
        betweenDateFilter,
        gameId,
        teamId,
        skipCount,
        maxResultCount,
        tournamentTypeFilter,
        tournamentStatus,
        isUserAppliedForTournaments,
        isApprovedTournaments,
        isTeam
    )

    suspend fun getTournamentFixture(
        tournamentId: String?
    ) = apiService.getTournamentFixture(tournamentId)

    suspend fun getUser() = apiService.getUser()

    suspend fun register(
        request: RegisterRequest?
    ) = apiService.register(request)

    suspend fun setUserGamesForRegister(
        request: UserGamesForRegisterRequest?
    ) = apiService.setUserGamesForRegister(request)

    suspend fun login(
        request: LoginRequest?
    ) = apiService.login(request)



    suspend fun sendOTPSMS(
        request: SendSMSRequest?
    ) = apiService.sendOTPSMS(request)



    suspend fun verifyOTPSMS(
        request: VerifySMSOtpRequest?
    ) = apiService.verifyOTPSMS(request)

    suspend fun sendOTPEmail(
        request: SendEmailRequest?
    ) = apiService.sendOTPEmail(request)



    suspend fun verifyOTPEmail(
        request: VerifyEmailOtpRequest?
    ) = apiService.verifyOTPEmail(request)

    suspend fun getRefreshToken(
        request: RefreshTokenRequest?
    ) = apiService.getRefreshToken(request)



    suspend fun changeUserInformation(
        request: ChangeUserInformationRequest
    ) = apiService.changeUserInformation(request)

    suspend fun changePassword(
        request: ChangePasswordRequest
    ) = apiService.changePassword(request)

    suspend fun changeEmail(
        request: ChangeEmailRequest
    ) = apiService.changeEmail(request)

    suspend fun changeUsername(
        request: ChangeUsernameRequest
    ) = apiService.changeUsername(request)

    suspend fun userAwardList(
        maxResultCount: Int?,
        sorting: String?,
        skipCount: Int?
    ) = apiService.userAwardList(maxResultCount, sorting, skipCount)

    suspend fun teamAwardList(
        maxResultCount: Int?,
        sorting: String?,
        skipCount: Int?
    ) = apiService.teamAwardList(maxResultCount, sorting, skipCount)

    suspend fun userTournamentsList(
        maxResultCount: Int?,
        sorting: String?,
        skipCount: Int?,
        tournamentStatus: Int? = null
    ) = apiService.userTournamentsList(maxResultCount, sorting, skipCount, tournamentStatus)

    suspend fun teamTournamentsList(
        maxResultCount: Int?,
        sorting: String?,
        skipCount: Int?,
        tournamentStatus: Int? = null
    ) = apiService.teamTournamentsList(maxResultCount, sorting, skipCount, tournamentStatus)

    suspend fun createTeam(
        body: RequestBody
    ) = apiService.createTeam(body)

    suspend fun changeProfilePicture(body: RequestBody) = apiService.changeProfilePicture(body)

    suspend fun teamUsers() = apiService.teamUsers()

    suspend fun eventList(
        gameNameFilter: String? = null,
        betweenDateFilter: String? = null,
        EventDateFilter: String? = null,
        maxResultCount: Int? = null,
        sorting: String? = null,
        skipCount: Int? = null
    ) = apiService.eventList(
        gameNameFilter,
        betweenDateFilter,
        EventDateFilter,
        maxResultCount,
        sorting,
        skipCount
    )

    suspend fun getEvent(
        eventID: String?
    ) = apiService.getEvent(eventID)

    suspend fun getNextMatch(
        tournamentId: String?
    ) = apiService.getNextMatch(tournamentId)

    suspend fun getLastMatch(
        tournamentId: String?
    ) = apiService.getLastMatch(tournamentId)

    suspend fun getNextMatchTeamUsers(
        tournamentId: String?
    ) = apiService.getNextMatchTeamUsers(tournamentId)

    suspend fun joinTournament(
        request: TournamentIdRequest?
    ) = apiService.joinTournament(request)

    suspend fun getTournament(
        id: String?
    ) = apiService.getTournament(id)

    suspend fun teamUserList(
    ) = apiService.teamUserList()

    suspend fun changeTeamGameUser(
        request: List<ChangeTeamGameUserRequestElement>
    ) = apiService.changeTeamGameUsers(request)

    suspend fun getUserTournamentCountDown() = apiService.getUserTournamentCountDown()

    suspend fun sendRefereeMessage(
        request: RequestBody?
    ) = apiService.sendRefereeMessage(request)

    suspend fun getTournamentRefereeUserChatList(
        tournamentId: String?,
        sorting: String? = null,
        skipCount: Int? = null,
        maxResultCount: Int? = null
    ) = apiService.getTournamentRefereeUserChatList(
        tournamentId,
        sorting,
        skipCount,
        maxResultCount
    )

    suspend fun getReadyMessage(
        maxResultCount: Int?,
        skipCount: Int?,
        sorting: String?
    ) = apiService.getReadyMessageList(maxResultCount, skipCount, sorting)

    suspend fun getReadyMessageHistory(
        tournamentId: String?,
        recipientId: String?
    ) = apiService.getReadyMessageHistory(tournamentId, recipientId)

    suspend fun sendReadyMessage(
        request: SendReadyMessageRequest
    ) = apiService.sendReadyMessage(request)

    suspend fun setDeviceLanguage(
        request: DeviceLanguageRequest
    ) = apiService.setDeviceLanguage(request)

    suspend fun getScoreboard(
        tournamentId: String?
    ) = apiService.getScoreboard(tournamentId)

    suspend fun getTournamentStage(
        tournamentId: String?
    ) = apiService.getTournamentStage(tournamentId)

    suspend fun getReadyMessageInbox(
        tournamentId: String?
    ) = apiService.getReadyMessageInbox(tournamentId)

    suspend fun setRefereeMessageAsRead(
        request: RefereeMessageAsReadRequest?
    ) = apiService.setRefereeMessageAsRead(request)

    suspend fun setReadyMessageAsRead(
        request: ReadyMessageAsReadRequest?
    ) = apiService.setReadyMessageAsRead(request)

    suspend fun checkNewRefereeMessage(
        tournamentId: String?
    ) = apiService.checkNewRefereeMessage(tournamentId)

    suspend fun checkNewReadyMessage(
        tournamentId: String?
    ) = apiService.checkNewReadyMessage(tournamentId)

    suspend fun getUserToTeamInvitedList() = apiService.getUserToTeamInvitedList()

    suspend fun getUserListByUsername(
        userName: String?,
        skipCount: Int?,
        maxResultCount: Int?,
        teamUserExcluded: Boolean?
    ) = apiService.getUserListByUsername(userName, skipCount, maxResultCount, teamUserExcluded)

    suspend fun sendTeamInvite(
        request: SendTeamInviteRequest?
    ) = apiService.sendTeamInvite(request)

    suspend fun sendPasswordCode(
        request: EMailRequest?
    ) = apiService.sendPasswordCode(request)

    suspend fun verifyPasswordCode(
        request: VerifyPasswordCodeRequest?
    ) = apiService.verifyPasswordCode(request)

    suspend fun resetPassword(
        request: ResetPasswordRequest?
    ) = apiService.resetPassword(request)

    suspend fun cancelTournament(
        request: TournamentIdRequest?
    ) = apiService.cancelTournament(request)

    suspend fun isJoinedTournament(
        tournamentId: String?
    ) = apiService.isJoinedTournament(tournamentId)

    suspend fun getTeamInviteList() = apiService.getTeamInviteList()

    suspend fun leaveTheTeam() = apiService.leaveTheTeam()

    suspend fun sendTeamInvitationDecision(
        request: TeamInviteDecisionRequest?
    ) = apiService.teamInvitationDecision(request)

    suspend fun getPreDefinedProfileIcons() = apiService.getPreDefinedProfilePicture()

    suspend fun setPreDefinedProfileIcon(
        request:PreDefinedProfilePictureRequest?
    ) = apiService.setProfilePicture(request)

    suspend fun cancelTeamInvitations(
        request:CancelTeamInvitationsRequest?
    ) = apiService.cancelTeamInvitations(request)

    suspend fun inviteExternalUser(
        request:InviteExternalUserRequest?
    ) = apiService.inviteExternalUser(request)
    suspend fun getScreenShots(
    ) = apiService.getScreenShots()

    suspend fun createMatchScreenshot(
        body: RequestBody
    ) = apiService.createMatchScreenshot(body)

    suspend fun getCountryList() = apiService.getCountryList()

    suspend fun getPublicConfigurations() = apiService.getPublicConfigurations()
}