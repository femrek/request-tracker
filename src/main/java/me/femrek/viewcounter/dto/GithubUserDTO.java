package me.femrek.viewcounter.dto;

import lombok.Data;
import me.femrek.viewcounter.model.GithubUser;

@Data
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
