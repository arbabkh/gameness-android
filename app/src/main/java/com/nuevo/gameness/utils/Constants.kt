package com.nuevo.gameness.utils

import android.util.Log

object Constants {
    const val PREFS_FILENAME = "com.nuevo.gameness"

    //    const val TEST_URL ="https://testapi.gameness.app/" //"https://lfgnerfitapi.demo.nuevoyazilim.com" // "https://api.nerfit.app" //
    const val BASE_URL ="https://api.gameness.app/" //"https://lfgnerfitapi.demo.nuevoyazilim.com" // "https://api.nerfit.app" //

   // const val BASE_URL = "https://testapi.gameness.app/" //"https://lfgnerfitapi.demo.nuevoyazilim.com" // "https://api.nerfit.app" //
    const val TEST_URL = "https://testapi.gameness.app/" //"https://lfgnerfitapi.demo.nuevoyazilim.com" // "https://api.nerfit.app" //


//    const val TEST_URL ="https://testapi.nerfit.app/" //"https://lfgnerfitapi.demo.nuevoyazilim.com" // "https://api.nerfit.app" //
//    const val BASE_URL ="https://testapi.nerfit.app/" //"https://lfgnerfitapi.demo.nuevoyazilim.com" // "https://api.nerfit.app" //

    const val SLIDER_LIST = "/api/Sliders/GetSliderList"
    const val GAME_LIST = "/api/Games/GetGameList"
    const val MY_GAME_LIST = "/api/Games/GetMyGameList"
    const val SET_USER_GAMES = "/api/Users/SetUserGames"
    const val REGISTER = "/api/Users/Register"
    const val SET_USER_GAMES_FOR_REGISTER = "/api/Users/SetUserGamesForRegister"
    const val LOGIN = "/api/Users/Login"
    const val USER = "/api/Users/GetUser"
    const val ANNOUNCEMENTS = "/api/Announcements/GetAnnouncementList"
    const val TOURNAMENTS = "/api/Tournaments/GetTournamentList"
    const val CHANGE_USER_INFORMATION = "/api/Users/ChangeUserInformation"
    const val CHANGE_PASSWORD = "/api/Users/ChangePassword"
    const val CHANGE_EMAIL = "/api/Users/ChangeEmail"
    const val CHANGE_USERNAME = "/api/Users/ChangeUserName"
    const val MY_TEAM = "/api/Teams/GetMyTeam"
    const val MY_TEAM_GAME_LIST = "/api/Games/GetMyTeamGameList"
    const val CHANGE_TEAM_INFORMATION = "/api/Teams/ChangeTeamInformations"
    const val USER_AWARD_LIST = "/api/UserAwards/GetUserAwardList"
    const val TEAM_AWARD_LIST = "/api/TeamAwards/GetTeamAwardList"
    const val USER_TOURNAMENTS_LIST = "/api/Tournaments/GetUserTournamentList"
    const val TEAM_TOURNAMENTS_LIST = "/api/Tournaments/GetTeamTournamentList"
    const val CREATE_TEAM = "/api/Teams/CreateTeam"
    const val TOURNAMENT_FIXTURE = "/api/Tournaments/GetTournamentFixture"
    const val TEAM_USERS = "/api/TeamUsers/GetTeamUsers"
    const val EVENT_LIST = "/api/Events/GetEventList"
    const val EVENT = "/api/Events/GetEvent"
    const val NEXT_MATCH = "/api/TournamentRooms/GetNextMatch"
    const val LAST_MATCH = "/api/TournamentRooms/GetLastMatch"
    const val NEXT_MATCH_TEAM_USERS = "/api/TournamentRooms/GetNextMatchTeamUsers"
    const val USER_LIST_BY_USERNAME = "/api/Users/GetUserListByUserName"
    const val TEAM_USER_LIST = "/api/TeamUsers/GetTeamUserList"
    const val CHANGE_TEAM_GAME_USERS = "api/TeamUsers/ChangeTeamGameUsers"
    const val JOIN_TOURNAMENT = "/api/Tournaments/JoinTournament"
    const val TOURNAMENT = "/api/Tournaments/GetTournament"
    const val USER_TOURNAMENT_COUNT_DOWN = "/api/Tournaments/GetUserTournamentCountDown"
    const val SEND_REFEREE_MESSAGE = "/api/TournamentRefereeMessages/SendRefereeMessage"
    const val TOURNAMENT_REFEREE_USER_CHAT_LIST =
        "/api/TournamentRefereeMessages/GetTournamentRefereeUserChatList"
    const val READY_MESSAGE_LIST = "/api/ReadyMessages/GetReadyMessageList"
    const val READY_MESSAGE_HISTORY = "/api/ReadyMessages/GetReadyMessageHistoryList"
    const val SEND_READY_MESSAGE = "/api/ReadyMessages/SendReadyMessage"
    const val SET_DEVICE_LANGUAGE = "/api/Users/SetDeviceLanguage"
    const val SCOREBOARD = "/api/TournamentRooms/GetScoreboard"
    const val TOURNAMENT_STAGE = "/api/TournamentRooms/GetTournamentStage"
    const val READY_MESSAGE_INBOX = "/api/ReadyMessages/GetReadyMessageInbox"
    const val SET_REFEREE_MESSAGE_AS_READ = "/api/TournamentRefereeMessages/SetMessagesAsRead"
    const val SET_READY_MESSAGE_AS_READ = "/api/ReadyMessages/SetMessagesAsRead"
    const val CHECK_NEW_REFEREE_MESSAGE = "/api/TournamentRefereeMessages/CheckNewRefereeMessage"
    const val CHECK_NEW_READY_MESSAGE = "/api/ReadyMessages/CheckNewReadyMessage"
    const val USER_TO_TEAM_INVITED_LIST = "/api/TeamUsers/GetUserToTeamInvitedList"
    const val SEND_TEAM_INVITE = "/api/TeamUsers/SendTeamInvite"
    const val SEND_PASSWORD_CODE = "/api/Users/SendPasswordCode"
    const val VERIFY_PASSWORD_CODE = "/api/Users/VerifyPasswordCode"
    const val RESET_PASSWORD = "/api/Users/ResetPassword"
    const val CANCEL_TOURNAMENT = "/api/Tournaments/CancelTournament"
    const val IS_JOINED_TOURNAMENT = "/api/Tournaments/IsJoinedTournament"
    const val GET_TEAM_INVITE_LIST = "/api/TeamUsers/GetTeamInviteList"
    const val LEAVE_TEAM = "/api/Teams/LeaveTeam"
    const val TEAM_INVITE_DECISION = "/api/TeamUsers/TeamInviteDecision"
    const val GET_PRE_DEFINED_PROFILE_PICTURES = "/api/Files/GetPreDefinedProfilePictures"
    const val SET_PRE_DEFINED_PROFILE_PICTURES = "/api/Users/SetPreDefinedProfilePicture"
    const val CANCEL_TEAM_INVITATIONS = "/api/TeamUsers/CancelTeamInvitations"
    const val INVITE_USER = "/api/Users/Invite"
    const val CREATE_MATCH_SCREENSHOTS = "/api/Tournaments/CreateMatchScreenshot"
    const val GET_SCREENSHOTS = "/api/Users/Screenshots"

