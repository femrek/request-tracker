package me.femrek.viewcounter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import me.femrek.viewcounter.model.GithubUser;

@Data
@AllArgsConstructor
@Getter
@Builder
public class GithubUserDTO {
    private String username;
    private String fullName;
    private String profileUrl;
    private String avatarUrl;

    public GithubUserDTO(GithubUser githubUser) {
        this.username = githubUser.getUsername();
        this.fullName = githubUser.getName();
        this.profileUrl = githubUser.getProfileUrl();
        this.avatarUrl = githubUser.getAvatarUrl();
    }
}
