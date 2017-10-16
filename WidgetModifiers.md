# Widget Modifiers

## methods
### addColor
* (float) red
* (float) green
* (float) blue
* (float) alpha

returns (int) modifierIndex

`widget.addColor(1, 0, 0, 0.5)`

adds a red color with 50% opacity

### addRotation
* (float) deg
* (float) x
* (float) y
* (float) z

returns (int) modifierIndex

`widget.addRotation(90, 0, 1, 0)`

rotates the widget by 90° on the Y-Axis

### addScale
* (float) x
* (float) y
* (float) z

returns (int) modifierIndex

`widget.addScale(2, 2, 2)`

scales widget by 2 on all axis

### addTranslation
* (float) x
* (float) y
* (float) z

returns (int) modifierIndex

`widget.addTranslation(0, 1, 0)`

moves widget on y axis by 1


### updateModifier
* (int) modifier index
* (float) values

`widget.updateModifier(index, 0, 1, 0)` // updates red/green/blue or x/y/z

`widget.updateModifier(index, 1, 0, 1, 0)` // updates red/green/blue/alpha or deg/x/y/z


# Animation
Widgets can be animated by binding an easing function to a modifier. So you can animate color / scale changes, movement and rotation with a simple syntax

## methods

### setEasing
* (int) modifierIndex
* (String) easing functionname
* (String) easing type
* (float) duration in milliseconds
* (String) variable name
* (float) min value
* (float) max value
* (String) loop/repeating mode

`widget.setEasing(widget.addRotation(0, 0, 1, 0), "LINEAR", "IN", 3000, "deg", 0, 360, "repeat")`

this example makes our widget rotate around the y-axis, one rotation takes 3seconds



### removeEasing
* (int) modifier Index
* (String) variable name

`widget.removeEasing(4, "x")`

removes easing from list "x" of modifier 4


## easing functions [previews](http://easings.net/)
* BACK
* BOUNCE
* CIRC
* CUBIC
* ELASTIC
* EXPO
* LINEAR
* QUAD
* QUART
* QUINT
* SINE

## easing types
* IN
* OUT
* INOUT

## loop/repeat modes
* `DEFAULT` only run once 
* `LOOP` loop/playback the easing back and forth
* `REPEAT` repeat the easing
