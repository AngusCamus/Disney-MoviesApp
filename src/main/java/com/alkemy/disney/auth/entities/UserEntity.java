package com.alkemy.disney.auth.entities;


import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.Collection;

@Entity@Getter@Setter
@Table(name = "users")
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Email
    private String username;
    @Size(min=8)
    private String password;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;

    public UserEntity() {
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
        this.enabled = true;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", accountNonExpired=" + accountNonExpired +
                ", accountNonLocked=" + accountNonLocked +
                ", credentialsNonExpired=" + credentialsNonExpired +
                ", enabled=" + enabled +
                '}';
    }
}
