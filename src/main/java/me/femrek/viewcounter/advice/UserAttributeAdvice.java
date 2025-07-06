package me.femrek.viewcounter.advice;

import me.femrek.viewcounter.controller.MainController;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(assignableTypes = MainController.class)
public class UserAttributeAdvice {
    @ModelAttribute("user")
    @Nullable
    public OAuth2User addUser(@AuthenticationPrincipal OAuth2User user) {
        return user;
    }

    @ModelAttribute("isAuthenticated")
    @NonNull
    public boolean isAuthenticated(@AuthenticationPrincipal OAuth2User user) {
        return user != null;
    }

    @ModelAttribute("userUsername")
    public String addUserUsername(@AuthenticationPrincipal OAuth2User user) {
        return user != null ? user.getAttribute("login") : null;
    }

    @ModelAttribute("userName")
    @Nullable
    public String addUserName(@AuthenticationPrincipal OAuth2User user) {
        return user != null ? user.getAttribute("name") : null;
    }

    @ModelAttribute("userProfileUrl")
    @Nullable
    public String addUserProfileUrl(@AuthenticationPrincipal OAuth2User user) {
        return user != null ? user.getAttribute("html_url") : null;
    }

    @ModelAttribute("userAvatarUrl")
    @Nullable
    public String addUserAvatarUrl(@AuthenticationPrincipal OAuth2User user) {
        return user != null ? user.getAttribute("avatar_url") : null;
    }
}
