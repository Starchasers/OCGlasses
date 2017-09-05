Possible Modifiers:

`addRotation(float angle, float x, float y, float z)`


`addScale(float x, float y, float z)`

`addColor(float red, float green, float blue, float alpha)`

`addTranslation(float x, float y, float z)`


Additional Lua Methods for Widgets:

`removeModifier(int modifierIndex)`

`getModifiers()`


modifiers can have conditions to get active, those are:

`IS_LIGHTLEVEL_MIN`, `IS_LIGHTLEVEL_MAX`
`IS_WEATHER_RAIN`, `IS_WEATHER_CLEAR`
`IS_SWIMMING`, `IS_NOT_SWIMMING`
`IS_SNEAKING`, `IS_NOT_SNEAKING`
`OVERLAY_ACTIVE`, `OVERLAY_INACTIVE`


Examples for Conditions:

`(widget)->setCondition(1, "IS_SNEAKING", true)` requires the user to sneak to activate 1st modifier

`(widget)->setCondition(1, "IS_SNEAKING", false)` disables the sneaky condition


conditions can be combined like in this example, which requires a lightlevel between 10-14 to activate the modifier

`(widget)->setCondition(2, "IS_LIGHTLEVEL_MIN", true, 10)`

`(widget)->setCondition(2, "IS_LIGHTLEVEL_MAX", true, 14)`