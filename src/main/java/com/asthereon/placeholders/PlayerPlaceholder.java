package com.asthereon.placeholders;

import net.minestom.server.entity.Player;

import javax.annotation.Nullable;

/**
 * An example placeholder that provides the following placeholders:
 *  player_health       the player's health as a float
 *  player_health_int   the player's health as an integer
 *  player_food         the player's food
 *  player_saturation   the player's saturation
 */
public class PlayerPlaceholder extends Placeholder {

    public String getPrefix() {
        return "player";
    }

    public String replace(Player player, String placeholderType, @Nullable String[] placeholderArguments) {
        switch (placeholderType) {
            // player_health
            // player_health_int
            case "health":
                // The placeholder arguments can be null if there are none, so a check is needed to see if they exist
                if (placeholderArguments != null && placeholderArguments.length > 0) {
                    if (placeholderArguments[0].equals("int")) {
                        return Integer.toString((int)player.getHealth());
                    }
                }
                return Float.toString(player.getHealth());
            // player_food
            case "food":
                return Float.toString(player.getFood());
            // player_saturation
            case "saturation":
                return Float.toString(player.getFoodSaturation());
        }

        // If the placeholderType doesn't match anything, return null to avoid replacing the placeholder with a blank string
        return null;
    }
}
