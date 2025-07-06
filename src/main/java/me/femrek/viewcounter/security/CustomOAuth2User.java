package me.femrek.viewcounter.security;

import lombok.Getter;
import me.femrek.viewcounter.dto.GithubUserDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
public class CustomOAuth2User implements OAuth2User {
    private final GithubUserDTO githubUser;
    private final Collection<? extends GrantedAuthority> authorities;
    private final Map<String, Object> attributes;

    public CustomOAuth2User(GithubUserDTO githubUser,
                            Collection<? extends GrantedAuthority> authorities,
                            Map<String, Object> attributes) {
        this.githubUser = githubUser;
        this.authorities = authorities;
        this.attributes = attributes;
    }

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
