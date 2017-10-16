### 3D World Widget
## Custom3D
`widget = component.glasses.addCustom3D();`

adds a Custom Shape to the 3D World Space

## addVertex
`widget.addVertex(x, y, z)`
adds a vertex

## setVertex
`widget.setVertex(index, x, y, z)`
updates a vertex

## removeVertex
`widget.removeVertex(index)`
removes a vertex

## getVertexCount
`=widget.getVertexCount()`

(Integer) return Vertex Count

## setGLMODE
`widget.setGLMODE("TRIANGLES")`
* (String) mode
  * TRIANGLES
  * TRIANGLE_STRIP

## setShading
`widget.setShading("SMOOTH")`
* (String) mode
  * FLAT
  * SMOOTH


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