package com.bymarcin.openglasses.component;

import ben_mkiv.commons0815.utils.PlayerStats;
import ben_mkiv.commons0815.utils.utilsCommon;
import ben_mkiv.rendertoolkit.common.widgets.Widget;
import ben_mkiv.rendertoolkit.common.widgets.component.face.*;
import ben_mkiv.rendertoolkit.common.widgets.component.world.*;
import ben_mkiv.rendertoolkit.common.widgets.core.attribute.IAttribute;
import ben_mkiv.rendertoolkit.network.messages.WidgetUpdatePacket;
import ben_mkiv.rendertoolkit.network.rTkNetwork;
import com.bymarcin.openglasses.OpenGlasses;
import com.bymarcin.openglasses.item.GlassesNBT;
import com.bymarcin.openglasses.item.OpenGlassesNBT.OpenGlassesHostsNBT;
import com.bymarcin.openglasses.item.OpenGlassesNBT.OpenGlassesNotificationsNBT;
import com.bymarcin.openglasses.item.upgrades.UpgradeGeolyzer;
import com.bymarcin.openglasses.item.upgrades.UpgradeNavigation;
import com.bymarcin.openglasses.lib.McJty.font.TrueTypeFont;
import com.bymarcin.openglasses.lua.AttributeRegistry;
import com.bymarcin.openglasses.lua.LuaObject;
import com.bymarcin.openglasses.lua.LuaReference;
import com.bymarcin.openglasses.network.NetworkRegistry;
import com.bymarcin.openglasses.network.packet.HostInfoPacket;
import com.bymarcin.openglasses.network.packet.TerminalStatusPacket;
import com.bymarcin.openglasses.surface.OCServerSurface;
import com.bymarcin.openglasses.utils.PlayerStatsOC;
import li.cil.oc.api.API;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.awt.*;
import java.util.*;

public class OpenGlassesHostComponent implements ManagedEnvironment {
    private UUID uuid;
    private Node node;
    private String terminalName = "";
    private boolean absoluteRenderPosition = false;
    private boolean addedToServerSurface = false;

    private WidgetServer widgets;
    private EnvironmentHost environmentHost;


    public static HashMap<EntityPlayer, UUID> linkRequests = new HashMap<>();

    public EnvironmentHost getHost(){
        return environmentHost;
    }

    public OpenGlassesHostComponent(EnvironmentHost container){
        uuid = UUID.randomUUID();
        widgets = new WidgetServer(this);
        environmentHost = container;

        setupNode();
    }

    public Vec3d getRenderPosition(){
        return new Vec3d(environmentHost.xPosition(), environmentHost.yPosition(), environmentHost.zPosition());
    }


    void setupNode(){
        if(this.node() == null || this.node().network() == null)
            node = API.network.newNode(this, Visibility.Network).withComponent(getComponentName()).withConnector().create();
    }

    public void sendInteractEventWorldBlock(String eventType, String name, Vec3d playerPos, Vec3d look, double eyeh, BlockPos pos, EnumFacing face, double playerRotation, double playerPitch){
        if(node() == null) return;
        node().sendToReachable("computer.signal", eventType.toLowerCase(), name,
                Math.round((playerPos.x - environmentHost.xPosition())*1000)/1000d,
                Math.round((playerPos.y - environmentHost.yPosition())*1000)/1000d,
                Math.round((playerPos.z - environmentHost.zPosition())*1000)/1000d,
                look.x, look.y, look.z, eyeh, pos.getX() - environmentHost.xPosition(), pos.getY() - environmentHost.yPosition(), pos.getZ() - environmentHost.zPosition(), face.getName(), playerRotation, playerPitch, EnumFacing.fromAngle(playerRotation).getName());
    }

    public void sendInteractEventWorld(String eventType, String name, Vec3d playerPos, Vec3d look, double eyeh, double playerRotation, double playerPitch){
        if(node() == null) return;
        node().sendToReachable("computer.signal", eventType.toLowerCase(), name,
                Math.round((playerPos.x - environmentHost.xPosition())*1000)/1000d,
                Math.round((playerPos.y - environmentHost.yPosition())*1000)/1000d,
                Math.round((playerPos.z - environmentHost.zPosition())*1000)/1000d,
                look.x, look.y, look.z, eyeh, playerRotation, playerPitch, EnumFacing.fromAngle(playerRotation).getName());
    }

    public void sendInteractEventOverlay(String eventType, String name, double button, double x, double y, Vec3d look, double eyeh, double playerRotation, double playerPitch){
        if(node() == null) return;
        node().sendToReachable("computer.signal", eventType.toLowerCase(), name, x, y, button, look.x, look.y, look.z, eyeh, playerRotation, playerPitch, EnumFacing.fromAngle(playerRotation).getName());
    }

