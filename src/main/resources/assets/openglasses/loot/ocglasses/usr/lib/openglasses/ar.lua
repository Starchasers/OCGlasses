local openglasses = {
  widgets_overlay = {},
  widgets_world = {},

  OVERLAY_WIDGETS = {},
  WORLD_WIDGETS = {}
}

function openglasses:init()
  self.OVERLAY_WIDGETS = {}
  self.OVERLAY_WIDGETS[1] = { name = "Box2D" }
  self.OVERLAY_WIDGETS[2] = { name = "Custom2D" }
  self.OVERLAY_WIDGETS[3] = { name = "Item2D" }
  self.OVERLAY_WIDGETS[4] = { name = "OBJModel2D" }
  self.OVERLAY_WIDGETS[5] = { name = "Text2D" }

  self.WORLD_WIDGETS = {}
  self.WORLD_WIDGETS[1] = { name = "Cube3D" }
  self.WORLD_WIDGETS[2] = { name = "Custom3D" }
  self.WORLD_WIDGETS[3] = { name = "Item3D" }
  self.WORLD_WIDGETS[4] = { name = "OBJModel3D" }
  self.WORLD_WIDGETS[5] = { name = "Text3D" }
  self.WORLD_WIDGETS[6] = { name = "EntityTracker3D" }
end

function openglasses:addToWorld(group, name)
	local i = #self.WORLD_WIDGETS + 1
	local g = require("component").glasses

	if name == "Cube3D" then
		self.widgets_world[i] = g.addCube3D()
		self.widgets_world[i].addColor(1, 0, 0, 0.8)
		self.widgets_world[i].addColor(1, 0, 0, 0.5)
		self.widgets_world[i].setCondition(2, "OVERLAY_ACTIVE", true)
		self.widgets_world[i].addTranslation(-0.05, -0.05, -0.05)
		self.widgets_world[i].addScale(1.1, 1.1, 1.1)
		self.widgets_world[i].addTranslation(0, 2, 0)
	elseif name == "Text3D" then
		self.widgets_world[i] = g.addText3D()
		self.widgets_world[i].addColor(1, 0, 0, 0.8)
		self.widgets_world[i].addColor(1, 0, 0, 0.1)
		self.widgets_world[i].setCondition(2, "OVERLAY_ACTIVE", true)
		self.widgets_world[i].addColor(0, 0, 1, 1)
		self.widgets_world[i].setCondition(3, "IS_SWIMMING", true)
		self.widgets_world[i].addColor(0, 1, 1, 0.8)
		self.widgets_world[i].setCondition(4, "IS_WEATHER_RAIN", true)
		self.widgets_world[i].addColor(1, 1, 1, 0.5)
		self.widgets_world[i].setCondition(5, "IS_SNEAKING", true)
		self.widgets_world[i].setText("Hello World!")
		self.widgets_world[i].addScale(0.5, 0.5, 0.5)
		self.widgets_world[i].addTranslation(0, 2, 0)
	elseif name == "Item3D" then
		self.widgets_world[i] = g.addItem3D()
		self.widgets_world[i].setItem("minecraft:diamond_helmet", 0)
		self.widgets_world[i].setFaceWidgetToPlayer(true)
		self.widgets_world[i].addTranslation(0, 2, 0)
	elseif name == "Custom3D" then
		self.widgets_world[i] = g.addCustom3D()
		self.widgets_world[i].addTranslation(0, 2, 0)
	elseif name == "OBJModel3D" then
		self.widgets_world[i] = g.addOBJModel3D()
		self.widgets_world[i].addTranslation(0, 2, 0)
	elseif name == "EntityTracker3D" then
		self.widgets_world[i] = g.addEntityTracker3D()
		local fh = io.open("/usr/wavefrontObjects/cubeMatrix.obj", "r")
		if(fh == nil) then
			print("this function requires the cubeMatrix.obj from the wavefront objects loot disk")
			os.exit()
		end
		local wavefrontObj = fh:read("*a")
		fh:close()
		self.widgets_world[i].loadOBJ(wavefrontObj)
		self.widgets_world[i].setTrackingType("living", 32)
		-- rotate the mesh in a loop
		self.widgets_world[i].addTranslation(0, 0.5, 0)
		self.widgets_world[i].setEasing(self.widgets_world[i].addRotation(0, 1, 1, 0), "LINEAR", "INOUT", 3000, "deg", 0, 360, "repeat")

		-- fade the color in a loop
		local colorModifier = self.widgets_world[i].addColor(0, 1, 0, 0.4)
		self.widgets_world[i].setEasing(colorModifier, "LINEAR", "INOUT", 1000, "red", 0, 0.6, "loop")
		self.widgets_world[i].setEasing(colorModifier, "LINEAR", "INOUT", 1500, "alpha", 0.2, 0.4, "loop")

		-- change color of marker for focused entity
		self.widgets_world[i].setCondition(self.widgets_world[i].addColor(1, 1, 0, 1), "IS_FOCUSED_ENTITY", true);

	else
		print("invalid widget name: '"..name.."'")
		return nil
	end

	self.widgets_world[i].group = group
	return self.widgets_world[i]
end

function openglasses:addToOverlay(group, name)
	local widgetIndex = #self.widgets_overlay + 1
	local g = require("component").glasses

	if name == "Box2D" then
		self.widgets_overlay[widgetIndex] = g.addBox2D()
		self.widgets_overlay[widgetIndex].setSize(80, 40)
		self.widgets_overlay[widgetIndex].addColor(1, 1, 1, 0.5)
	elseif name == "Text2D" then
		self.widgets_overlay[widgetIndex] = g.addText2D()
		self.widgets_overlay[widgetIndex].setText("Hello World!")
	elseif name == "Item2D" then
		self.widgets_overlay[widgetIndex] = g.addItem2D()
		self.widgets_overlay[widgetIndex].setItem("minecraft:diamond_chestplate", 0)
	elseif name == "Custom2D" then
		self.widgets_overlay[widgetIndex] = g.addCustom2D()
	elseif name == "OBJModel2D" then
		self.widgets_overlay[widgetIndex] = g.addOBJModel2D()
	else
		print("invalid widget name: '"..name.."'")
		return nil
	end

	self.widgets_overlay[widgetIndex].group = group
	return self.widgets_overlay[widgetIndex]
end

openglasses:init()

return openglasses
