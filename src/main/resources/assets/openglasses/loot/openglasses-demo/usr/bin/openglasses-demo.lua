ar = require("openglasses/openglasses-demo")

resolution = 500

g.setRenderResolution("", resolution, resolution);
drawFrames();

for i=1,#openglasses.OVERLAY_WIDGETS do
	addButton(resolution - 70, 10 + i * 12, 70, 10, openglasses.OVERLAY_WIDGETS[i].name, function() addWidgetOverlay(i) end, nil, 0, 1, 0, "overlay_widgets")
	os.sleep(0)
end

for i=1,#openglasses.WORLD_WIDGETS do
	addButton(resolution - 70, 10 + (1+i+#openglasses.OVERLAY_WIDGETS) * 12, 70, 10, openglasses.WORLD_WIDGETS[i].name, function() addWidgetWorld(i) end, nil, 0, 1, 0, "world_widgets")
	os.sleep(0)
end

function screenSizeEvent(event, uuid, playername, width, height, scale)
	print("fixing render resolution ("..math.floor(width*scale).."x"..math.floor(height*scale)..") for " .. playername)
	g.setRenderResolution(playername, resolution, resolution * height/width);
end

event.listen("interact_overlay", clickEvent)
event.listen("glasses_screen_size", screenSizeEvent)

g.requestResolutionEvents();

print("\n# openGlasses Demo loaded, close with [CTRL] + [C]")
event.pull("interrupted")

event.ignore("interact_overlay", clickEvent)
event.ignore("glasses_screen_size", screenSizeEvent)

g.removeAll()