    public void sendChangeSizeEvent(String eventType, String player, int width, int height, double scaleFactor){
        if(node() == null) return;
        node().sendToReachable("computer.signal", eventType.toLowerCase(), player, width, height, scaleFactor);
    }

    public Node node(){
        return node;
    }

    private void addToSurface(){
        if(!addedToServerSurface) {
            OCServerSurface.addHost(this);
            addedToServerSurface = true;
        }
    }

    private void removeFromSurface(){
        OCServerSurface.removeHost(uuid);
        addedToServerSurface = false;
    }

    @Override
    public void onConnect(Node connectingNode){
        if(connectingNode.equals(node())) {
            addToSurface();
        }
    }

    @Override
    public void onDisconnect(Node disconnectingNode){
        if(disconnectingNode.equals(node())) {
            removeFromSurface();
        }
    }

    @Override
    public void onMessage(Message nodeSender){}

    public boolean canUpdate(){ return true; }

    public String getComponentName() {
        return "glasses";
    }

    public void onGlassesPutOn(EntityPlayerMP player){
        if(node() == null) return;
        sync(player);
        node().sendToReachable("computer.signal","glasses_on", player.getDisplayNameString());
    }

    public void onGlassesPutOff(EntityPlayerMP player){
        if(node() == null) return;
        rTkNetwork.sendTo(new WidgetUpdatePacket(getUUID()), player);
        node().sendToReachable("computer.signal","glasses_off", player.getDisplayNameString());
    }

    @Callback
    public Object[] startLinking(Context context, Arguments args) {
        String playerName = args.optString(0, "");

        HashSet<String> players = new HashSet<>();

        if(playerName.length() > 0){
            EntityPlayerMP player = (EntityPlayerMP) OpenGlasses.proxy.getPlayer(playerName);
            if(requestLink(player))
                players.add(player.getDisplayName().getUnformattedText());

            return new Object[]{ players.size() > 0, players.toArray() };
        }
        else {
            for (EntityPlayerMP player : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()) {
                if (requestLink(player))
                    players.add(player.getDisplayName().getUnformattedText());
            }
        }
        
        return new Object[]{ true, players.toArray() };
    }

    private boolean requestLink(EntityPlayerMP player){
        // check if player is in range of the terminal
        if(getRenderPosition().distanceTo(player.getPositionVector()) > 64)
            return false;

        ItemStack glasses = OpenGlasses.getGlassesStack(player);
        if(glasses.isEmpty())
            return false;

        // check if glasses are already linked to the host
        if(OpenGlassesHostsNBT.getHostFromNBT(getUUID(), glasses) != null)
            return false;

        // check if linkrequest was already send
        if(OpenGlassesNotificationsNBT.getLinkRequest(glasses, getUUID()) != null)
            return false;

        // actually add the request to the glasses NBT, sync the stack to the client and notify the client about a new notification... (sounds weird... uhmmm probably is)
        OpenGlassesNotificationsNBT.addLinkRequest(glasses, getUUID());
        GlassesNBT.syncStackNBT(glasses, player);
        NetworkRegistry.packetHandler.sendTo(new TerminalStatusPacket(TerminalStatusPacket.TerminalEvent.NOTIFICATION, getUUID()), player);

        return true;
    }

    @Callback(direct = true)
    public Object[] getConnectedPlayers(Context context, Arguments args) {
        Object[] ret = new Object[OCServerSurface.instances.playerStats.size()];
        int i = 0;
        for(Map.Entry<UUID, PlayerStats> e : OCServerSurface.instances.playerStats.entrySet()){
            PlayerStatsOC s = (PlayerStatsOC) e.getValue();
            ret[i] = new Object[]{ s.name, s.uuid, s.screenWidth, s.screenHeight, s.guiScale };
            i++;
        }

        return new Object[]{ ret };
    }

    @Callback(direct = true)
    public Object[] requestResolutionEvents(Context context, Arguments args) {
        String user = args.optString(0, "").toLowerCase();
        int i=0;
        for(EntityPlayerMP player : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()){
            if(user.length() > 0 && !user.equals(player.getDisplayNameString().toLowerCase()))
                continue;

            OCServerSurface.instance().requestResolutionEvent(player, getUUID());
            i++;
        }
        return new Object[]{ i };
    }

