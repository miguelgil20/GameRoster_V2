package com.example.rosterapp.API;

public class FavoriteModel {

    private Long id;
    private Long userId;
    private Long teamId;
    private String teamName;
    private String region;
    private Integer ranking;

    public FavoriteModel(Long id, Long userId, Long teamId,
                         String teamName, String region, Integer ranking) {
        this.id = id;
        this.userId = userId;
        this.teamId = teamId;
        this.teamName = teamName;
        this.region = region;
        this.ranking = ranking;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getTeamId() {
        return teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getRegion() {
        return region;
    }

    public Integer getRanking() {
        return ranking;
    }
}