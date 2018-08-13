args = {...}

if #args == 0 then
	print("usage: objLoader 'filename'");
	os.exit()
end

fileHandler = io.open(args[1], "r");
objData = fileHandler:read("*a");
fileHandler:close();

glassesTerminal = require("component").glasses;
glassesTerminal.removeAll();
wavefrontWidget = glassesTerminal.addOBJModel3D();

wavefrontWidget.loadOBJ(objData);

wavefrontWidget.addTranslation(0.5, 2.5, 0.5);
wavefrontWidget.addColor(0, 1, 0, 0.8);

rotationModifier = wavefrontWidget.addRotation(0, 0, 1, 0)

wavefrontWidget.setEasing(rotationModifier, "LINEAR", "INOUT", 3000, "deg", 0, 360, "repeat");
