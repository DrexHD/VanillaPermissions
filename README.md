# Vanilla Permissions

This mod adds permission checks into vanilla, to allow for full permission customization.

* You need to have a permissions mod installed. (e.g. [LuckPerms](https://luckperms.net))
  (Any permission provider mod that supports [fabric-permissions-api](https://github.com/lucko/fabric-permissions-api)
  is also supported.)

## Permissions

| Permission                                                                                | Description                                                                                 |
|-------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------|
| `minecraft.adminbroadcast.receive`                                                        | Receive command feedback                                                                    |
| `minecraft.bypass.spawn-protection`                                                       | Build inside spawn protection                                                               |
| `minecraft.bypass.force-gamemode`                                                         | Bypass forced gamemode                                                                      |
| `minecraft.bypass.move-speed.player`                                                      | Bypass "Player moved too fast"                                                              |
| `minecraft.bypass.move-speed.vehicle.<entity>`                                            | Bypass "Player moved too fast", while riding an `entity` (e.g `minecraft.boat`)             |
| `minecraft.bypass.chat-speed`                                                             | Bypass chat kick, when sending messages / commands to quick                                 |
| `minecraft.bypass.whitelist`                                                              | Bypass server whitelist                                                                     |
| `minecraft.bypass.player-limit`                                                           | Bypass server player limit                                                                  |
| [`minecraft.command.<command>`](#commands)                                                | Command permissions, see [commands](#commands) for more information                         |
| `minecraft.debug_stick.use.<block>`                                                       | Use debug stick on `block` (e.g. `minecraft.oak_trapdoor`)                                  |
| `minecraft.debug_chart`                                                                   | View debug chart                                                                            |
| `minecraft.<query/load>.<entity/block>`                                                   | Place blocks with nbt data and use debug commands                                           |
| `minecraft.operator_block.<command_block/jigsaw/structure_block>.<place/view/edit/break>` | Place, view, edit and break operator blocks.                                                |
| `minecraft.selector`                                                                      | Use entity selectors in commands                                                            |
| [`minecraft.selector.entity.<command>.<selector>`](#selectors)                            | Allow selecting [non-player entities](#scope-control) using the `selector` within `command` |
| [`minecraft.selector.player.<command>.<selector>`](#selectors)                            | Allow selecting [nonself players](#scope-control) using the `selector` within `command`     |
| [`minecraft.selector.self.<command>.<selector>`](#selectors)                              | Allow selecting [self](#scope-control) using the `selector` within `command`                |

## Meta

Also sometimes referred to as "options" or "variables".

Incorrect types are considered undefined values.

| Meta                                                           | Type      | Description                                                                                                      |
|----------------------------------------------------------------|-----------|------------------------------------------------------------------------------------------------------------------|
| [`minecraft.selector.limit.<command>.<selector>`](#selectors)  | `Integer` | [Limit the maximum number](#entity-limit) of entities that can be selected using the `selector` within `command` |
| [`minecraft.selector.weight.<command>.<selector>`](#selectors) | `Integer` | Selector weight, see [selection weight](#selection-weight) for more infomation                                   |

## Commands

This mod uses [Brigadier's](https://github.com/Mojang/brigadier) node-based permission system. Each command is made up
of multiple nodes, and each node has its own permission.

For example, the `/gamemode` command:
- The root command node (`/gamemode`) requires minecraft.command.gamemode.
- Sub-nodes like `survival`, `creative`, etc., use `minecraft.command.gamemode.survival`,
  `minecraft.command.gamemode.creative`, and so on.

In vanilla Minecraft, only the **root node** has a permission check (e.g. OP level 2). Once a player has access to that
root node, **all sub-nodes are considered unlocked by default**.

If you want finer control, you can manually restrict sub-nodes by denying their specific permissions.

#### Example

```yml
- Allow:
  minecraft.command.gamemode
- Deny:
  minecraft.command.gamemode.creative
  minecraft.command.gamemode.spectator
```

This allows players to use `/gamemode` but restricts them to only the allowed sub-options
(e.g., survival and adventure).

## Selectors

Command blocks and datapacks bypass all selector permission checks.

By default, granting `minecraft.selector` allows players to use any selector in commands they have access to.

Fine-grained permission control operates as follows. Note that this mod restricts based on **selection results**, not
raw selector syntax. Using player names, UUIDs, or selectors like `@e` are equivalent if they produce identical
results.

### Value of `selector`

Defined in the *Arguments* section of each command's Minecraft Wiki page.

For example, the [`/teleport`](https://minecraft.wiki/w/Commands/teleport#Arguments 'Minecraft Wiki') command uses
`<targets>` and `<destination>` as selectors.

### Scope Control

Use these permissions to define selector scope:

- `minecraft.selector.entity.<command>.<selector>`
- `minecraft.selector.player.<command>.<selector>`
- `minecraft.selector.self.<command>.<selector>`

Commands fail if a player attempts to select unauthorized entities.

#### Example

```yml
- Allow:
  minecraft.command.teleport
  minecraft.selector
  minecraft.selector.self.teleport.targets
  minecraft.selector.entity.teleport.targets
  minecraft.selector.self.teleport.destination
  minecraft.selector.player.teleport.destination
- Deny:
  minecraft.selector.*
```

Players may teleport themselves (`self` for `targets`) or non-player entities (`entity` for `targets`) to themselves
(`self` for `destination`) or nonself players (`player` for `destination`).

### Entity Limit

Set meta `minecraft.selector.limit.<command>.<selector>` to restrict the maximum number of entities selectable via a
given selector.

No limit is applied if this meta is unset.

### Selection Weight

Controlled by meta `minecraft.selector.weight.<command>.<selector>`.

Entities without weight settings can always select any target and be selected by any selector. When both entities have
weight values, a selector can only select targets whose weight is `less than or equal` to its own.

#### Example

```yml
# Global permissions
- Allow:
  minecraft.command.gamemode
  minecraft.selector
  minecraft.selector.self.gamemode.target
  minecraft.selector.player.gamemode.target
- Deny:
  minecraft.selector.*
# Player-specific metadata
Player1: minecraft.selector.weight.gamemode.target = 7
Player2: minecraft.selector.weight.gamemode.target = -1
Player3: minecraft.selector.weight.gamemode.target = 7
Player4: (no weight set)
```

| Player  | Can modify gamemode of   | Reason                                                                     |
|---------|--------------------------|----------------------------------------------------------------------------|
| Player1 | All players              | Weight ($7$) ≥ all others' weights                                         |
| Player2 | Only Player2 and Player4 | Weight ($-1$) < Player1/Player3 ($7$)<br>No weight restriction for Player4 |
| Player3 | All players              | Weight ($7$) ≥ all others' weights                                         |
| Player4 | All players              | No weight restriction → unrestricted access                                |

### Limitation

Selectors in chat message (e.g. `/me`) bypass fine-grained permissions.

Players without all three scopes cannot do `/scoreboard` on offline players.

With all three scopes, players doing `/scoreboard` on offline players bypass selection weight.

`/ban-ip` bypasses all selector permission checks.

## Quality of Life

### Server Side

If the mod is installed server-side, it will make clients think they're OP. This allows players with [appropriate
permissions](#permissions) to place operator blocks and access the gamemode switcher menu.

### Execute Command

* Includes another condition: `/execute if permission <entity> <permission>` to allow datapacks to
  check permissions (e.g. `/execute if permission @s group.admin run say I am an admin`)
* #### Modifiers
    * The `feedback` modifier `/execute feedback [silent | console]` or `/execute feedback entity <entity>` is used to
      silence, or redirect command feedback (e.g. `/execute feedback silent run gamemode creative`)
    * The `oplevel` modifier `/execute oplevel <level>` or `/execute oplevel <targets>` can be used to *reduce* the op
      permission level of the command source context, this can be useful if you want to run a command as a user (from
      functions or console), but want bypass permissions to work (e.g. `/execute as DrexHD oplevel entity @s run rtp`,
      this will use the op level of the player `DrexHD` instead of the actual command executor for checking permissions
      like `rtp.bypass`)

### Client Side

If the mod is installed on the client, the gamemode switcher can also be accessed, if the player has access to the
command, but isn't OP (useful for spigot-based servers)!
