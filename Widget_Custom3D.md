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
