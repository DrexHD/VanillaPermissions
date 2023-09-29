# Vanilla Permissions

This mod adds permission checks into vanilla, to allow for full permission customization.

* You need to have a permissions mod installed. (e.g. [LuckPerms](https://luckperms.net))
  (Any permission provider mod that supports [fabric-permissions-api](https://github.com/lucko/fabric-permissions-api) is also supported.)
## Permissions
| Permission                                                                 	                | Description                                                                     	 |
|---------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------|
| `minecraft.adminbroadcast.receive`                                         	                | Receive command feedback                                                        	 |
| `minecraft.bypass.spawn-protection`                                        	                | Build inside spawn protection                                                   	 |
| `minecraft.bypass.force-gamemode`                                          	                | Bypass forced gamemode                                                          	 |
| `minecraft.bypass.move-speed.player`                                       	                | Bypass "Player moved too fast"                                                  	 |
| `minecraft.bypass.move-speed.vehicle.<entity>`                             	                | Bypass "Player moved too fast", while riding an `entity` (e.g `minecraft.boat`) 	 |
| `minecraft.bypass.chat-speed`                                              	                | Bypass chat kick, when sending messages / commands to quick                     	 |
| `minecraft.bypass.whitelist`                                               	                | Bypass server whitelist                                                         	 |
| `minecraft.bypass.hunger`                                              	                    | Bypass hunger                     	                                               |
| `minecraft.bypass.player-limit`                                            	                | Bypass server player limit                                                      	 |
| `minecraft.command.<command>`                                              	                | Command permissions, see [commands](#commands) for more information             	 |
| `minecraft.debug_stick.use.<block>`                                        	                | Use debug stick on `block` (e.g. `minecraft.oak_trapdoor`)                      	 |
| `minecraft.<query/load>.<entity/block>`                                    	                | Place blocks with nbt data and use debug commands                               	 |
| `minecraft.operator_block.<command_block/jigsaw/structure_block>.<place/view/edit/break>` 	 | Place, view, edit and break operator blocks.                                    	 |
| `minecraft.selector`                                                       	                | Use entity selectors in commands                                                	 |

## Commands
Command permissions use the node system of [brigadier](https://github.com/Mojang/brigadier). Each node has its own 
permission, e.g. to get access to the `/gamemode survival` command `minecraft.command.gamemode` permission is required 
for access to the `/gamemode` command node, and `minecraft.command.gamemode.survival` is required to be able to run 
`/gamemode survival`. If you wish to grant full access to a command you need to give access to all child nodes, e.g.
`minecraft.command.gamemode.*`.

## Quality of Life

### Server Side
If the mod is installed server-side, it will make clients think they're OP. This allows players with [appropriate 
permissions](#permissions) to place operator blocks and access the gamemode switcher menu.

### Execute Command
* Includes another condition: `/execute if permission <entity> <permission>` to allow datapacks to
  check permissions (e.g. `/execute if permission @s group.admin run say I am an admin`)
* #### Modifiers
  * The `feedback` modifier `/execute feedback [silent | console]` or `/execute feedback entity <entity>` is used to silence, or redirect command feedback (e.g. `/execute feedback silent run gamemode creative`)
  * The `oplevel` modifier `/execute oplevel <level>` or `/execute oplevel <targets>` can be used to *reduce* the op permission level of the command source context, this can be useful if you want to run a command as a user (from functions or console), but want bypass permissions to work (e.g. `/execute as DrexHD oplevel entity @s run rtp`, this will use the op level of the player `DrexHD` instead of the actual command executor for checking permissions like `rtp.bypass`)

### Client Side
If the mod is installed on the client, the gamemode switcher can also be accessed, if the player has access to the command, but isn't OP (useful for spigot-based servers)!