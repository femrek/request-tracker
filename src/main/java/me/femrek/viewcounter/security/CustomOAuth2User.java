package me.femrek.viewcounter.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import me.femrek.viewcounter.model.GithubUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
@AllArgsConstructor
@Builder
public class CustomOAuth2User implements OAuth2User {
    private final GithubUser githubUser;
    private final Collection<? extends GrantedAuthority> authorities;
    private final Map<String, Object> attributes;

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getName() {
        return githubUser.getUsername();
    }
}
