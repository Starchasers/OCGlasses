``g = require("component").glasses
g.removeAll()

f = g.addItemIcon()
f.addScale(40, 40, 40)
f.addTranslation(2, 3, 0)
f.setItem("wooden_shovel", 0)
f.addRotation(180, 1, 0, 0)

c = g.addCube3D()
c.addTranslation(0, 3, 0)
c.addColor(1, 1, 0.4, 0.8)
c.addRotation(45, 0, 0, 1)
c.addScale(2, 2, 2)
c.setCondition(4, "IS_LIGHTLEVEL_MIN", true, 7)

s = g.addFloatingText()
s.setText("Hello!?")
s.addTranslation(0, 4, 0)
s.addColor(1, 1, 0, 0.8)

l = g.addTextLabel()
l.setText("Hello")
l.addColor(1, 0, 0, 0.5)
l.addColor(0, 1, 0, 0.5)
l.setCondition(2, "OVERLAY_ACTIVE", true)``