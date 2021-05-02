package com.asthereon.placeholders;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minestom.server.entity.Player;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashMap;

public class PlaceholderManager {

    // Instance
    private static final PlaceholderManager instance = new PlaceholderManager();

    private static final String PLACEHOLDER_START = "{{";       // Indicates the start of a placeholder string
    private static final String PLACEHOLDER_END = "}}";         // Indicates the end of a placeholder string
    private static final String PLACEHOLDER_SEPARATOR = "_";    // Indicates the separation between the placeholder nodes
    private static final HashMap<String, Placeholder> placeholders = new HashMap<>();

    private PlaceholderManager() { }

    public static PlaceholderManager getInstance() {
        return instance;
    }

    /**
     * Loads the default placeholders for use, call this before trying to use any of the default placeholders
     *  Example:
     *      new PlayerPlaceholder().register();
     */
    public static void registerDefaultPlaceholders() {
        System.out.println("[Placeholders] Registering default placeholders...");
        new PlayerPlaceholder().register();
    }

    /**
     * Registers a placeholder, automatically called by the Placeholder.register() method
     * @param placeholder the placeholder to register
     */
    protected static void register(Placeholder placeholder) {
        PlaceholderManager.getInstance()._register(placeholder);
    }

    /**
     * Prevents duplicate prefix placeholders
     * @param placeholder the placeholder to register
     */
    private void _register(Placeholder placeholder) {
        if (!placeholders.containsKey(placeholder.getPrefix())) {
            System.out.println("[Placeholders] Registered placeholder '" + placeholder.getPrefix() + "'");
            placeholders.put(placeholder.getPrefix(), placeholder);
        }
    }

    /**
     * Replaces all valid placeholders in a string
     *  Example: A placeholder could look like {{player_health_arg1_arg2}}
     *      The {{ and }} are the start and end characters for the placeholder
     *      The placeholder string itself is split up into 3 parts based on the underscore separator:
     *          player: the prefix, which determines which placeholder is used
     *          health: the type, a string that can be used within the placeholder to determine what value to replace with
     *          arg1/arg2: the arguments, additional data that can be used by the placeholder system for additional functionality
     * @param player the player who should be used for the data to fill in the placeholders
     * @param mayContainPlaceholders a string that may contain placeholders to be replaced
     * @return the string with all valid placeholders replaced with their values
     */
    public static String replacePlaceholders(Player player, String mayContainPlaceholders) {
        // Parse out the placeholders
        String[] presentPlaceholders = StringUtils.substringsBetween(mayContainPlaceholders, PLACEHOLDER_START, PLACEHOLDER_END);

        // IF there are placeholders
        if (presentPlaceholders != null && presentPlaceholders.length > 0) {
            // FOR each placeholder
            for (String placeholder : presentPlaceholders) {
                // Split the placeholder by the separator character
                String[] parsedPlaceholder = StringUtils.split(placeholder, PLACEHOLDER_SEPARATOR);

                // IF there are at least a prefix (index 0) and a type (index 1)
                if (parsedPlaceholder != null && parsedPlaceholder.length > 1) {
                    // Store the prefix
                    String prefix = parsedPlaceholder[0];

                    // Check if the prefix was registered
                    if (placeholders.containsKey(prefix)) {
                        // Store the type
                        String placeholderType = parsedPlaceholder[1];

                        // Store any placeholder arguments (indexes 2 and above)
                        String[] placeholderArguments = Arrays.copyOfRange(parsedPlaceholder, 1, parsedPlaceholder.length);

                        // Get the replacement value for the placeholder
                        String replacementValue = placeholders.get(prefix).replace(player, placeholderType, placeholderArguments);

                        // IF the replacement value comes back valid
                        if (replacementValue != null) {
                            // Create the replacement string
                            String replacementString = PLACEHOLDER_START + StringUtils.join(parsedPlaceholder, PLACEHOLDER_SEPARATOR) + PLACEHOLDER_END;

                            // Update the placeholder in the provided string
                            mayContainPlaceholders = mayContainPlaceholders.replace(replacementString, replacementValue);
                        }
                    }
                }
            }
        }

        return mayContainPlaceholders;
    }

    /**
     * Replaces all valid placeholders in a string, then parses the string into a Component using MiniMessage
     * @param player the player who should be used for the data to fill in the placeholders
     * @param mayContainPlaceholders a string that may contain placeholders to be replaced
     * @return the Component with all valid placeholders replaced with their values and parsed by MiniMessage
     */
    public static Component getComponent(Player player, String mayContainPlaceholders) {
        return MiniMessage.get().parse(PlaceholderManager.replacePlaceholders(player, mayContainPlaceholders));
    }

    /**
     * Replaces all valid placeholders in a Component using MiniMessage
     * @param player the player who should be used for the data to fill in the placeholders
     * @param mayContainPlaceholders a Component that may contain placeholders to be replaced
     * @return the Component with all valid placeholders replaced with their values and parsed by MiniMessage
     */
    public static Component getComponent(Player player, Component mayContainPlaceholders) {
        return GsonComponentSerializer.gson().deserialize(replacePlaceholders(player, GsonComponentSerializer.gson().serialize(mayContainPlaceholders)));
    }
}
