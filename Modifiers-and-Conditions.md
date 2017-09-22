Possible Modifiers:

`addRotation(float angle, float x, float y, float z)`


`addScale(float x, float y, float z)`

`addColor(float red, float green, float blue, float alpha)`

`addTranslation(float x, float y, float z)`


Additional Lua Methods for Widgets:

`removeModifier(int modifierIndex)`

`getModifiers()`


modifiers can have conditions to get active, those are:

`IS_LIGHTLEVEL_MIN`, `IS_LIGHTLEVEL_MAX` -- requires minecraft Daylight Sensor

`IS_WEATHER_RAIN`, `IS_WEATHER_CLEAR`  -- requires opencomputers tank Upgrade

`IS_SWIMMING`, `IS_NOT_SWIMMING`  -- requires opencomputers geolyzer

`IS_SNEAKING`, `IS_NOT_SNEAKING`   -- requires opencomputers motion sensor

`OVERLAY_ACTIVE`, `OVERLAY_INACTIVE`

(add required upgrades on a Anvil like when enchanting armor)



Examples for Conditions:

`(widget)->setCondition(1, "IS_SNEAKING", true)` requires the user to sneak to activate 1st modifier

`(widget)->setCondition(1, "IS_SNEAKING", false)` disables the sneaky condition


conditions can be combined like in this example, which requires a lightlevel between 10-14 to activate the modifier

`(widget)->setCondition(2, "IS_LIGHTLEVEL_MIN_10", true)`

`(widget)->setCondition(2, "IS_LIGHTLEVEL_MAX_14", true)`