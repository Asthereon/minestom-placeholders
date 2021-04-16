package com.asthereon.placeholders;

import net.minestom.server.entity.Player;

import javax.annotation.Nullable;

abstract public class Placeholder {

    Placeholder() { }

    /**
     * Registers this placeholder in the PlaceholderManager for use
     */
    public void register() {
        PlaceholderManager.register(this);
    }

    /**
     * Gets the prefix used to identify if this is the correct placeholder handler
     * @return the prefix as a string
     */
    abstract public String getPrefix();

    /**
     * Returns the value of a matching placeholder
     * @param player the player related to this placeholder
     * @param placeholderType the first part of the placeholder after the prefix
     * @param placeholderArguments the remaining parts of the placeholder after the type
     * @return the value to replace the placeholder with
     */
    abstract public String replace(Player player, String placeholderType, @Nullable String[] placeholderArguments);

}
