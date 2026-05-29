package com.example.rosterapp.API;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UtilJSONParser {

    public static ArrayList<TeamModel> parseArrayTeams(String json) {
        ArrayList<TeamModel> teams = new ArrayList<>();

        try {
            JSONArray array = new JSONArray(json);

            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);

                TeamModel team = new TeamModel(
                        obj.optLong("id"),
                        obj.optString("name"),
                        obj.optString("region"),
                        obj.optInt("ranking")
                );

                teams.add(team);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return teams;
    }

    public static TeamModel parseTeam(String json) {
        TeamModel team = null;

        try {
            JSONObject obj = new JSONObject(json);

            team = new TeamModel(
                    obj.optLong("id"),
                    obj.optString("name"),
                    obj.optString("region"),
                    obj.optInt("ranking")
            );

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return team;
    }

    public static ArrayList<PlayerModel> parseArrayPlayers(String json) {
        ArrayList<PlayerModel> players = new ArrayList<>();

        try {
            JSONArray array = new JSONArray(json);

            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);

                PlayerModel player = new PlayerModel(
                        obj.optLong("id"),
                        obj.optString("nickname"),
                        obj.optString("name"),
                        obj.optString("role"),
                        obj.optString("country"),
                        obj.optLong("teamId"),
                        obj.optString("teamName")
                );

                players.add(player);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return players;
    }

    public static ArrayList<FavoriteModel> parseArrayFavorites(String json) {
        ArrayList<FavoriteModel> favorites = new ArrayList<>();

        try {
            JSONArray array = new JSONArray(json);

            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);

                FavoriteModel favorite = new FavoriteModel(
                        obj.optLong("id"),
                        obj.optLong("userId"),
                        obj.optLong("teamId"),
                        obj.optString("teamName"),
                        obj.optString("region"),
                        obj.optInt("ranking")
                );

                favorites.add(favorite);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return favorites;
    }

    public static JSONObject createLogin(String nombreUsuario, String password) {
        JSONObject json = new JSONObject();

        try {
            json.put("nombreUsuario", nombreUsuario);
            json.put("password", password);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }

    public static JSONObject createRegister(String nombreUsuario, String password, String email) {
        JSONObject json = new JSONObject();

        try {
            json.put("nombreUsuario", nombreUsuario);
            json.put("password", password);
            json.put("email", email);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }

    public static JSONObject createFavorite(Long userId, Long teamId) {
        JSONObject json = new JSONObject();

        try {
            json.put("userId", userId);
            json.put("teamId", teamId);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }

    private UtilJSONParser() {
    }
}