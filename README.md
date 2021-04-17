# minestom-placeholders
A simple string placeholder system for Minestom

## How to get?

Follow the instructions given here to add placeholders as a dependency for your project:

https://jitpack.io/#Asthereon/minestom-placeholders


## How to use?

**Setup**

Call PlaceholderManager.registerDefaultPlaceholders() somewhere before you try to access any of the default placeholders

**Creating a custom Placeholder**
	
-Create a class that extends Placeholder
-Implement the getPrefix() method to return the string that you want to be the prefix for your placeholders
-Implement the replace() method to add the placeholder types that should be handled by your placeholder
-* *Note:  You can check the PlayerPlaceholder class for an example of how to implement the above methods* *
	
**Registering your custom Placeholder**

`new YourPlaceholderName().register()`
Call this somewhere before you try to access any of the placeholders it handles