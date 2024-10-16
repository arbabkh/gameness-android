package com.nuevo.gameness.data.model.users

data class UserModel(
    var token: String = "",
    var username: String = "",
    var password: String = "",
    var eMail: String = "",
    var isTeamLeader: Boolean = false,
    var profilePageItem: Int = 0,
    var id: String = "",
    var tokenExpiresAt: String = "",
    var refreshToken: String = "",
    var refreshTokenExpiresAt: String = "",
) {
    companion object {
        val instance = UserModel()
    }
}