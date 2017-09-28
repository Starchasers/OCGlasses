	glassesTerminal = require("component").glasses
	glassesTerminal.removeAll()

	Widget_ItemIcon = glassesTerminal.addItemIcon()
	Widget_ItemIcon.setItem("minecraft:iron_sword", 0)
	Widget_ItemIcon.addScale(40, 40, 40)      -- modifier #1
	Widget_ItemIcon.addTranslation(2, 3, 0)   -- modifier #2
	Widget_ItemIcon.addRotation(180, 1, 0, 0) -- modifier #3
	Widget_ItemIcon.setFaceWidgetToPlayer(true) -- enables auto rotation, so the widget rotates to face the player

	Widget_Cube3D = glassesTerminal.addCube3D()
	Widget_Cube3D.addTranslation(0, 3, 0)  -- modifier #1
	Widget_Cube3D.addColor(1, 1, 0.4, 0.8) -- modifier #2
	Widget_Cube3D.addRotation(45, 0, 0, 1) -- modifier #3
	Widget_Cube3D.addScale(2, 2, 2)        -- modifier #4
	Widget_Cube3D.setCondition(4, "IS_LIGHTLEVEL_MIN_7", true)

	Widget_FloatingText = glassesTerminal.addFloatingText()
	Widget_FloatingText.setText("openGlasses") -- no modifier!
	Widget_FloatingText.addTranslation(0, 4, 0) -- this is modifier #1
	Widget_FloatingText.addColor(1, 1, 0, 0.8)  -- this is modifier #2

	Widget_TextLabel = glassesTerminal.addTextLabel()
	Widget_TextLabel.setText("openGlasses") -- no modifier!
	Widget_TextLabel.addColor(1, 0, 0, 0.5)  -- this is modifier #1
	Widget_TextLabel.addColor(0, 1, 0, 0.5)  -- this is modifier #2
	Widget_TextLabel.setCondition(2, "OVERLAY_ACTIVE", true)




### rotating cube
	glassesTerminal = component.glasses; 
	glassesTerminal.removeAll(); 
	cube3D = glassesTerminal.addCube3D(); 
	cube3D.addTranslation(0, 2, 0); 
	cube3D.addColor(1, 1, 1, 0.7); 

	cube3D.addTranslation(0.5, 0.5, 0.5);
	cube3D.setEasing(cube3D.addRotation(1, 0, 1, 0), "LINEAR", "IN", 360, "deg", 0, 360, "repeat");
	cube3D.addTranslation(-0.5, -0.5, -0.5); 
