require ("package").loaded.ar = nil

component = require("component")
event = require("event")

ar = require("openglasses/ar")

g = component.glasses
g.removeAll()

resolution = { x = 512, y = 288 }

g.setRenderResolution("", resolution.x, resolution.y);

buttons = {}

f = ar:addToOverlay("background", "Box2D")
f.setSize(512, 288)
f.addColor(0,0,0,0)
f.addColor(0,0,0,0)
f.addColor(0.1, 0.1, 0.1, 0.5)
f.addColor(0.2, 0.2, 0.2, 0.5)
f.setCondition(3, "OVERLAY_ACTIVE", true)
f.setCondition(4, "OVERLAY_ACTIVE", true)

fBl = ar:addToOverlay("widgets_list_l", "Box2D")
fBl.setSize(70, 273)
fBl.addColor(0.1, 0.1, 0.1, 0.1)
fBl.addColor(0.1, 0.1, 0.1, 0.1)
fBl.addColor(0.1, 0.1, 0.1, 0.2)
fBl.addColor(0, 0, 0, 0.2)
fBl.setCondition(3, "OVERLAY_ACTIVE", true)
fBl.setCondition(4, "OVERLAY_ACTIVE", true)
fBl.addTranslation(0, 15, 0)

fBr = ar:addToOverlay("widgets_list_r", "Box2D")
fBr.setSize(resolution.x - 70, 273)
fBr.addColor(0.1, 0.1, 0.1, 0.1)
fBr.addColor(0.1, 0.1, 0.1, 0.1)
fBr.addColor(0.1, 0.1, 0.1, 0.2)
fBr.addColor(0, 0, 0, 0.2)
fBr.setCondition(3, "OVERLAY_ACTIVE", true)
fBr.setCondition(4, "OVERLAY_ACTIVE", true)
fBr.addTranslation(0, 15, 0)

fMM = ar:addToOverlay("mainmenue", "Box2D")
fMM.setSize(resolution.x, 15)
fMM.addColor(0, 0, 0, 0.1)
fMM.addColor(0.01, 0.01, 0.01, 0.1)
fMM.addColor(0.01, 0.01, 0.01, 0.2)
fMM.addColor(0, 0, 0, 0.2)
fMM.setCondition(3, "OVERLAY_ACTIVE", true)
fMM.setCondition(4, "OVERLAY_ACTIVE", true)

fMMS = ar:addToOverlay("mainmenue", "Text2D")
fMMS.setText("openGlasses Demo v2")
fMMS.addColor(1, 1, 1, 0.2)
fMMS.addColor(1, 1, 1, 0.8)
fMMS.setCondition(2, "OVERLAY_ACTIVE", true)
fMMS.addTranslation(2, 2, 0)

function addButton(x, y, w, h, text, cb, widget, r, g, b)
  local alpha = 0.1
  local buttonIndex = #buttons + 1
  buttons[buttonIndex] = {}
  buttons[buttonIndex].el = {}
  buttons[buttonIndex].el[1] = ar:addToOverlay("buttons", "Box2D")
  buttons[buttonIndex].el[1].setSize(w, h)
  buttons[buttonIndex].el[1].addTranslation(x, y, 0)
  buttons[buttonIndex].el[1].addColor(r, g, b, alpha)
  buttons[buttonIndex].el[1].addColor(r, g, b, alpha+0.1)
  buttons[buttonIndex].el[1].addColor(r, g, b, alpha+0.3)
  buttons[buttonIndex].el[1].addColor(r, g, b, alpha+0.1)
  buttons[buttonIndex].el[1].setCondition(5, "OVERLAY_ACTIVE", true)
  buttons[buttonIndex].el[1].setCondition(6, "OVERLAY_ACTIVE", true)
  buttons[buttonIndex].el[2] = ar:addToOverlay("buttons", "Text2D")
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
  return buttons[buttonIndex]
end

function configWidget(i, status)
  local alpha = 0.1

  if status == "active" then
	alpha = 0.5
  end

  for j=1,4 do buttons[i].el[1].removeModifier(3) end

  buttons[i].el[1].addColor(buttons[i].r, buttons[i].g, buttons[i].b, alpha)
  buttons[i].el[1].addColor(buttons[i].r, buttons[i].g, buttons[i].b, alpha+0.1)
  buttons[i].el[1].addColor(buttons[i].r, buttons[i].g, buttons[i].b, alpha+0.3)
  buttons[i].el[1].addColor(buttons[i].r, buttons[i].g, buttons[i].b, alpha+0.1)
  buttons[i].el[1].setCondition(5, "OVERLAY_ACTIVE", true)
  buttons[i].el[1].setCondition(6, "OVERLAY_ACTIVE", true)
end

selectedWidget = false

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

modifierList = {}
toolsList = {}

function removeWidget(bar)
	bar = selectedWidget
	selectWidget(selectedWidget)       -- deselect the widget
	for i=1,#buttons do
	  if buttons[i].widget == buttons[bar].widget then
		buttons[i].el[1].removeWidget()  -- remove button in menue
		buttons[i].el[2].removeWidget()  -- remove button in menue
	  end
	end

	buttons[bar].widget.removeWidget() -- remove widget from world/overlay
	table.remove(buttons, bar)
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
	end
	toolsList[1] = addButton(0, 132, 70, 10, "remove widget", function(foo) removeWidget(foo) end, buttons[i].widget, 1, 0, 0)
	toolsList[2] = addButton(0, 144, 70, 10, "dump modifiers", function() dumpModifiers() end, buttons[i].widget, 0.5, 0.5, 1)
end

s = 0
function addWidgetWorld(i)
	local w = ar:addToWorld("world", ar.WORLD_WIDGETS[i].name)
	addButton(0, 22 + s * 12, 70, 10, ar.WORLD_WIDGETS[i].name, function(foo) selectWidget(foo) end, w, 0, 0, 1)
    s = s + 1
end

function addWidgetOverlay(i)
	local w = ar:addToOverlay("overlay", ar.OVERLAY_WIDGETS[i].name)
	w.addTranslation(70, 30, 0)
	if ar.OVERLAY_WIDGETS[i].name == "Item2D" then
		w.addTranslation(50, 50, 0)
		w.addScale(32, 32, 32)
		w.addRotation(180, 0, 0, 1)
	end
	addButton(0, 22 + s * 12, 70, 10, ar.OVERLAY_WIDGETS[i].name, function(foo) selectWidget(foo) end, w, 0, 0, 1)
    s = s + 1
end

for i=1,#ar.OVERLAY_WIDGETS do
  addButton(resolution.x - 70, 10 + i * 12, 70, 10, ar.OVERLAY_WIDGETS[i].name, function() addWidgetOverlay(i) end, nil, 0, 1, 0)
end

for i=1,#ar.WORLD_WIDGETS do
  addButton(resolution.x - 70, 10 + (1+i+#ar.OVERLAY_WIDGETS) * 12, 70, 10, ar.WORLD_WIDGETS[i].name, function() addWidgetWorld(i) end, nil, 0, 1, 0)
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

event.listen("interact_overlay", clickEvent)
event.pull("interrupted")
event.ignore("interact_overlay", clickEvent)