    @Callback(direct = true)
    public Object[] setRenderResolution(Context context, Arguments args) {
        TerminalStatusPacket packet = new TerminalStatusPacket(TerminalStatusPacket.TerminalEvent.SET_RENDER_RESOLUTION, getUUID());
        String user = "";

        double x,y;

        if(args.count() == 3) {
            user = args.checkString(0).toLowerCase();
            x = args.checkDouble(1);
            y = args.checkDouble(2);
        }
        else if(args.count() == 2) {
            x = args.checkDouble(0);
            y = args.checkDouble(1);
        }
        else{
            if(args.count() < 2)
                return new Object[]{ false, "not enough arguments specified" };
            else
                return new Object[]{ false, "too many arguments specified" };
        }

        packet.x = (float) x;
        packet.y = (float) y;

        int i=0;
        for(EntityPlayerMP player : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()){
            if(user.length() > 0 && !user.equals(player.getDisplayNameString().toLowerCase()))
                continue;

            if(OCServerSurface.instance().isSubscribedTo(player.getUniqueID(), getUUID())) {
                ItemStack glassesStack = OpenGlasses.getGlassesStack(player);
                OpenGlassesHostsNBT.setRenderResolution(new Vec3d(x, y, 0), glassesStack, getUUID());

                NetworkRegistry.packetHandler.sendTo(packet, player);
            }

            i++;
        }
        return new Object[]{ i };
    }

    @Callback(direct = true)
    public Object[] getWidgetCount(Context context, Arguments args){
        return new Object[]{ widgets.size()};
    }

    @Callback(direct = true)
    public Object[] removeWidget(Context context, Arguments args){
        if(args.count() < 1 || !args.isInteger(0))
            return new Object[]{ false, "argument widget id missing" };

        int id = args.checkInteger(0);
        return new Object[]{ widgets.remove(id)};
    }

    @Callback(direct = true)
    public Object[] removeAll(Context context, Arguments args){
        widgets.clear();
        return new Object[]{ true };
    }

    /* Object manipulation */

    @Callback(direct = true)
    public Object[] addCube3D(Context context, Arguments args){
        Widget w = new Cube3D();
        return addWidget(w);
    }

    @Callback(direct = true)
    public Object[] addText3D(Context context, Arguments args){
        Widget w = new Text3D();
        return addWidget(w);
    }

    @Callback(direct = true)
    public Object[] addText2D(Context context, Arguments args){
        Widget w = new Text2D();
        return addWidget(w);
    }

    @Callback(direct = true)
    public Object[] addItem2D(Context context, Arguments args){
        Widget w = new Item2D();
        return addWidget(w);
    }

    @Callback(direct = true)
    public Object[] addItem3D(Context context, Arguments args){
        Widget w = new Item3D();
        return addWidget(w);
    }

    @Callback(direct = true)
    public Object[] addCustom2D(Context context, Arguments args){
        Widget w = new Custom2D();
        return addWidget(w);
    }

    @Callback(direct = true)
    public Object[] addOBJModel3D(Context context, Arguments args){
        Widget w = new OBJModel3D();
        return addWidget(w);
    }

    @Callback(direct = true)
    public Object[] addOBJModel2D(Context context, Arguments args){
        Widget w = new OBJModel2D();
        return addWidget(w);
    }

    @Callback(direct = true)
    public Object[] addCustom3D(Context context, Arguments args){
        Widget w = new Custom3D();
        return addWidget(w);
    }

    @Callback(direct = true)
    public Object[] addBox2D(Context context, Arguments args){
        Widget w = new Box2D();
        return addWidget(w);
    }

    @Callback(direct = false)
    public Object[] getFonts(Context context, Arguments args){
        ArrayList<String> fonts = new ArrayList<>();

        fonts.add("this method only returns fonts that are available on the server side");
        fonts.add("client fonts probably differ");

        for(Font font : TrueTypeFont.getFonts())
            fonts.add(font.getFontName());

        return new Object[]{ fonts.toArray() };
    }

    @Callback(direct = true)
    public Object[] addEntityTracker3D(Context context, Arguments args){
        Widget w = new EntityTracker3D();
        return addWidget(w);
    }

    @Callback(direct = true)
    public Object[] setTerminalName(Context context, Arguments args) {
        String newName = args.optString(0, "");
        if(!terminalName.equals(newName)) {
            terminalName = newName;
            syncHostInfo();
        }
        return new Object[]{ true };
    }

    @Callback(direct = true)
    public Object[] getTerminalName(Context context, Arguments args) {
        return new Object[]{ getName() };
    }

