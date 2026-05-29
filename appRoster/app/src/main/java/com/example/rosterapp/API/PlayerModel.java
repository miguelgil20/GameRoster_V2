package com.example.rosterapp.API;

public class PlayerModel {

    private Long id;
    private String nickname;
    private String name;
    private String role;
    private String country;
    private Long teamId;
    private String teamName;

    public PlayerModel(Long id, String nickname, String name, String role,
                       String country, Long teamId, String teamName) {
        this.id = id;
        this.nickname = nickname;
        this.name = name;
        this.role = role;
        this.country = country;
        this.teamId = teamId;
        this.teamName = teamName;
    }

    public Long getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public String getCountry() {
        return country;
    }

    public Long getTeamId() {
        return teamId;
    }

    public String getTeamName() {
        return teamName;
    }
}