-- this script places items when rightclicking a real block in the world
-- geolyzer upgrade IS REQUIRED!
-- place a chest next to a transposer and put some items as reference in
-- or set items in a database and make it available with an adapter

require("openglasses/itemPlacer")
itemMargin = 4
itemScale = 32
itemPadding = 2


-- read items from transposers
for address,type in pairs(component.list("transposer")) do
    getItemsFromTransposer(component.proxy(address)); end

-- read items from databases
for address,type in pairs(component.list("database")) do
    getItemsFromDatabase(component.proxy(address)); end


-- create overlay button for each item
itemSize = (itemScale + 2*(itemPadding+itemMargin))

for i=1,#ITEMS do
    ITEMS[i].x = ((i-1)*itemSize) - ((#ITEMS)*itemSize/2)
    ITEMS[i].y = itemMargin
    ITEMS[i].z = 0

    ITEMS[i].widgetBox = glassesTerminal.addBox2D()
    ITEMS[i].widgetBox.setHorizontalAlign("center")
    ITEMS[i].widgetBox.setSize(itemScale+(2*itemPadding), itemScale+(2*itemPadding))
    ITEMS[i].widgetBox.addTranslation(ITEMS[i].x, ITEMS[i].y, 0)
    ITEMS[i].widgetBox.addColor(0.3,0.3,0.3, 0.8)
    ITEMS[i].widgetBox.addColor(0.3,0.3,0.3, 0.7)

    ITEMS[i].widget = glassesTerminal.addItem2D()
    ITEMS[i].widget.setItem(ITEMS[i].name, ITEMS[i].damage)
    ITEMS[i].widget.setHorizontalAlign("center")
    ITEMS[i].widget.addTranslation(ITEMS[i].x+itemPadding, ITEMS[i].y+itemPadding, 0)
    ITEMS[i].widget.addScale(itemScale, itemScale, itemScale)


end

selectedItemWidget.addTranslation(0, itemSize, 0)
selectedItemWidget.setText("select an item to place into the world")
selectedItemWidget.setHorizontalAlign("center")

-- load widgets from config file
loadWidgets()

--register event listeners and idle until user interrupts
event.listen("interact_world_block_right", setItem)
event.listen("interact_overlay", touchEvent)
event.listen("glasses_screen_size", updateClientResolution)

glassesTerminal.requestResolutionEvents("")

print("\n# itemPlacer loaded, close with [CTRL] + [C]")
event.pull("interrupted")
event.ignore("glasses_screen_size", updateClientResolution)
event.ignore("interact_overlay", touchEvent)
event.ignore("interact_world_block_right", setItem)

-- remove all widgets from glasses
glassesTerminal.removeAll()

-- save widgets to config file
saveWidgets();