    @Callback(direct = true)
    public Object[] setRenderPosition(Context context, Arguments args) {
        if (args.count() > 0 && args.checkString(0).toLowerCase().equals("absolute")) {

            if(!OpenGlasses.absoluteRenderingAllowed)
                return new Object[]{ false, "absolute render position is disabled in the mod config" };

            if (!absoluteRenderPosition){
                absoluteRenderPosition = true;
                syncHostInfo();
            }
            return new Object[]{ true };
        }
        else if (args.count() > 0 && args.checkString(0).toLowerCase().equals("relative")){
            if(absoluteRenderPosition) {
                absoluteRenderPosition = false;
                syncHostInfo();
            }
            return new Object[]{true};
        }
        else
            return new Object[]{ false, "first argument has to be \"relative\" or \"absolute\"" };
    }

    @Callback(direct = true)
    public Object[] getRenderPosition(Context context, Arguments args) {
        return new Object[]{ renderAbsolute() ? "absolute" : "relative" };
    }

    @Callback(direct = true)
    public Object[] widgets(Context context, Arguments args) {

        HashMap<String, Object> widgetRefs = new HashMap<>();

        int i = 0;
        for(Map.Entry<Integer, Widget> widget : widgets.list.entrySet()){
            widgetRefs.put("widget" + i++, new LuaObject(widget.getKey(), widget.getValue(), getUUID()));
        }

        return new Object[]{ widgetRefs };
    }

    @Callback(direct = true)
    public Object[] newUniqueKey(Context context, Arguments args){
        for(EntityPlayerMP player: FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()){
            if(OCServerSurface.instance().isSubscribedTo(player.getUniqueID(), getUUID()))
                onGlassesPutOff(player);
        }
        OCServerSurface.components.remove(getUUID());
        uuid = UUID.randomUUID();
        OCServerSurface.components.put(getUUID(), this);
        return new Object[]{ getUUID() };
    }

	@Callback
	public Object[] getUserPosition(Context context, Arguments args){
		if(args.isString(0)) {
            EntityPlayerMP player = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUsername(args.checkString(0));
            if(player == null)
                return new Object[]{ false, "player not found" };
            Object position = getUserPosition(player);

            if(position == null)
                position = false;

            return new Object[] { position };
        }
        else {
            HashSet<Object> positions = new HashSet<>();
            for (EntityPlayerMP player : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()) {
                Object position = getUserPosition(player);
                if(position instanceof HashMap) {
                    ((HashMap<String, Object>) position).put("user", player.getDisplayNameString());
                    positions.add(position);
                }
            }

            return new Object[]{positions};
        }
	}

	@Callback
	public Object[] getUserLookingAt(Context context, Arguments args){
		if(!args.isString(0))
		    return new Object[]{ false, "first argument has to be a username"};

        EntityPlayerMP player = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUsername(args.checkString(0));
        if(player == null)
            return new Object[]{ false, "player not found" };

        Object lookingAt = getUserLookingAt(player);

        if(lookingAt == null)
            lookingAt = false;

        return new Object[]{ lookingAt };
	}

    public String getName(){
        return terminalName;
    }

    public boolean renderAbsolute(){
        return OpenGlasses.absoluteRenderingAllowed && absoluteRenderPosition;
    }

    public UUID getUUID(){
        return uuid;
    }

    public Object[] addWidget(Widget widget){
        int widgetId = widgets.add(widget);
        return new Object[] { new LuaObject(widgetId, widget, getUUID()).call(null, null)[0] };
    }

    public boolean removeWidget(int id){
        return widgets.remove(id);
    }

    public void updateWidget(int id){
        widgets.update(id);
    }

    public Widget getWidget(int id){
        return widgets.get(id);
    }

    @Override
    public void save(NBTTagCompound nbt) {
        setupNode();

        nbt = widgets.writeToNBT(nbt);
        nbt.setString("name", terminalName);
        nbt.setUniqueId("uuid", uuid);
        nbt.setBoolean("absoluteRenderPosition", absoluteRenderPosition);

        if(node() != null) {
            NBTTagCompound nodeTag = new NBTTagCompound();
            node().save(nodeTag);
            nbt.setTag("oc:node", nodeTag);
        }
    }

    @Override
    public void load(NBTTagCompound nbt) {
        widgets.readFromNBT(nbt);
        terminalName = nbt.getString("name");
        absoluteRenderPosition = OpenGlasses.absoluteRenderingAllowed && nbt.getBoolean("absoluteRenderPosition");
        if(nbt.hasUniqueId("uuid")) //check required as we would end up with 0000000... uuid for new items
            uuid = nbt.getUniqueId("uuid");

        setupNode();
        if(node() != null && nbt.hasKey("oc:node"))
            node().load(nbt.getCompoundTag("oc:node"));
    }

    @Override
    public void update() {
        addToSurface();
    }

