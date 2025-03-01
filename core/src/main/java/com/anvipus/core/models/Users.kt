package com.anvipus.core.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize


@JsonClass(generateAdapter = true)
@Entity(indices = [Index("localId", unique = true)])
@Parcelize
data class Users(
    @PrimaryKey(autoGenerate = true)
    var localId: Int? = null,
    @Json(name = "login")
    val login: String?,
    @Json(name = "id")
    val id: String?,
    @Json(name = "node_id")
    val node_id: String?,
    @Json(name = "avatar_url")
    val avatar_url: String?,
    @Json(name = "gravatar_id")
    val gravatar_id: String?,
    @Json(name = "url")
    val url: String?,
    @Json(name = "html_url")
    val html_url: String?,
    @Json(name = "followers_url")
    val followers_url: String?,
    @Json(name = "following_url")
    val following_url: String?,
    @Json(name = "gists_url")
    val gists_url: String?,
    @Json(name = "starred_url")
    val starred_url: String?,
    @Json(name = "subscriptions_url")
    val subscriptions_url: String?,
    @Json(name = "organizations_url")
    val organizations_url: String?,
    @Json(name = "type")
    val type: String?,
    @Json(name = "site_admin")
    val site_admin: Boolean = false,
    @Json(name = "repos_url")
    val repos_url: String?,
    @Json(name = "events_url")
    val events_url: String?,
    @Json(name = "received_events_url")
    val received_events_url: String?

): Parcelable {
    val siteAdminString: String?
        get() = "Site Admin: ${site_admin}"

    val username: String?
        get() = "Username: ${login}"

    val userType: String?
        get() = "User Type: ${type}"

    val userId: String?
        get() = "User Id: ${id}"
}

@JsonClass(generateAdapter = true)
@Parcelize
data class UserDetail(
    @Json(name = "login")
    val login: String?,
    @Json(name = "id")
    val id: String?,
    @Json(name = "node_id")
    val node_id: String?,
    @Json(name = "avatar_url")
    val avatar_url: String?,
    @Json(name = "gravatar_id")
    val gravatar_id: String?,
    @Json(name = "url")
    val url: String?,
    @Json(name = "html_url")
    val html_url: String?,
    @Json(name = "followers_url")
    val followers_url: String?,
    @Json(name = "following_url")
    val following_url: String?,
    @Json(name = "gists_url")
    val gists_url: String?,
    @Json(name = "starred_url")
    val starred_url: String?,
    @Json(name = "subscriptions_url")
    val subscriptions_url: String?,
    @Json(name = "organizations_url")
    val organizations_url: String?,
    @Json(name = "type")
    val type: String?,
    @Json(name = "site_admin")
    val site_admin: Boolean = false,
    @Json(name = "repos_url")
    val repos_url: String?,
    @Json(name = "events_url")
    val events_url: String?,
    @Json(name = "received_events_url")
    val received_events_url: String?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "company")
    val company: String?,
    @Json(name = "blog")
    val blog: String?,
    @Json(name = "location")
    val location: String?,
    @Json(name = "email")
    val email: String?,
    @Json(name = "bio")
    val bio: String?,
    @Json(name = "twitter_username")
    val twitter_username: String?,
    @Json(name = "public_repos")
    val public_repos: Int?,
    @Json(name = "public_gists")
    val public_gists: Int?,
    @Json(name = "followers")
    val followers: Int?,
    @Json(name = "following")
    val following: Int?,
    @Json(name = "created_at")
    val created_at: String?,
    @Json(name = "updated_at")
    val updated_at: String?

): Parcelable {
    val siteAdminString: String?
        get() = "Site Admin: ${site_admin}"

    val username: String?
        get() = "Username: ${login}"

    val fullname: String?
        get() = "Full Name: ${name}"

    val emailFull: String?
        get() = "Email: ${email}"

    val userType: String?
        get() = "User Type: ${type}"

    val userId: String?
        get() = "User Id: ${id}"
}