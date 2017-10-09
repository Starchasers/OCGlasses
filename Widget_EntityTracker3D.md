### 3D World Widget

this widget requires [swimming](Glasses#swimming) + [sneaking](Glasses#sneaking) upgrades

## EntityTracker3D
`widget = component.glasses.addEntityTracker3D();`

adds a EntityTracker3D to the 3D World Space


## setTracking(trackingtype, radius, type, index)
* (String) trackingtype - `NONE`, `ALL`, `ENTITYID`, `ITEMTYPE`, `ENTITYTYPE`
* (int) radius - area around the player to search for entities
* (String) type - when using `ITEMTYPE` or `ENTITYTYPE` as trackingtype you can define the item/creature type here
* (int) index - (not used yet, just parse 0) ;)

this function is used to setup the tracking

### examples

`entityTracker.setTracking("ENTITYTYPE", 32, "Sheep", 0)` 

searches for sheeps in a radius of 32 blocks

`entityTracker.setTracking("ITEMTYPE", 32, "minecraft:cobblestone", 0)` 

searches for cobblestone on the groun


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