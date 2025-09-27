package com.alicloud.config.security;

import cn.hutool.core.collection.CollectionUtil;
import com.alicloud.model.UserVo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: zhaolin
 * @Date: 2024/12/8
 **/
@Data
@NoArgsConstructor
public class LoginUser implements UserDetails {


    private UserVo userVo;

    private List<String> roles;

    private List<String> permissions;

    public LoginUser(UserVo userVo, List<String> roles,List<String> permissions) {
        this.userVo = userVo;
        this.roles = roles;
        this.permissions = permissions;
    }

    List<GrantedAuthority> authorities;

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (CollectionUtil.isNotEmpty(authorities)) {
            return authorities;
        }
        authorities = permissions.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        return authorities;
    }

    @Override
    public String getPassword() {
        return userVo.getPassword();
    }

    @Override
    public String getUsername() {
        return userVo.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }
}
