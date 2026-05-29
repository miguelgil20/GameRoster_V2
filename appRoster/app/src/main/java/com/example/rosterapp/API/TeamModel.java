package com.example.rosterapp.API;

public class TeamModel {

    private Long id;
    private String name;
    private String region;
    private Integer ranking;

    public TeamModel(Long id, String name, String region, Integer ranking) {
        this.id = id;
        this.name = name;
        this.region = region;
        this.ranking = ranking;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRegion() {
        return region;
    }

    public Integer getRanking() {
        return ranking;
    }
}