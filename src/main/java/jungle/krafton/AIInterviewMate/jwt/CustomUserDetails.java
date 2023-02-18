package jungle.krafton.AIInterviewMate.jwt;

import jungle.krafton.AIInterviewMate.domain.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Getter
public class CustomUserDetails implements UserDetails, OAuth2User {

    private Long idx;
    private String email;
    private Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes;

    public CustomUserDetails(Long idx, String email, Collection<? extends GrantedAuthority> authorities) {
        this.idx = idx;
        this.email = email;
        this.authorities = authorities;
    }

    public static CustomUserDetails create(Member user) {
        List<GrantedAuthority> authorities = Collections.
                singletonList(new SimpleGrantedAuthority("ROLE_USER"));

        return new CustomUserDetails(
                user.getIdx(),
                user.getEmail(),
                authorities
        );
    }

    public static CustomUserDetails create(Member user, Map<String, Object> attributes) {
        CustomUserDetails userDetails = CustomUserDetails.create(user);
        userDetails.setAttributes(attributes);
        return userDetails;
    }

    @Override
    public String getName() {
        return String.valueOf(idx);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;
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
}
