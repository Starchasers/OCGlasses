# Widget Modifier Conditions

### Light
* `IS_LIGHTLEVEL_MIN_[0-15]`
* `IS_LIGHTLEVEL_MAX_[0-15]`

requires [upgrade](Glasses#light)

### Weather
* `IS_WEATHER_RAIN`
* `IS_WEATHER_CLEAR`

requires [upgrade](Glasses#weather)

### Swimming
* `IS_SWIMMING`
* `IS_NOT_SWIMMING`

requires [upgrade](Glasses#swimming)

### Sneaking
* `IS_SNEAKING`
* `IS_NOT_SNEAKING`

requires [upgrade](Glasses#sneaking)

### OverlayActive
* `OVERLAY_ACTIVE`
* `OVERLAY_INACTIVE`

### EntityTracker
these conditions are only useful for the entity tracker
* `IS_FOCUSED_ENTITY`
* `IS_LIVING`
* `IS_PLAYER`
* `IS_NEUTRAL`
* `IS_HOSTILE`
* `IS_ITEM`


## setCondition
### Examples for Conditions:

`widget.setCondition(1, "IS_SNEAKING", true)` requires the user to sneak to activate 1st modifier

`widget.setCondition(1, "IS_SNEAKING", false)` disables the sneaky condition


conditions can be combined like in this example, which requires a lightlevel between 10-14 to activate the modifier

`widget.setCondition(2, "IS_LIGHTLEVEL_MIN_10", true)`

`widget.setCondition(2, "IS_LIGHTLEVEL_MAX_14", true)`
