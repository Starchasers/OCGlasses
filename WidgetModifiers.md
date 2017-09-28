# Modifiers:

### addColor
`addColor(float red, float green, float blue, float alpha)`

### addRotation
`addRotation(float angle, float x, float y, float z)`

### addScale
`addScale(float x, float y, float z)`

### addTranslation
`addTranslation(float x, float y, float z)`



# Animation
Widgets can be animated by binding an easing function to a modifier. So you can animate color changes, movement, rotation and scale changes with a simple syntax

## methods

### setEasing


### removeEasing
* (int) modifier Index
* (int) easing Index
* (String) variable name

`widget->removeEasing(4, 2, "x")`

removes easing 2 from list "x" of modifier 4


### easing functions [previews](http://easings.net/)
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

### easing types
* IN
* OUT
* INOUT

### loop/repeat modes
* `DEFAULT` only run once 
* `LOOP` loop/playback the easing back and forth
* `REPEAT` repeat the easing
