package com.asthereon.placeholders;

import net.minestom.server.entity.Player;

import javax.annotation.Nullable;

public class PlayerPlaceholder extends Placeholder {

    public String getPrefix() {
        return "player";
    }

    public String replace(Player player, String placeholderType, @Nullable String[] placeholderArguments) {
        switch (placeholderType) {
            case "health":
                return Float.toString(player.getHealth());
            case "food":
                return Float.toString(player.getFood());
            case "saturation":
                return Float.toString(player.getFoodSaturation());
        }
        return null;
    }
}