    const val GET_COUNTRY_LIST = "api/Geographical/GetCountryList"

    const val SEND_SMS = "/api/Otp/SendSms"

    const val SEND_EMAIL = "/api/Otp/SendEmail"

    const val VERIFY_EMAIL = " /api/Otp/VerifyEmailOtp"

    const val VERIFY_SMS_OTP = "/api/Otp/VerifySmsOtp"

    const val REFRESH_TOKEN = "api/Users/RefreshToken"

    const val WEB_SOCKET_REFEREE_MESSAGE_CHAT =
        "wss://api.nerfit.app/hub/RefereeMessageChat?access_token="
    const val WEB_SOCKET_READY_MESSAGE_CHAT =
        "wss://api.nerfit.app/hub/ReadyMessageChat?access_token="

    const val TERMS_OF_USAGE_URL = "https://nerfit.app/legal/terms-of-use"

    val PHONE_NUMBER_REGEX = "^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}\$".toRegex()

    val EMAIL_ADDRESS_REGEX = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()

    val PASSWORD_REGEX = ".*[!&^.%\$#@()-:;*/]+.*"

    const val PUBLIC_CONFIGURATION = "api/Miscellaneous/PublicConfiguration"

    var AUTO_REFRESH_RATE = 5000_000


    const val CHANGE_PROFILE_PICTURE  = "/api/Users/ChangeProfilePicture"

    var spa_chars = arrayOf("*", "+",  "?", "@", "[","#", "$", "%", "&","!", "\"", "'", "(", ")", ",", "-", ".", "/", ":", ";", "<", ">", "=", "\\", "]", "^", "_" ,"{", "|", "}", "~")

    fun isValidPassword(text: String): Boolean {
        Log.e("hatatatatata", text)

        if (text.length < 6) {
            Log.e("here1", text)
            return false
        }

        if(text.contains(" ")) {
            Log.e("here2", text)
            return false
        }

        if (!text.contains("[a-z]".toRegex())) {
            Log.e("here3", text)
            return false
        }

        if (!text.contains("[A-Z]".toRegex())) {
            Log.e("here4", text)
            return false
        }

        if (!text.contains("[0-9]".toRegex())) {
            Log.e("here5", text)
            return false
        }

        for (spa in spa_chars) {
            if (text.contains(spa)) {
                return true
            }
        }

        return false
    }

}

