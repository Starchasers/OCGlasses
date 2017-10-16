-- this script places items when rightclicking a real block in the world
-- geolyzer upgrade IS REQUIRED!
-- place a chest next to a transposer and put some items as reference in

require("openglasses/itemPlacer")
itemMargin = 4
itemScale = 32
itemPadding = 2

function getItemsFromTransposer(transposer)
    print("checking inventorys at transposer " .. transposer.address)
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
end

function getItemsFromDatabase(database)
    print("checking database")

    --determine database size
    if pcall(function() database.get(81); end) then
        size = 81
    elseif pcall(function() database.get(25); end) then
        size = 25
    elseif pcall(function() database.get(9); end) then
        size = 9
    else
        print("what the heck of a database is this? computer says NO! not going to scan it -.-")
        return
    end

    if size ~= nil and size > 0 then
        io.write("database found (size: " .. size .. ") scanning: ")

        for slot=1,size do
           io.write(".")
           local item = database.get(slot)
           if item ~= nil and checkItem(item) == false then
              table.insert(ITEMS, item); end
        end

        print(" done!")
    end
end

-- read items from transposers
for address,type in pairs(component.list("transposer")) do
    getItemsFromTransposer(component.proxy(address)); end

-- read items from databases
for address,type in pairs(component.list("database")) do
    getItemsFromDatabase(component.proxy(address)); end



itemSize = (itemScale + 2*(itemPadding+itemMargin))

-- create overlay button for each item
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
end

selectedItemWidget.addTranslation(itemMargin, itemSize, 0)
selectedItemWidget.setText("select an item to place into the world")

-- load widgets from config file
loadWidgets()

--register event listeners and idle until user interrupts
event.listen("interact_world_block_right", setItem)
event.listen("interact_overlay", touchEvent)

print("\n# itemPlacer loaded, close with [CTRL] + [C]")
event.pull("interrupted")

event.ignore("interact_overlay", touchEvent)
event.ignore("interact_world_block_right", setItem)

-- remove all widgets from glasses
glassesTerminal.removeAll()

-- save widgets to config file
saveWidgets();