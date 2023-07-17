package com.jbrst.jbrstask.api.models

import com.jbrst.jbrstask.core.models.User

fun UserDto.toUser(): User {
    return User(username, password, email)
}

data class UserDto(
    val id: Int? = null,
    val username: String,
    val password: String,
    val email: String,
    val roles: RolesDto,
    val groups: GroupsDto? = null
)

data class UsersDto(val count: Int, val user: List<UserDto>)

data class RolesDto(val count: Int? = null, val role: List<RoleDto>)

data class RoleDto(val roleId: String, val scope: String, val href: String? = null)

data class GroupsDto(val count: Int? = null, val group: List<GroupDto>)

data class GroupDto(val key: String, val name: String? = null)