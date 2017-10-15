-- load some mesh as marker
fh = io.open("/usr/wavefrontObjects/cubeMatrix.obj", "r")
wavefrontObj = fh:read("*a")
fh:close()


-- load glasses terminal and remove old widgets
glassesTerminal = require("component").glasses
glassesTerminal.removeAll()


-- add a entity tracker which filters for living creatures
entityTracker = glassesTerminal.addEntityTracker3D()
entityTracker.loadOBJ(wavefrontObj)
entityTracker.setTrackingType("living", 32)
entityTracker.addTranslation(0, 0.5, 0)

-- rotate the mesh in a loop
entityTracker.setEasing(entityTracker.addRotation(0, 1, 1, 0), "LINEAR", "INOUT", 3000, "deg", 0, 360, "repeat")

-- fade the color in a loop
colorModifier = entityTracker.addColor(0, 1, 0, 0.4)
entityTracker.setEasing(colorModifier, "LINEAR", "INOUT", 1000, "red", 0, 0.6, "loop")
entityTracker.setEasing(colorModifier, "LINEAR", "INOUT", 1500, "alpha", 0.2, 0.4, "loop")

-- change color of marker for focused entity
entityTracker.setCondition(entityTracker.addColor(1, 1, 0, 1), "IS_FOCUSED_ENTITY", true);


-- add another entitytracker which filters ALL dirt items
itemTracker = glassesTerminal.addEntityTracker3D()
itemTracker.loadOBJ(wavefrontObj)
itemTracker.setTrackingType("item", 32)
itemTracker.setTrackingFilter("minecraft:dirt")
itemTracker.addTranslation(0, 0.5, 0)
itemTracker.addColor(1, 1, 1, 0.3)
itemTracker.setEasing(itemTracker.addRotation(0, -1, -1, 0), "LINEAR", "INOUT", 3000, "deg", 0, 360, "repeat")


-- and another one for andesite
itemTracker2 = glassesTerminal.addEntityTracker3D()
itemTracker2.loadOBJ(wavefrontObj)
itemTracker2.setTrackingType("item", 32)
itemTracker2.setTrackingFilter("minecraft:stone", 5)
itemTracker2.addTranslation(0, 0.5, 0)
itemTracker2.addColor(0, 0, 1, 0.3)
itemTracker2.setEasing(itemTracker2.addRotation(0, -1, -1, 0), "LINEAR", "INOUT", 3000, "deg", 0, 360, "repeat")