    public void remove() {
        widgets.clear();

        if (getHost() != null && !getHost().world().isRemote) {
            for (Map.Entry<UUID, HashSet<UUID>> entry : OCServerSurface.instance().players.entrySet())
                if (entry.getValue().contains(getUUID())) {
                    EntityPlayer player = OpenGlasses.proxy.getPlayer(entry.getKey());
                    if (player instanceof EntityPlayerMP)
                        syncWidgets((EntityPlayerMP) player);
                }

            //OCServerSurface.removeHost(getUUID());
            addedToServerSurface = false;

        }

        if (node() != null)
            node().remove();
    }

    public HashSet<EntityPlayerMP> getConnectedPlayers(){
        HashSet<EntityPlayerMP> players = new HashSet<>();
        for(UUID playerUUID : getPlayers()){
            EntityPlayerMP player = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(playerUUID);
            if(player != null)
               players.add(player);
        }

        return players;
    }

    public HashSet<UUID> getPlayers(){
        HashSet<UUID> players = new HashSet<>();
        for(UUID player : OCServerSurface.instances.players.keySet()){
            if(OCServerSurface.instance().isSubscribedTo(player, getUUID()))
                players.add(player);
        }

        return players;
    }

    public static HashMap<String, Object> vec3D_to_map(Vec3d vector){
        HashMap<String, Object> result = new HashMap<>();
        result.put("x", vector.x);
        result.put("y", vector.y);
        result.put("z", vector.z);
        return result;
    }

    private Object getUserPosition(EntityPlayer player){
        ItemStack glassesStack = OpenGlasses.getGlassesStack(player);

        if (glassesStack.isEmpty())
            return null;

        if (!UpgradeNavigation.hasUpgrade(glassesStack))
            return new Object[]{false, "glasses haven't navigation upgrade installed"};

        if(renderAbsolute())
            return vec3D_to_map(player.getPositionVector());
        else {
            Vec3d hostPosition = getRenderPosition();
            return vec3D_to_map(player.getPositionVector().subtract(hostPosition));
        }
    }

    private Object getUserLookingAt(EntityPlayerMP player){
        ItemStack glassesStack = OpenGlasses.getGlassesStack(player);

        if (glassesStack.isEmpty())
            return null;

        if (!UpgradeNavigation.hasUpgrade(glassesStack))
            return new Object[]{false, "glasses haven't navigation upgrade installed"};

        double range = UpgradeGeolyzer.getRange(glassesStack);

        if (range == 0)
            return new Object[]{false, "glasses haven't geolyzer upgrade installed"};

        RayTraceResult result = utilsCommon.rayTrace(player, range, 0);

        HashMap<String, Object> hitInfo = new HashMap<>();
        Vec3d pos;
        if(result != null && !result.typeOfHit.equals(RayTraceResult.Type.MISS)) {
            //todo: figure out entity raytracing on server side, probably did that already in OpenEntity
            /*if (result.entityHit != null) {
                pos = result.entityHit.getPositionVector();

                if (!renderAbsolute())
                    pos = pos.subtract(environmentHost.getRenderPosition());

                hitInfo.put("type", "entity");
                hitInfo.putAll(vec3D_to_map(pos));
                hitInfo.put("name", result.entityHit.getDisplayName().toString());
                return hitInfo;
            }
            else*/
            if(result.getBlockPos() != null){
                pos = new Vec3d(result.getBlockPos());

                if(!renderAbsolute())
                    pos = pos.subtract(getRenderPosition());

                IBlockState blockState = player.getEntityWorld().getBlockState(result.getBlockPos());

                hitInfo.put("type", "block");
                hitInfo.putAll(vec3D_to_map(pos));
                hitInfo.put("name", blockState.getBlock().getRegistryName().toString());
                hitInfo.put("meta", blockState.getBlock().getMetaFromState(blockState));
                return hitInfo;
            }
        }
        hitInfo.put("type", "air");
        return hitInfo;
    }

    public void sync(EntityPlayerMP player){
        syncHostInfo(player);
        syncWidgets(player);
    }

    private void syncHostInfo(){
        for(EntityPlayerMP player : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers())
            syncHostInfo(player);
    }

    public void syncHostInfo(EntityPlayerMP player){
        if(player != null)
            NetworkRegistry.packetHandler.sendTo(new HostInfoPacket(this), player);
    }

    public void syncWidgets(EntityPlayerMP player){
        OCServerSurface.instances.sendSync(getUUID(), player, widgets.list);
    }

    public void syncWidgets(){
        for(EntityPlayerMP player : getConnectedPlayers())
            OCServerSurface.instances.sendSync(getUUID(), player, widgets.list);
    }

}
