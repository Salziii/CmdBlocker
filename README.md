# CmdBlocker

## Dependencies
-  [BungeeCord](https://ci.md-5.net/job/BungeeCord/)
- [Protocolize](https://github.com/Exceptionflug/protocolize)

## Features
- Blocks all commands that don't exist
- Blocks all commands for which the user does not have permission
- Blocks all commands that have been excluded by config
- Both the execution of the command and Tab Complete are blocked
- A permission to bypass the commands excluded from the config
- A command to reload the config and display the plugin information
- Overwrites all /help commands provided by minecraft, and replaces the content with the commands possible for the player
- Configuration adjustable
   - ```deny message``` -> The message sent when the player executes a command that is blocked for them
   - ```bypass permission``` -> Permission to bypass commands excluded from config, ```Default -> cmdblocker.bypass```
   - ```type```
      -  ```whitelist``` -> A whitelist allows commands that match the filter, blocking everything else.
      -  ```blacklist``` -> A blacklist denies commands that match the filter, allowing everything else.
   - ```patterns``` -> Patterns are [regular expressions](https://www3.ntu.edu.sg/home/ehchua/programming/howto/Regexe.html) that represent the rules for the black- or whitelist
