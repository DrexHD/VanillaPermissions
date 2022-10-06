# Vanilla Permissions

This mod adds permission checks into vanilla, to allow for full permission customization.

* You need to have a permissions mod installed. (e.g. [LuckPerms](https://luckperms.net))
  (Any permission provider mod that supports [vanilla-permissions-api](https://github.com/lucko/vanilla-permissions-api) is also supported.)
## Permissions
| Permission                                                                 	| Description                                                                     	 |
|----------------------------------------------------------------------------	|-----------------------------------------------------------------------------------|
| `minecraft.adminbroadcast.receive`                                         	| Receive command feedback                                                        	 |
| `minecraft.bypass.spawn-protection`                                        	| Build inside spawn protection                                                   	 |
| `minecraft.bypass.force-gamemode`                                          	| Bypass forced gamemode                                                          	 |
| `minecraft.bypass.move-speed.player`                                       	| Bypass "Player moved too fast"                                                  	 |
| `minecraft.bypass.move-speed.vehicle.<entity>`                             	| Bypass "Player moved too fast", while riding an `entity` (e.g `minecraft.boat`) 	 |
| `minecraft.bypass.chat-speed`                                              	| Bypass chat kick, when sending messages / commands to quick                     	 |
| `minecraft.bypass.whitelist`                                               	| Bypass server whitelist                                                         	 |
| `minecraft.bypass.player-limit`                                            	| Bypass server player limit                                                      	 |
| `minecraft.command.<command>`                                              	| Command permissions, see [commands](#commands) for more information             	 |
| `minecraft.debug_stick.use.<block>`                                        	| Use debug stick on `block` (e.g. `minecraft.oak_trapdoor`)                      	 |
| `minecraft.<query/load>.<entity/block>`                                    	| Place blocks with nbt data and use debug commands                               	 |
| `minecraft.<command_block/jigsaw/structure_block>.<place/view/edit/break>` 	| Place, view, edit and break operator blocks.                                    	 |
| `minecraft.selector`                                                       	| Use entity selectors in commands                                                	 |

## Commands
Command permissions use the node system of [brigadier](https://github.com/Mojang/brigadier). Each node has its own 
permission, e.g. to get access to the `/gamemode survival` command `minecraft.command.gamemode` permission is required 
for access to the `/gamemode` command node and `minecraft.command.gamemode.survival` is required to be able to run 
`/gamemode survival`. If you wish to grant full access to a command you need to give access to all child nodes, e.g.
`minecraft.command.gamemode.*`.

## Quality of Live

### Server Side
If the mod is installed server side, it will make clients think they're OP. This allows players with [appropriate 
permissions](#permissions) to place operator blocks and access the gamemode switcher menu.

### Client Side
If the mod is installed on the client the gamemode switcher screen will be adjusted to only show game modes the player 
has permissions for. Gamemodes can also be switched if the player has access to the command, but isn't OP (useful for 
spigot based servers)!
![Gamemode Switcher Screen](/assets/gamemode-switcher-screen.png)