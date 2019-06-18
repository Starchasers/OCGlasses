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

	OVERLAY_WIDGETS = {
		{ name = "Box2D" },
		{ name = "Custom2D" },
		{ name = "Item2D" },
		{ name = "OBJModel2D" },
		{ name = "Text2D" }
	},
	WORLD_WIDGETS = {
		{ name = "Cube3D" },
		{ name = "Custom3D" },
		{ name = "Item3D" },
		{ name = "OBJModel3D" },
		{ name = "Text3D" },
		{ name = "EntityTracker3D" }
	}
}


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
		local fh = io.open("/usr/wavefrontObjects/cubeMatrix.obj", "r")
		if(fh == nil) then
			print("couldnt read /usr/wavefrontObjects/cubeMatrix.obj")
			os.exit()
		end
		local wavefrontObj = fh:read("*a")
		fh:close()
		self.widgets_world[i] = g.addEntityTracker3D()
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
		self.widgets_overlay[widgetIndex].addColor(0.5, 0, 0.4, 0.5)
		self.widgets_overlay[widgetIndex].addColor(0, 0, 0.4, 0.4)
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

function drawFrames()
	f = openglasses:addToOverlay("background", "Box2D")
	f.setSize(512, 512)
	f.addColor(0,0,0,0)
	f.addColor(0,0,0,0)
	f.setCondition(f.addColor(0.1, 0.1, 0.1, 0.5), "OVERLAY_ACTIVE", true)
	f.setCondition(f.addColor(0.2, 0.2, 0.2, 0.5), "OVERLAY_ACTIVE", true)

	fBl = openglasses:addToOverlay("widgets_list_l", "Box2D")
	fBl.setSize(70, 512)
	fBl.addColor(0.1, 0.1, 0.1, 0.05)
	fBl.addColor(0.1, 0.1, 0.1, 0.05)
	fBl.setCondition(fBl.addColor(0.1, 0.1, 0.1, 0.1), "OVERLAY_ACTIVE", true)
	fBl.setCondition(fBl.addColor(0, 0, 0, 0.2), "OVERLAY_ACTIVE", true)
	fBl.addTranslation(0, 15, 0)

	fBr = openglasses:addToOverlay("widgets_list_r", "Box2D")
	fBr.setSize(70, 512)
	fBr.addColor(0.1, 0.1, 0.1, 0.05)
	fBr.addColor(0.1, 0.1, 0.1, 0.05)
	fBr.setCondition(fBr.addColor(0.1, 0.1, 0.1, 0.1), "OVERLAY_ACTIVE", true)
	fBr.setCondition(fBr.addColor(0, 0, 0, 0.2), "OVERLAY_ACTIVE", true)
	fBr.addTranslation(resolution.x, 15, 0)
	fBr.setHorizontalAlign("left")

	fMM = openglasses:addToOverlay("mainmenue", "Box2D")
	fMM.setSize(resolution.x, 15)
	fMM.addColor(0, 0, 0, 0.1)
	fMM.addColor(0.01, 0.01, 0.01, 0.1)
	fMM.setCondition(fMM.addColor(0.01, 0.01, 0.01, 0.2), "OVERLAY_ACTIVE", true)
	fMM.setCondition(fMM.addColor(0, 0, 0, 0.2), "OVERLAY_ACTIVE", true)

	fMMS = openglasses:addToOverlay("mainmenue", "Text2D")
	fMMS.setText("openGlasses Demo v4")
	fMMS.addColor(1, 1, 1, 0.05)
	fMMS.setCondition(fMMS.addColor(1, 1, 1, 0.8), "OVERLAY_ACTIVE", true)
	fMMS.addTranslation(2, 2, 0)

	fMMSi = openglasses:addToOverlay("mainmenue", "Text2D")
	fMMSi.setText("openGlasses Demo v4, press interact overlay key to interact with the UI")
	fMMSi.addColor(1, 1, 1, 0.8)
	fMMSi.setCondition(fMMSi.addColor(1, 1, 1, 0.05), "OVERLAY_ACTIVE", true)
	fMMSi.addTranslation(2, 2, 0)
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
	elseif openglasses.OVERLAY_WIDGETS[i].name == "Box2D" then
		w.addTranslation(20, 0, 0)
	elseif openglasses.OVERLAY_WIDGETS[i].name == "Text2D" then
		w.addTranslation(30, 10, 0)
	end
	addButton(0, 22 + getWidgetCount() * 12, 70, 10, openglasses.OVERLAY_WIDGETS[i].name, function(foo) selectWidget(foo) end, w, 0, 0, 1, "widgets")
end

function addButton(x, y, w, h, text, cb, widget, r, g, b, group)
	local alpha = 0.05

	local buttonIndex = #buttons + 1

	local boxElement = openglasses:addToOverlay("buttons", "Box2D")
	boxElement.setSize(w, h)
	boxElement.addTranslation(x, y, 0)
	boxElement.addColor(r, g, b, 0.05)
	boxElement.addColor(r, g, b, 0.05)
	boxElement.setCondition(boxElement.addColor(r, g, b, alpha+0.4), "OVERLAY_ACTIVE", true)
	boxElement.setCondition(boxElement.addColor(r, g, b, alpha+0.2), "OVERLAY_ACTIVE", true)
	
	local textElement = openglasses:addToOverlay("buttons", "Text2D")
	textElement.setText(text)
	textElement.addTranslation(x+2, y+3, 0)
	textElement.addScale(0.8, 0.8, 0.8)
	textElement.addColor(1, 1, 1, 0.05)
	textElement.setCondition(textElement.addColor(1, 1, 1, 1), "OVERLAY_ACTIVE", true)
	
	buttons[buttonIndex] = {}
	buttons[buttonIndex].el = { boxElement, textElement }	
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
			buttons[i].el[1].updateModifier(3, buttons[i].x, buttons[i].y, 0)
		 	buttons[i].el[2].updateModifier(1, buttons[i].x, buttons[i].y+3, 0)
			index = (index + 1)
		end; end
end

function configWidget(i, status)
	local alpha = 0.1

	if status == "active" then
		alpha = 0.6; end

	buttons[i].el[1].modifiers()[6].set(buttons[i].r, buttons[i].g, buttons[i].b, alpha+0.1)
	buttons[i].el[1].modifiers()[7].set(buttons[i].r, buttons[i].g, buttons[i].b, alpha+0.3)
end

function dumpModifiers()
	if selectedWidget == false then return; end
	require("term").clear()
	print("\n#"..buttons[selectedWidget].text.." modifiers:")
	local modifiers = buttons[selectedWidget].widget.modifiers()

	for i=1,#modifiers do
		print("#"..i.." "..modifiers[i].type())
		for name,val in pairs(modifiers[i].get()) do
			io.write(name.."="..(math.floor(val * 1000 + 0.5)/1000).."\t")
		end
		print("")
	end
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
		for j=1,#toolsList[i].el do
			toolsList[i].el[j].removeWidget()
		end
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

