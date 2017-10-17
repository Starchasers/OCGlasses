component = require("component")
event = require("event")
sides = require("sides")
serialization = require("serialization")

glassesTerminal = component.glasses
glassesTerminal.removeAll()


BLOCKS = {}

function checkPosition(bar)
    for index=1,#BLOCKS do
        if BLOCKS[index].x == bar.x and BLOCKS[index].y == bar.y and BLOCKS[index].z == bar.z then
            return index; end; end

    return false
end


ITEMS = {}

function checkItem(item)
    for index=1,#ITEMS do
        if ITEMS[index].name == item.name and ITEMS[index].damage == item.damage then
            return index; end; end

    return false
end

selectedItem = false
selectedItemWidget = glassesTerminal.addText2D()

function deselectItem()
    if selectedItem ~= false then
        local index = selectedItem
        ITEMS[index].widgetBox.updateModifier(2, 0.3, 0.3, 0.3, 0.8)
        ITEMS[index].widgetBox.updateModifier(3, 0.3, 0.3, 0.3, 0.7)
        selectedItem = false
        selectedItemWidget.setText("no item selected")
        return index
    end
end

function selectItem(index)
    if deselectItem() == index then
        return; end

    ITEMS[index].widgetBox.updateModifier(2, 0.3, 0.8, 0.3, 0.8)
    ITEMS[index].widgetBox.updateModifier(3, 0.3, 0.8, 0.3, 0.7)

    selectedItem = index
    selectedItemWidget.setText("using item: " .. ITEMS[index].label)
end


itemMargin = 4
itemScale = 32
itemPadding = 2
itemWorldScale = 0.7
itemWorldTextScale = 0.05

function placeItem(foo)
    foo.widgetText = glassesTerminal.addText3D()
    foo.widgetText.setText(foo.label)
    foo.widgetText.addTranslation(foo.x, foo.y + 1, foo.z)
    foo.widgetText.addTranslation(0.5, 0, 0.5)
    foo.widgetText.addColor(1, 1, 1, 0.8)
    foo.widgetText.addScale(itemWorldTextScale, itemWorldTextScale, itemWorldTextScale)

    foo.widget = glassesTerminal.addItem3D()
    foo.widget.setItem(foo.name, foo.damage)
    foo.widget.addTranslation(foo.x, foo.y, foo.z)
    foo.widget.addColor(1, 1, 1, 0.8)

    local rotateWidget = false
    local m = (itemWorldScale/2)

    foo.widget.addTranslation(0.5, 0.5, 0.5)

    if rotateWidget == true then
        foo.widget.setEasing(foo.widget.addRotation(0, 0, 1, 0), "LINEAR", "INOUT", 3000, "deg", 0, 359.99, "REPEAT")
    else
        foo.widget.setFaceWidgetToPlayer(true)
    end

    foo.widget.addTranslation(-m, -m, -m)

    foo.widget.addScale(itemWorldScale, itemWorldScale, itemWorldScale)

    return foo
end

function setItem(EVENT, ID, USER, PLAYER_POSITION_X, PLAYER_POSITION_Y, PLAYER_POSITION_Z, PLAYER_LOOKAT_X, PLAYER_LOOKAT_Y, PLAYER_LOOKAT_Z, ROTATION, BLOCK_POSITION_X, BLOCK_POSITION_Y, BLOCK_POSITION_Z, BLOCK_SIDE)
    if selectedItem == false then
        return
    end

    local foo = { x = BLOCK_POSITION_X, y = BLOCK_POSITION_Y, z = BLOCK_POSITION_Z }

    if BLOCK_SIDE == "up" then         foo.y=(foo.y + 1)
    elseif BLOCK_SIDE == "down" then   foo.y=(foo.y - 1)
    elseif BLOCK_SIDE == "north" then  foo.z=(foo.z - 1) -- no(t[ch]) logic...
    elseif BLOCK_SIDE == "south" then  foo.z=(foo.z + 1)
    elseif BLOCK_SIDE == "west" then   foo.x=(foo.x - 1)
    elseif BLOCK_SIDE == "east" then   foo.x=(foo.x + 1); end

    local i = checkPosition(foo)

    if i == false then
        foo.name = ITEMS[selectedItem].name
        foo.label = ITEMS[selectedItem].label
        foo.damage = ITEMS[selectedItem].damage
        table.insert(BLOCKS, placeItem(foo))
    else
        BLOCKS[i].widget.removeWidget()
        BLOCKS[i].widgetText.removeWidget()
        table.remove(BLOCKS, i)
    end
end

-- load blocks from config file
function loadWidgets()
    io.write("reading config...")
    local fh = io.open("/home/itemPlacer.cfg", "r")
    if fh ~= nil then
        BLOCKS = serialization.unserialize(fh:read())
        fh:close()
        for i=1,#BLOCKS do placeItem(BLOCKS[i]); end
        print(" done!")
    else
        print(" no config found!")
    end
end

-- save blocks to config file
function saveWidgets()
    io.write("saving data...")
    for i=1,#BLOCKS do
        BLOCKS[i].widget = nil
        BLOCKS[i].widgetText = nil
    end

    local fh = io.open("/home/itemPlacer.cfg", "w")
    fh:write(serialization.serialize(BLOCKS))
    fh:close()
    print(" done!")
end

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
    local size = 0

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

USERS = {}

function touchEvent(EVENT, ID, USER, X, Y, BUTTON)
    local index = addUser(USER)
    local itemSize = ( itemScale + (2*itemMargin) + (2*itemPadding))
    for i=1,#ITEMS do
        if Y <= itemSize then
            local xItem = ITEMS[i].x
            xItem = (xItem - (USERS[index].w/2))
            if X < (xItem+itemSize) and X >= xItem then
                selectItem(i); end; end; end
end

function getUser(username)
    for i=1,#USERS do
        if USERS[i].name == username then
            return i; end; end

    return false;
end

function addUser(username)
    local index = getUser(username)
    if index ~= false then
        return index; end

    local foo = {}
    foo.name = username

    table.insert(USERS, foo)

    return #USERS
end


function updateClientResolution(EVENT, ID, USER, WIDTH, HEIGHT, GUI_SCALE)
    local index = addUser(USER)
    USERS[index].w = WIDTH
    USERS[index].h = HEIGHT
    USERS[index].scale = GUI_SCALE
end



