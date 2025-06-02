package com.cava.AuthService.model;

import com.cava.AuthService.utils.UsersUtils;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.NameBasedGenerator;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import jakarta.persistence.*;

import static com.cava.AuthService.utils.UsersUtils.validatePhoneNumber;


@Entity
@Table(name = "Users")
@NoArgsConstructor
public class Users implements UserDetails {

    @Id
    private UUID id;

    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    private String name;

    private String password;


    public Users(UUID id, String phoneNumber,String login, String password) {
        this.id = id;
        this.phoneNumber = UsersUtils.validatePhoneNumber(phoneNumber);
        this.name = login;
        this.password = password;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    public UUID getId(){return id;}

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoneNumber(){return phoneNumber;}



}
