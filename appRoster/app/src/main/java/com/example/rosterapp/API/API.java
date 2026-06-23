package com.example.rosterapp.API;

import org.json.JSONObject;

public class API {

    private static final String BASE_URL = "http://192.168.0.27:8080";

    public static void login(JSONObject loginJson,
                             UtilREST.OnResponseListener listener) {

        UtilREST.runQuery(
                UtilREST.QueryType.POST,
                BASE_URL + "/auth/login",
                loginJson.toString(),
                null,
                listener
        );
    }

    public static void register(JSONObject registerJson,
                                UtilREST.OnResponseListener listener) {

        UtilREST.runQuery(
                UtilREST.QueryType.POST,
                BASE_URL + "/auth/register",
                registerJson.toString(),
                null,
                listener
        );
    }

    public static void getTeams(String token,
                                UtilREST.OnResponseListener listener) {

        UtilREST.runQuery(
                UtilREST.QueryType.GET,
                BASE_URL + "/api/teams",
                null,
                token,
                listener
        );
    }

    public static void getTeamById(Long id,
                                   String token,
                                   UtilREST.OnResponseListener listener) {

        UtilREST.runQuery(
                UtilREST.QueryType.GET,
                BASE_URL + "/api/teams/" + id,
                null,
                token,
                listener
        );
    }

    public static void getPlayers(String token,
                                  UtilREST.OnResponseListener listener) {

        UtilREST.runQuery(
                UtilREST.QueryType.GET,
                BASE_URL + "/api/players",
                null,
                token,
                listener
        );
    }

    public static void getFavoritesByUser(Long userId,
                                          String token,
                                          UtilREST.OnResponseListener listener) {

        UtilREST.runQuery(
                UtilREST.QueryType.GET,
                BASE_URL + "/api/favorites/user/" + userId,
                null,
                token,
                listener
        );
    }

    public static void createFavorite(JSONObject favoriteJson,
                                      String token,
                                      UtilREST.OnResponseListener listener) {

        UtilREST.runQuery(
                UtilREST.QueryType.POST,
                BASE_URL + "/api/favorites",
                favoriteJson.toString(),
                token,
                listener
        );
    }

    public static void deleteFavorite(Long favoriteId,
                                      String token,
                                      UtilREST.OnResponseListener listener) {

        UtilREST.runQuery(
                UtilREST.QueryType.DELETE,
                BASE_URL + "/api/favorites/" + favoriteId,
                null,
                token,
                listener
        );
    }

    private API() {
    }
}