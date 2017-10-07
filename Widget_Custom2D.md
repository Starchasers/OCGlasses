### 2D Overlay Widget
## Custom2D
`widget = component.glasses.addCustom2D();`

adds a Custom Shape to the 2D Overlay

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
* [private](Widget_Methods_private)
  * [getOwner](Widget_Methods_private#getOwner)
  * [getOwnerUUID](Widget_Methods_private#getOwnerUUID)
  * [setOwner](Widget_Methods_private#setOwner)
* [alignment](Widget_Methods_alignments)
  * [setVerticalAlign](Widget_Methods_alignments#setVerticalAlign)
  * [setHorizontalAlign](Widget_Methods_alignments#setHorizontalAlign)
* [visibility](Widget_Methods_visibility)
  * [isVisible](Widget_Methods_visibility#isVisible)
  * [setVisible](Widget_Methods_visibility#setVisible)
  
* [Modifier Methods](WidgetModifiers)
  * [addColor](WidgetModifiers#addColor)
  * [addRotation](WidgetModifiers#addRotation)
  * [addScale](WidgetModifiers#addScale)
  * [addTranslation](WidgetModifiers#addTranslation)
  * [getModifiers](WidgetModifierMethods#getModifiers)
  * [removeModifier](WidgetModifierMethods#removeModifier)
  * [setCondition](WidgetModifierConditions)