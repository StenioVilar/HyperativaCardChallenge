package com.hyperativa.card.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hyperativa.card.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.List;

@Data
public class UserDetailsImpl implements UserDetails {
  @Serial
  private static final long serialVersionUID = 1L;

  private Long id;

  private String username;

  @JsonIgnore
  private String password;


  public UserDetailsImpl(Long id, String username, String password) {
    this.id = id;
    this.username = username;
    this.password = password;
  }

  public static UserDetailsImpl build(User user) {

    return new UserDetailsImpl(
        user.getId(),
        user.getUsername(),
        user.getPassword());
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of();
  }

  @Override
  public boolean isAccountNonExpired() {
    return UserDetails.super.isAccountNonExpired();
  }

  @Override
  public boolean isAccountNonLocked() {
    return UserDetails.super.isAccountNonLocked();
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return UserDetails.super.isCredentialsNonExpired();
  }

  @Override
  public boolean isEnabled() {
    return UserDetails.super.isEnabled();
  }

}
