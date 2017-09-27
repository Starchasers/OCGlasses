# Widget Modifier Conditions

## Light
`IS_LIGHTLEVEL_MIN`, `IS_LIGHTLEVEL_MAX` -- requires minecraft Daylight Sensor

## Weather
`IS_WEATHER_RAIN`, `IS_WEATHER_CLEAR`  -- requires opencomputers tank Upgrade

## Swimming
`IS_SWIMMING`, `IS_NOT_SWIMMING`  -- requires opencomputers geolyzer

## Sneaking
`IS_SNEAKING`, `IS_NOT_SNEAKING`   -- requires opencomputers motion sensor

## OverlayActive
`OVERLAY_ACTIVE`, `OVERLAY_INACTIVE`



(add required upgrades on a Anvil like when enchanting armor, each upgrade costs 20 levels of XP)


## setCondition
### Examples for Conditions:

`(widget)->setCondition(1, "IS_SNEAKING", true)` requires the user to sneak to activate 1st modifier

`(widget)->setCondition(1, "IS_SNEAKING", false)` disables the sneaky condition


conditions can be combined like in this example, which requires a lightlevel between 10-14 to activate the modifier

`(widget)->setCondition(2, "IS_LIGHTLEVEL_MIN_10", true)`

`(widget)->setCondition(2, "IS_LIGHTLEVEL_MAX_14", true)`
