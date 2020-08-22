package com.hubt.data.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Table(name = "sys_user")
public class User extends BaseModel implements UserDetails, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @JsonIgnore
    private String password;

    private String email;

    private String phoneNumber;

    private boolean accountExpired;

    private boolean accountLocked;

    private boolean credentialsExpired;

    private boolean enable;

    private long facebookId;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return !isAccountExpired();
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return !isAccountLocked();
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return !isCredentialsExpired();
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return isEnable();
    }

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_role",
                joinColumns = @JoinColumn(name = "user_id"),
                inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

//    @JsonIgnore
//    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
//    private List<Image> images;

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Post> posts;

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Notification> notifications;

}
