package dev.perogroupe.wecheapis.utils

import dev.perogroupe.wecheapis.entities.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class UserPrincipal(val user: User) : UserDetails {

    /**
     * Retrieves the authorities of the user.
     *
     * @return a mutable collection of granted authorities based on the user's roles
     */
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return user.roles.map { role ->
            SimpleGrantedAuthority(role.roleName)
        }.toMutableList()
    }

    override fun getPassword(): String = user.password

    override fun getUsername(): String = user.username

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = user.isNotLocked

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}
