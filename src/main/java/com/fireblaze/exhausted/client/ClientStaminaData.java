package com.fireblaze.exhausted.client;

public class ClientStaminaData {
    private static float playerShortStamina;
    private static float playerLongStamina;
    private static float playerMaxStamina;
    private static float playerStaminaExp;
    private static float playerStaminaLvl;

    public static void set(float shortStamina, float longStamina, float maxStamina, float staminaExp, int staminaLvl) {
        ClientStaminaData.playerShortStamina = shortStamina;
        ClientStaminaData.playerLongStamina = longStamina;
        ClientStaminaData.playerMaxStamina = maxStamina;
        ClientStaminaData.playerStaminaExp = staminaExp;
        ClientStaminaData.playerStaminaLvl = staminaLvl;
    }

    public static float getPlayerShortStamina() {
        return playerShortStamina;
    }
    public static float getPlayerLongStamina() {
        return playerLongStamina;
    }
    public static float getPlayerMaxStamina() {
        return playerMaxStamina;
    }
}
