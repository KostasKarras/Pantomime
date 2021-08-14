package com.example.pantomime;

import java.util.HashMap;

public class Store {

    private static HashMap<String, Integer> scores;

    private static int rounds = -1;
    private static int teamsCounter = -1;

    private static boolean sound = true;
    private static boolean vibration = true;

    public static void State(String team, boolean found) {
        if (scores == null){
            scores = new HashMap<>();
        }
        if (found) {
            if (scores.containsKey(team)) {
                int counter = scores.get(team);
                counter++;
                scores.put(team, counter);
            } else {
                scores.put(team, 1);
            }
        } else {
            if (!scores.containsKey(team))
                scores.put(team, 0);
        }
    }

    public static void Rounds(int teams, int selectedRounds) {
        teamsCounter = teams;
        rounds = teams * selectedRounds;
    }

    public static void decreaseRound() {
        rounds -= 1;
    }

    public static boolean anotherRound() {
        if (rounds == 0)
            return false;
        return true;
    }

    public static HashMap<String, Integer> getScores() {
        return scores;
    }

    public static void setScores() {
        scores = null;
    }

    public static void setSound(boolean state) {
        sound = state;
    }

    public static void setVibration(boolean state) {
        vibration = state;
    }

    public static boolean getSound() { return sound; }

    public static boolean getVibration() {
        return vibration;
    }
}