-- this script places items when rightclicking a real block in the world
-- geolyzer upgrade IS REQUIRED!
-- place a chest next to a transposer and put some items as reference in
-- TODO: make this using a database upgrade?!

require("openglasses/itemPlacer")
itemMargin = 4
itemScale = 32
itemPadding = 2

local itemSize = (itemScale + 2*(itemPadding+itemMargin))

selectedItemWidget.addTranslation(itemMargin, itemSize, 0)
selectedItemWidget.setText("select an item to place into the world")

local transposer = component.transposer

print("checking inventorys")
--read items from inventorys
for side=0,(#sides-1) do
    local size = transposer.getInventorySize(side)
    if size ~= nil then
        io.write("inventory found at side " .. sides[side] .. " (size: " .. size .. ") scanning: ")

        for slot=1,size do
            io.write(".")
            local item = transposer.getStackInSlot(side, slot)
            if item ~= nil and checkItem(item) == false then
                table.insert(ITEMS, item);
            end; end
        print(" done!")
    end
end

--create button for each item
for i=1,#ITEMS do
    ITEMS[i].widgetBox = glassesTerminal.addBox2D()
    ITEMS[i].widgetBox.setSize(itemScale+(2*itemPadding), itemScale+(2*itemPadding))
    ITEMS[i].widgetBox.addTranslation((i*itemSize), itemMargin, 0)
    ITEMS[i].widgetBox.addColor(0.3,0.3,0.3, 0.8)
    ITEMS[i].widgetBox.addColor(0.3,0.3,0.3, 0.7)

    ITEMS[i].widget = glassesTerminal.addItem2D()
    ITEMS[i].widget.setItem(ITEMS[i].name, ITEMS[i].damage)
    ITEMS[i].widget.addTranslation((i*itemSize)+itemPadding, itemMargin+itemPadding, 0)
    ITEMS[i].widget.addScale(itemScale, itemScale, itemScale)

    ITEMS[i].widgetText = glassesTerminal.addText2D()
    ITEMS[i].widgetText.setText(ITEMS[i].label)
    ITEMS[i].widgetText.addTranslation((i*itemSize)+itemPadding, itemMargin+itemPadding, 0)
    ITEMS[i].widgetText.addScale(0.5, 0.5, 0.5)
end


--register event listeners and idle until user interrupts
event.listen("interact_world_block_right", setItem)
event.listen("interact_overlay", touchEvent)

print("\n# itemPlacer loaded, close with [CTRL] + [C]")
event.pull("interrupted")

event.ignore("interact_overlay", touchEvent)
event.ignore("interact_world_block_right", setItem)

glassesTerminal.removeAll()