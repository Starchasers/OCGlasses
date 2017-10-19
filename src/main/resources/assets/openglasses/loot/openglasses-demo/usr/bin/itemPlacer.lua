-- this script places items when rightclicking a real block in the world
-- geolyzer upgrade IS REQUIRED!
-- place a chest next to a transposer and put some items as reference in

require("openglasses/itemPlacer")
itemMargin = 4
itemScale = 32
itemPadding = 2

playerName = "ben_mkiv"
rotateWidget = true

-- read items from transposers
for address,type in pairs(component.list("transposer")) do
    getItemsFromTransposer(component.proxy(address)); end

-- read items from databases
for address,type in pairs(component.list("database")) do
    getItemsFromDatabase(component.proxy(address)); end


-- read items from ae2
for address,type in pairs(component.list("me_controller")) do
    getItemsFromAE2(component.proxy(address));
end
--for address,type in pairs(component.list("me_interface")) do
--    getItemsFromAE2(component.proxy(address)); end


itemSize = (itemScale + 2*(itemPadding+itemMargin))

-- create overlay button for each item
for i=1,#ITEMS do
    ITEMS[i].widgetBox = glassesTerminal.addBox2D()
    ITEMS[i].widgetBox.setSize(itemScale+(2*itemPadding), itemScale+(2*itemPadding))
    ITEMS[i].widgetBox.addColor(0.3,0.3,0.3, 0.8)
    ITEMS[i].widgetBox.addColor(0.3,0.3,0.3, 0.7)
    ITEMS[i].widgetBox.addAutoTranslation(50, 0)
    ITEMS[i].widgetBox.addTranslation(-(#ITEMS*itemSize/2), 0, 0);
    ITEMS[i].widgetBox.addTranslation(((i-1)*itemSize), itemMargin, 0)


    ITEMS[i].widget = glassesTerminal.addItem2D()
    ITEMS[i].widget.setItem(ITEMS[i].name, ITEMS[i].damage)
    ITEMS[i].widget.addAutoTranslation(50, 0)
    ITEMS[i].widget.addTranslation(-(#ITEMS*itemSize/2), 0, 0);
    ITEMS[i].widget.addTranslation(((i-1)*itemSize)+itemPadding, itemMargin+itemPadding, 0)
    ITEMS[i].widget.addScale(itemScale, itemScale, itemScale)
    os.sleep(0)
end

updateOverlayWidgetsPositions(nil, nil, playerName, nil, nil, nil);

selectedItemWidget.addTranslation(itemMargin, itemSize, 0)
selectedItemWidget.addAutoTranslation(50, 0)
selectedItemWidget.setText("select an item to place into the world")
selectedItemWidget.setHorizontalAlign("center")

-- load widgets from config file
loadWidgets()

--register event listeners and idle until user interrupts
event.listen("interact_world_block_right", setItem)
event.listen("interact_overlay", touchEvent)
event.listen("glasses_screen_size", updateOverlayWidgetsPositions)
print("\n# itemPlacer loaded, close with [CTRL] + [C]")
event.pull("interrupted")
event.ignore("glasses_screen_size", updateOverlayWidgetsPositions)
event.ignore("interact_overlay", touchEvent)
event.ignore("interact_world_block_right", setItem)

-- remove all widgets from glasses
glassesTerminal.removeAll()

-- save widgets to config file
saveWidgets();