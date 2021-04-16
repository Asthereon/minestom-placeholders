package com.asthereon.placeholders;

import net.minestom.server.entity.Player;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashMap;

public class PlaceholderManager {

    // Instance
    private static final PlaceholderManager instance = new PlaceholderManager();

    private static final String PLACEHOLDER_START = "{{";
    private static final String PLACEHOLDER_END = "}}";
    private static final String PLACEHOLDER_SEPARATOR = "_";
    private static final HashMap<String, Placeholder> placeholders = new HashMap<>();

    private PlaceholderManager() { }

    public static PlaceholderManager getInstance() {
        return instance;
    }

    /**
     * Add your placeholders to this method to load them for use
     *  Example:
     *      new PlayerPlaceholder().register();
     */
    public static void registerPlaceholders() {
        System.out.println("[Placeholders] Registering placeholders...");
        new PlayerPlaceholder().register();
    }

    /**
     * Registers a placeholder
     * @param placeholder the placeholder to register
     */
    public static void register(Placeholder placeholder) {
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
                        String replacementValue = placeholders.get(prefix).replace(player, placeholderType, placeholderArguments);
                        if (replacementValue != null) {
                            mayContainPlaceholders = mayContainPlaceholders.replace(PLACEHOLDER_START + StringUtils.join(parsedPlaceholder, PLACEHOLDER_SEPARATOR) + PLACEHOLDER_END, replacementValue);
                        }
                    }
                }
            }
        }
        return mayContainPlaceholders;
    }
}
