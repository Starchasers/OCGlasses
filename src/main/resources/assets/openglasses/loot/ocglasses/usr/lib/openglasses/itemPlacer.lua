component = require("component")
event = require("event")
sides = require("sides")

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
        ITEMS[selectedItem].widgetBox.updateModifier(2, 0.3, 0.3, 0.3, 0.8)
        ITEMS[selectedItem].widgetBox.updateModifier(3, 0.3, 0.3, 0.3, 0.7)
        selectedItem = false
        selectedItemWidget.setText("no item selected")
    end
end

function selectItem(index)
    deselectItem()
    ITEMS[index].widgetBox.updateModifier(2, 0.3, 0.8, 0.3, 0.8)
    ITEMS[index].widgetBox.updateModifier(3, 0.3, 0.8, 0.3, 0.7)

    selectedItem = index
    selectedItemWidget.setText("using item: " .. ITEMS[index].label)
end


itemMargin = 4
itemScale = 32
itemPadding = 2

function setItem(EVENT, ID, USER, PLAYER_POSITION_X, PLAYER_POSITION_Y, PLAYER_POSITION_Z, PLAYER_LOOKAT_X, PLAYER_LOOKAT_Y, PLAYER_LOOKAT_Z, ROTATION, BLOCK_POSITION_X, BLOCK_POSITION_Y, BLOCK_POSITION_Z, BLOCK_SIDE)
    if selectedItem == false then
        return false
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
        foo.widget = glassesTerminal.addItem3D()
        foo.widget.setItem(ITEMS[selectedItem].name, ITEMS[selectedItem].damage)
        foo.widget.addTranslation(foo.x, foo.y, foo.z)
        foo.widget.addColor(1, 1, 1, 0.3)
        table.insert(BLOCKS, foo)
    else
        BLOCKS[i].widget.removeWidget()
        table.remove(BLOCKS, i)
    end
end

function touchEvent(EVENT, ID, USER, X, Y, BUTTON)
    for i=1,#ITEMS do
        if Y <= ( itemScale + (2*itemMargin) + (2*itemPadding)) then
            xItem = ( i * (itemScale + (2*itemMargin) + (2*itemPadding)) )
            if X < (xItem+itemScale+(2*itemMargin)+(2*itemPadding)) and X >= xItem then
                selectItem(i); end; end; end
end