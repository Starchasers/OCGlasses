component = require("component")
event = require("event")

g = component.glasses
g.removeAll()

buttons = {}

selectedWidget = false

modifierList = {}
toolsList = {}


openglasses = {
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




function drawFrames()
	f = openglasses:addToOverlay("background", "Box2D")
	f.setSize(512, 288)
	f.addColor(0,0,0,0)
	f.addColor(0,0,0,0)
	f.addColor(0.1, 0.1, 0.1, 0.5)
	f.addColor(0.2, 0.2, 0.2, 0.5)
	f.setCondition(3, "OVERLAY_ACTIVE", true)
	f.setCondition(4, "OVERLAY_ACTIVE", true)

	fBl = openglasses:addToOverlay("widgets_list_l", "Box2D")
	fBl.setSize(70, 273)
	fBl.addColor(0.1, 0.1, 0.1, 0.1)
	fBl.addColor(0.1, 0.1, 0.1, 0.1)
	fBl.addColor(0.1, 0.1, 0.1, 0.2)
	fBl.addColor(0, 0, 0, 0.2)
	fBl.setCondition(3, "OVERLAY_ACTIVE", true)
	fBl.setCondition(4, "OVERLAY_ACTIVE", true)
	fBl.addTranslation(0, 15, 0)

	fBr = openglasses:addToOverlay("widgets_list_r", "Box2D")
	fBr.setSize(resolution.x - 70, 273)
	fBr.addColor(0.1, 0.1, 0.1, 0.1)
	fBr.addColor(0.1, 0.1, 0.1, 0.1)
	fBr.addColor(0.1, 0.1, 0.1, 0.2)
	fBr.addColor(0, 0, 0, 0.2)
	fBr.setCondition(3, "OVERLAY_ACTIVE", true)
	fBr.setCondition(4, "OVERLAY_ACTIVE", true)
	fBr.addTranslation(0, 15, 0)

	fMM = openglasses:addToOverlay("mainmenue", "Box2D")
	fMM.setSize(resolution.x, 15)
	fMM.addColor(0, 0, 0, 0.1)
	fMM.addColor(0.01, 0.01, 0.01, 0.1)
	fMM.addColor(0.01, 0.01, 0.01, 0.2)
	fMM.addColor(0, 0, 0, 0.2)
	fMM.setCondition(3, "OVERLAY_ACTIVE", true)
	fMM.setCondition(4, "OVERLAY_ACTIVE", true)

	fMMS = openglasses:addToOverlay("mainmenue", "Text2D")
	fMMS.setText("openGlasses Demo v3")
	fMMS.addColor(1, 1, 1, 0.2)
	fMMS.addColor(1, 1, 1, 0.8)
	fMMS.setCondition(2, "OVERLAY_ACTIVE", true)
	fMMS.addTranslation(2, 2, 0)
end

function getWidgetCount()
	local count = 0
	for i=1,#buttons do
		if buttons[i].group == "widgets" then count = count + 1; end; end

	return count;
end

function addWidgetWorld(i)
	local w = openglasses:addToWorld("world", openglasses.WORLD_WIDGETS[i].name)
	addButton(0, 22 + getWidgetCount() * 12, 70, 10, openglasses.WORLD_WIDGETS[i].name, function(foo) selectWidget(foo) end, w, 0, 0, 1, "widgets")
end

function addWidgetOverlay(i)
	local w = openglasses:addToOverlay("overlay", openglasses.OVERLAY_WIDGETS[i].name)
	w.addTranslation(70, 30, 0)
	if openglasses.OVERLAY_WIDGETS[i].name == "Item2D" then
		w.addTranslation(50, 50, 0)
		w.addScale(32, 32, 32)
		w.addRotation(180, 0, 0, 1)
	end
	addButton(0, 22 + getWidgetCount() * 12, 70, 10, openglasses.OVERLAY_WIDGETS[i].name, function(foo) selectWidget(foo) end, w, 0, 0, 1, "widgets")
end

function addButton(x, y, w, h, text, cb, widget, r, g, b, group)
	local alpha = 0.1
	local buttonIndex = #buttons + 1
	buttons[buttonIndex] = {}
	buttons[buttonIndex].el = {}
	buttons[buttonIndex].el[1] = openglasses:addToOverlay("buttons", "Box2D")
	buttons[buttonIndex].el[1].setSize(w, h)
	buttons[buttonIndex].el[1].addTranslation(x, y, 0)
	buttons[buttonIndex].el[1].addColor(r, g, b, alpha)
	buttons[buttonIndex].el[1].addColor(r, g, b, alpha+0.1)
	buttons[buttonIndex].el[1].addColor(r, g, b, alpha+0.3)
	buttons[buttonIndex].el[1].addColor(r, g, b, alpha+0.1)
	buttons[buttonIndex].el[1].setCondition(5, "OVERLAY_ACTIVE", true)
	buttons[buttonIndex].el[1].setCondition(6, "OVERLAY_ACTIVE", true)
	buttons[buttonIndex].el[2] = openglasses:addToOverlay("buttons", "Text2D")
	buttons[buttonIndex].el[2].setText(text)
	buttons[buttonIndex].el[2].addTranslation(x+2, y+3, 0)
	buttons[buttonIndex].el[2].addScale(0.8, 0.8, 0.8)
	buttons[buttonIndex].r = r
	buttons[buttonIndex].g = g
	buttons[buttonIndex].b = b
	buttons[buttonIndex].x = x
	buttons[buttonIndex].y = y
	buttons[buttonIndex].w = w
	buttons[buttonIndex].h = h
	buttons[buttonIndex].widget = widget
	buttons[buttonIndex].text = text
	buttons[buttonIndex].cb = cb
	buttons[buttonIndex].group = group
	return buttons[buttonIndex]
end

function updateButtons()
	local index = 1;
	for i=1,#buttons do
		if buttons[i].group == "widgets" then
			buttons[i].y = (22 + (index-1)*12)
			buttons[i].el[1].updateModifier(2, buttons[i].x, buttons[i].y, 0)
		 	buttons[i].el[2].updateModifier(1, buttons[i].x, buttons[i].y+3, 0)
			index = (index + 1)
		end; end
end

function configWidget(i, status)
	local alpha = 0.1

	if status == "active" then
		alpha = 0.5; end

	for j=1,4 do
		buttons[i].el[1].removeModifier(3) end

	buttons[i].el[1].addColor(buttons[i].r, buttons[i].g, buttons[i].b, alpha)
	buttons[i].el[1].addColor(buttons[i].r, buttons[i].g, buttons[i].b, alpha+0.1)
	buttons[i].el[1].addColor(buttons[i].r, buttons[i].g, buttons[i].b, alpha+0.3)
	buttons[i].el[1].addColor(buttons[i].r, buttons[i].g, buttons[i].b, alpha+0.1)
	buttons[i].el[1].setCondition(5, "OVERLAY_ACTIVE", true)
	buttons[i].el[1].setCondition(6, "OVERLAY_ACTIVE", true)
end

function printTable(t, x)
	if type(t) ~= "table" then
		print("got something which isn't a table -.-")
		return
	end
	local term = require("term")
	local suffix = ""
	for f=1,x do
		suffix = " "..suffix
	end

	local output = false
	for i=1,#t do
		if type(t[i]) == "table" then
			printTable(t[i], (x+1))
		else
			term.write(suffix.." "..t[i])
			term.write("\t")
			output = true
		end
	end
	if(output == true) then term.write("\n") end
end

function dumpModifiers()
	if selectedWidget == false then return; end
	local ser = require("serialization")
	require("term").clear()
	print("")
	print("#"..buttons[selectedWidget].text.." modifiers:")
	local modifiers = buttons[selectedWidget].widget.getModifiers()
	printTable(modifiers, 1)
end

function removeWidget(bar)
	bar = selectedWidget
	selectWidget(bar)       -- deselect the widget
	for i=1,#buttons do
		if buttons[i].widget == buttons[bar].widget then
			buttons[i].el[1].removeWidget()  -- remove button in menue
			buttons[i].el[2].removeWidget()  -- remove button in menue
		end
	end

	buttons[bar].widget.removeWidget() -- remove widget from world/overlay
	table.remove(buttons, bar)
	updateButtons()
end

function deselectWidget()
	for i=1,#toolsList do
		toolsList[i].el[2].removeWidget()
		toolsList[i].el[1].removeWidget()
		toolsList[i] = nil
	end
	toolsList = {}

	for j=1,#modifierList do
		modifierList[j].el[2].removeWidget()
		modifierList[j].el[1].removeWidget()
		modifierList[j] = nil
	end
	modifierList = {}
end

function selectWidget(i)
	if i == selectedWidget then
		configWidget(selectedWidget, "inactive")
		deselectWidget()
		selectedWidget = false
		return
	elseif selectedWidget ~= false then
		configWidget(selectedWidget, "inactive")
		deselectWidget()
	end

	selectedWidget = i
	configWidget(i, "active")

	local modifiers = buttons[i].widget.getModifiers()
	for m=1,#modifiers do
		modifierList[m] = addButton(0, 144+(m*12), 70, 10, "#" .. modifiers[m][1] .. " " .. modifiers[m][2], function() print("clicked") end, buttons[i].widget, 0, 1, 1)
		os.sleep(0)
	end
	toolsList[1] = addButton(0, 132, 70, 10, "remove widget", function(foo) removeWidget(foo) end, buttons[i].widget, 1, 0, 0)
	toolsList[2] = addButton(0, 144, 70, 10, "dump modifiers", function() dumpModifiers() end, buttons[i].widget, 0.5, 0.5, 1)
end

function clickEvent(id, device, user, x, y, button, maxX, maxY)
	for i=1,#buttons do
		local tmpX = math.floor(buttons[i].x)
		local tmpY = math.floor(buttons[i].y)
		local tmpXM = math.floor(tmpX + buttons[i].w)
		local tmpYM = math.floor(tmpY + buttons[i].h)

		if tmpX <= x and tmpXM >= x and tmpY <= y and tmpYM >= y then
			buttons[i].cb(i)
		end
	end
end
