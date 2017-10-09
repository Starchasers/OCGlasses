### 3D World Widget
## Item3D
`widget = component.glasses.addItem3D();`

adds a Item to the 3D World Space

## setItem
* (String) material name
* (Integer) material metaindex

`widget.setItem("minecraft:stone", 6)` 

sets item icon to Andesite Block


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