-- this script places cubes when rightclicking a real block in the world
-- geolyzer upgrade IS REQUIRED!

event = require("event")

glassesTerminal = require("component").glasses
glassesTerminal.removeAll()

BLOCKS = {}

function checkPosition(bar)
    for index=1,#BLOCKS do
        if BLOCKS[index].x == bar.x and BLOCKS[index].y == bar.y and BLOCKS[index].z == bar.z then
            return index; end; end

    return false
end


function setBlock(EVENT, ID, USER, PLAYER_POSITION_X, PLAYER_POSITION_Y, PLAYER_POSITION_Z, PLAYER_LOOKAT_X, PLAYER_LOOKAT_Y, PLAYER_LOOKAT_Z, ROTATION, BLOCK_POSITION_X, BLOCK_POSITION_Y, BLOCK_POSITION_Z, BLOCK_SIDE)

    local foo = { x = BLOCK_POSITION_X, y = BLOCK_POSITION_Y, z = BLOCK_POSITION_Z }

    if BLOCK_SIDE == "up" then         foo.y=(foo.y + 1)
    elseif BLOCK_SIDE == "down" then   foo.y=(foo.y - 1)
    elseif BLOCK_SIDE == "north" then  foo.z=(foo.z - 1) -- no(t[ch]) logic...
    elseif BLOCK_SIDE == "south" then  foo.z=(foo.z + 1)
    elseif BLOCK_SIDE == "west" then   foo.x=(foo.x - 1)
    elseif BLOCK_SIDE == "east" then   foo.x=(foo.x + 1); end

    local i = checkPosition(foo)

    if i == false then
        foo.widget = glassesTerminal.addCube3D()
        foo.widget.addTranslation(foo.x, foo.y, foo.z)
        foo.widget.addColor(1, 1, 1, 0.3)
        table.insert(BLOCKS, foo)
    else
        BLOCKS[i].widget.removeWidget()
        table.remove(BLOCKS, i)
    end
end


event.listen("interact_world_block_right", setBlock)

event.pull("interrupted")

event.ignore("interact_world_block_right", setBlock)