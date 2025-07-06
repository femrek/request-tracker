package me.femrek.viewcounter.security;

import lombok.extern.log4j.Log4j2;
import me.femrek.viewcounter.dto.GithubUserDTO;
import me.femrek.viewcounter.model.GithubUser;
import me.femrek.viewcounter.repository.GithubUserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Log4j2
public class CustomOAuthUserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final GithubUserRepository userRepository;

    public CustomOAuthUserService(GithubUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.trace("Loading user from OAuth2 provider: {}", userRequest.getClientRegistration().getRegistrationId());

        OAuth2User oauth2User = new DefaultOAuth2UserService().loadUser(userRequest);

        Map<String, Object> attributes = oauth2User.getAttributes();
        Integer githubId = (Integer) attributes.get("id");
        String username = (String) attributes.get("login");
        String fullName = (String) attributes.get("name");
        String avatarUrl = (String) attributes.get("avatar_url");
        String profileUrl = (String) attributes.get("html_url");

        GithubUser githubUser = userRepository.findById(githubId)
                .orElseGet(() -> {
                    GithubUser newUser = new GithubUser();
                    newUser.setId(githubId);
                    newUser.setUsername(username);
                    newUser.setName(fullName);
                    newUser.setAvatarUrl(avatarUrl);
                    newUser.setProfileUrl(profileUrl);
                    return userRepository.save(newUser);
                });

        // update fields on every login
        githubUser.setUsername(username);
        githubUser.setName(fullName);
        githubUser.setAvatarUrl(avatarUrl);
        githubUser.setProfileUrl(profileUrl);
        userRepository.save(githubUser);

        GithubUserDTO userDTO = new GithubUserDTO(githubUser);

        return new CustomOAuth2User(
                userDTO,
                List.of(new SimpleGrantedAuthority("ROLE_USER")),
                attributes
        );
    }
}
