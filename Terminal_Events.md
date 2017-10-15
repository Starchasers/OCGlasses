# Events

![](https://i.imgur.com/MUEE9RC.png)

## Glasses Events
* EQUIPED_GLASSES
* UNEQUIPED_GLASSES

`EVENT`, `ID`, `USER`

fires when user puts its glasses on/off

* INTERACT_WORLD_RIGHT
* INTERACT_WORLD_LEFT

`EVENT`, `ID`, `USER`, `PLAYER_POSITION_X`, `PLAYER_POSITION_Y`, `PLAYER_POSITION_Z`, `PLAYER_LOOKAT_X`, `PLAYER_LOOKAT_Y`, `PLAYER_LOOKAT_Z`

fires when user interacts in the world

* INTERACT_WORLD__BLOCK_RIGHT
* INTERACT_WORLD_BLOCK_LEFT

`EVENT`, `ID`, `USER`, `PLAYER_POSITION_X`, `PLAYER_POSITION_Y`, `PLAYER_POSITION_Z`, `PLAYER_LOOKAT_X`, `PLAYER_LOOKAT_Y`, `PLAYER_LOOKAT_Z`, `BLOCK_POSITION_X`, `BLOCK_POSITION_Y`, `BLOCK_POSITION_Z`

fires when user interacts with a block in the world, requires [Geolyzer Upgrade](Glasses#geolyzer)



* INTERACT_OVERLAY

`EVENT`, `ID`, `USER`, `X`, `Y`, `BUTTON`

fires when user interacts with overlay

* GLASSES_SCREEN_SIZE

`EVENT`, `ID`, `USER`, `WIDTH`, `HEIGHT`, `GUI_SCALE`

fires on screen-size change and on [request](Terminal_Commands#requestresolutioneventsuser).