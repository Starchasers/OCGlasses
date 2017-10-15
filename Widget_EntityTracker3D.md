## 3D World Widget
![](https://i.imgur.com/s0cEoup.png)
### this widget requires [sneaking](Glasses#sneaking) + [swimming](Glasses#swimming) upgrades

## EntityTracker3D
`widget = component.glasses.addEntityTracker3D();`

adds a EntityTracker3D to the 3D World Space


## setTrackingType(trackingtype, radius)
* (String) trackingtype - `NONE`, `ALL`, `UNIQUE`, `ITEM`, `LIVING`, `PLAYER`, `NEUTRAL`, `HOSTILE`
* (int) radius - area around the player to search for entities

`widget.setTrackingType('LIVING', 32)`

searches for all livings in a radius of 32 blocks around the player

## setTrackingFilter(type, metaindex)
* (String) type - item/creature name
* (int) metaindex - metaindex for Items

sets a filter on the tracker

## setTrackingEntity(uniqueid)
* (String) uniqueid - entitys unique id

sets the uuid for the unique filter, "none" or empty String disables the filter

### examples

`widget.setTrackingType("NEUTRAL", 32)` 
`widget.setTrackingFilter("sheep")

searches for sheeps in a radius of 32 blocks

`widget.setTrackingType("ITEM", 32)`
`widget.setTrackingFilter("minecraft:cobblestone", 0)` 

searches for cobblestone on the ground

`widget.setTrackingType("ALL", 32)` 

tracks everything in a radius of 32 blocks



## loadOBJ(data)
* (String) wavefront object data

loads the model, which is displayed at entities, from the parsed object data


### methods
* [default](Widget_Methods_default)
  * [getID](Widget_Methods_default#getID)
  * [removeWidget](Widget_Methods_default#removeWidget)
  * [getRenderPosition](Widget_Methods_getRenderPosition)
* [private](Widget_Methods_private)
  * [getOwner](Widget_Methods_private#getOwner)
  * [getOwnerUUID](Widget_Methods_private#getOwnerUUID)
  * [setOwner](Widget_Methods_private#setOwner)
* [visibility](Widget_Methods_visibility)
  * [getLookingAt](Widget3D_Methods_visibility#getLookingAt)
  * [setLookingAt](Widget3D_Methods_visibility#setLookingAt)
  * [getViewDistance](Widget3D_Methods_visibility#getViewDistance)
  * [setViewDistance](Widget3D_Methods_visibility#setViewDistance)
  * [isVisible](Widget_Methods_visibility#isVisible)
  * [setVisible](Widget_Methods_visibility#setVisible)
  * [isVisibleThroughObjects](Widget3D_Methods_visibility#isVisibleThroughObjects)
  * [setVisibleThroughObjects](Widget3D_Methods_visibility#setVisibleThroughObjects)
* [Modifier Methods](WidgetModifiers)
  * [setFaceWidgetToPlayer](WidgetModifier_setFaceWidgetToPlayer)
  * [addColor](WidgetModifiers#addColor)
  * [addRotation](WidgetModifiers#addRotation)
  * [addScale](WidgetModifiers#addScale)
  * [addTranslation](WidgetModifiers#addTranslation)
  * [getModifiers](WidgetModifierMethods#getModifiers)
  * [removeModifier](WidgetModifierMethods#removeModifier)
  * [setCondition](WidgetModifierConditions)
* [Modifier Animation](WidgetModifiers#animation)
  * [setEasing](WidgetModifiers#seteasing)
  * [removeEasing](WidgetModifiers#removeeasing)