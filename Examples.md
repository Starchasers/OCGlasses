# Animated
### rotating monkey head
`pastebin get mm7QbueM /home/suzanne.obj`

	fh = io.open("/home/suzanne.obj", "r"); 
	objData = fh:read("*a"); 
	fh:close(); 

	glassesTerminal = require("component").glasses; 
	glassesTerminal.removeAll(); 
	customOBJ = glassesTerminal.addOBJModel3D(); 

	customOBJ.loadOBJ(objData); 

	customOBJ.addTranslation(0, 2.5, 0); 
	customOBJ.addColor(0, 1, 0, 0.8); 

	customOBJ.setEasing(customOBJ.addRotation(0, 0, 1, 0), "LINEAR", "INOUT", 3000, "deg", 0, 360, "repeat");



### rotating cube
	glassesTerminal = require("component").glasses; 
	glassesTerminal.removeAll(); 
	cube3D = glassesTerminal.addCube3D(); 
	cube3D.addTranslation(0, 2, 0); 
	cube3D.addColor(1, 1, 1, 0.7); 

	cube3D.addTranslation(0.5, 0.5, 0.5);
	cube3D.setEasing(cube3D.addRotation(0, 0, 1, 0), "LINEAR", "IN", 360, "deg", 0, 360, "repeat");
	cube3D.addTranslation(-0.5, -0.5, -0.5); 


# Static
### 2D Overlay Item2D
	glassesTerminal = require("component").glasses
	glassesTerminal.removeAll()

	Widget_ItemIcon = glassesTerminal.addItem2D()
	Widget_ItemIcon.setItem("minecraft:iron_sword", 0)
	Widget_ItemIcon.addScale(40, 40, 40)      -- modifier #1
	Widget_ItemIcon.addRotation(180, 1, 0, 0) -- modifier #2

### 2D Overlay Text2D
	glassesTerminal = require("component").glasses
	glassesTerminal.removeAll()

	Widget_TextLabel = glassesTerminal.addText2D()
	Widget_TextLabel.setText("openGlasses") -- no modifier!
	Widget_TextLabel.addColor(1, 0, 0, 0.5)
	Widget_TextLabel.setCondition(Widget_TextLabel.addColor(0, 1, 0, 0.5), "OVERLAY_ACTIVE", true)


### 3D World Cube3D
	glassesTerminal = require("component").glasses
	glassesTerminal.removeAll()

	Widget_Cube3D = glassesTerminal.addCube3D()
	Widget_Cube3D.addTranslation(0, 3, 0)
	Widget_Cube3D.addColor(1, 1, 0.4, 0.8)
	Widget_Cube3D.addRotation(45, 0, 0, 1)
	Widget_Cube3D.setCondition(Widget_Cube3D.addScale(2, 2, 2), "IS_LIGHTLEVEL_MIN_7", true)


### 3D World Text3D
	glassesTerminal = require("component").glasses
	glassesTerminal.removeAll()

	Widget_FloatingText = glassesTerminal.addText3D()
	Widget_FloatingText.setText("openGlasses") -- no modifier!
	Widget_FloatingText.addTranslation(0, 4, 0) -- this is modifier #1
	Widget_FloatingText.addColor(1, 1, 0, 0.8)  -- this is modifier #2


