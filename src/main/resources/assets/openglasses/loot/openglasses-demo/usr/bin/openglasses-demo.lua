ar = require("openglasses/openglasses-demo")

resolution = { x = 512, y = 288 }
g.setRenderResolution("", resolution.x, resolution.y);

drawFrames();

for i=1,#openglasses.OVERLAY_WIDGETS do
	addButton(resolution.x - 70, 10 + i * 12, 70, 10, openglasses.OVERLAY_WIDGETS[i].name, function() addWidgetOverlay(i) end, nil, 0, 1, 0, "overlay_widgets")
	os.sleep(0)
end

for i=1,#openglasses.WORLD_WIDGETS do
	addButton(resolution.x - 70, 10 + (1+i+#openglasses.OVERLAY_WIDGETS) * 12, 70, 10, openglasses.WORLD_WIDGETS[i].name, function() addWidgetWorld(i) end, nil, 0, 1, 0, "world_widgets")
	os.sleep(0)
end


event.listen("interact_overlay", clickEvent)
print("\n# openGlasses Demo loaded, close with [CTRL] + [C]")
event.pull("interrupted")
event.ignore("interact_overlay", clickEvent)

g.removeAll()
