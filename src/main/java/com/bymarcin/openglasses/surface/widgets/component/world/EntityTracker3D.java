package com.bymarcin.openglasses.surface.widgets.component.world;

import com.bymarcin.openglasses.surface.*;
import com.bymarcin.openglasses.surface.widgets.core.attribute.ITracker;
import com.bymarcin.openglasses.utils.Location;
import io.netty.buffer.ByteBuf;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.UUID;


public class EntityTracker3D extends OBJModel3D implements ITracker {
    public enum EntityType{ NONE, ALL, ITEM, LIVING, PLAYER, HOSTILE, NEUTRAL, UNIQUE }
    static ArrayList<WidgetModifier.WidgetModifierType> applyModifiersList;

    public int maximumRange = 128;
    public int trackingRange = 0;
    public EntityType trackingType;
    public String trackingEntityName = "";
    public int trackingEntityMetaIndex = 0;
    public UUID uniqueEntityID = null;

    public EntityTracker3D(){
        super();

        applyModifiersList = new ArrayList();
        applyModifiersList.add(WidgetModifier.WidgetModifierType.ROTATE);
        applyModifiersList.add(WidgetModifier.WidgetModifierType.TRANSLATE);

        this.setupTracking(EntityType.ALL, 0);
    }

    @Override
    public void writeData(ByteBuf buff) {
        super.writeData(buff);
        buff.writeInt(trackingRange);
        buff.writeInt(trackingType.ordinal());

        ByteBufUtils.writeUTF8String(buff, trackingEntityName);
        buff.writeInt(trackingEntityMetaIndex);

        if(uniqueEntityID == null)
            ByteBufUtils.writeUTF8String(buff, "none");
        else
            ByteBufUtils.writeUTF8String(buff, uniqueEntityID.toString());
    }

    @Override
    public void readData(ByteBuf buff) {
        super.readData(buff);
        trackingRange = Math.min(ClientSurface.instances.glassesStack.getTagCompound().getInteger("radarRange"), buff.readInt());
        setupTracking(buff.readInt(), trackingRange);
        setupTrackingFilter(ByteBufUtils.readUTF8String(buff), buff.readInt());

        String uuid = ByteBufUtils.readUTF8String(buff);

        if(!uuid.equals("none"))
            setupTrackingEntity(UUID.fromString(uuid));
        else
            setupTrackingEntity(null);
    }

    public void setupTracking(int tT, int range) {
        setupTracking(EntityType.values()[tT], range);
    }

    public void setupTracking(EntityType tT, int range){
        this.trackingType = tT;
        this.trackingRange = Math.min(range, maximumRange);
    }

    public void setupTrackingFilter(String type, int metaindex){
        this.trackingEntityName = type.toLowerCase();
        this.trackingEntityMetaIndex = metaindex;
    }

    public void setupTrackingEntity(UUID uuid){
        this.uniqueEntityID = uuid;
    }


    public ArrayList getAllEntities(Vec3d origin, int distance, World world, Class<? extends Entity> eClass, AxisAlignedBB bounds){
        ArrayList entities = new ArrayList();
        for(Entity entity : world.getEntitiesWithinAABB(eClass, bounds)) {
            if(checkDistance(origin, new Vec3d(entity.posX, entity.posY, entity.posZ), distance))
                entities.add(entity);
        }
        return entities;
    }

    @Override
    public WidgetType getType() {
        return WidgetType.ENTITYTRACKER3D;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IRenderableWidget getRenderable() {
        return new RenderEntityTracker();
    }

    @SideOnly(Side.CLIENT)
    class RenderEntityTracker extends RenderableOBJModel{
        @Override
        public void render(EntityPlayer player, Location glassesTerminalLocation, long conditionStates) {
            if(objFile == null) return;
            if(!ClientSurface.instances.glassesStack.getTagCompound().getBoolean("motionsensor")) return; //require Motionsensor Update
            if(!ClientSurface.instances.glassesStack.getTagCompound().getBoolean("geolyzer")) return;    //require Geolyzer Update



            if(trackingType.equals(EntityType.NONE)) return;

            Entity focusedEntity;
            
            RayTraceResult objectMouseOver = Minecraft.getMinecraft().objectMouseOver;
            if(objectMouseOver != null && objectMouseOver.typeOfHit.equals(RayTraceResult.Type.ENTITY)){
                focusedEntity = objectMouseOver.entityHit;
            }
            else
                focusedEntity = null;

            Vec3d playerPos = player.getPositionVector();
            AxisAlignedBB bounds = new AxisAlignedBB(playerPos.x - trackingRange, playerPos.y - trackingRange, playerPos.z - trackingRange,
                                                     playerPos.x + trackingRange, playerPos.y + trackingRange, playerPos.z + trackingRange);

            GL11.glTranslated(-ClientSurface.instances.lastBind.x, -ClientSurface.instances.lastBind.y, -ClientSurface.instances.lastBind.z);

            this.preRender(conditionStates);

            TESR = Tessellator.getInstance();
            buffer = TESR.getBuffer();

            switch(trackingType) {
                case ALL:
                case ITEM:
                    for (EntityItem e : (ArrayList<EntityItem>) getAllEntities(player.getPositionVector(), trackingRange, player.world, EntityItem.class, bounds)) {
                        if (!checkRender(e)) continue;
                        renderTarget(e.getPositionVector(), player, customRenderConditions(conditionStates, e, focusedEntity));
            }   }

            switch(trackingType) {
                case ALL:
                case PLAYER:
                case LIVING:
                    for (EntityPlayer e : (ArrayList<EntityPlayer>) getAllEntities(player.getPositionVector(), trackingRange, player.world, EntityPlayer.class, bounds)) {
                        if (!checkRender(e)) continue;
                        if (e == player) continue;
                        renderTarget(e.getPositionVector(), player, customRenderConditions(conditionStates, e, focusedEntity));
            }   }

            switch(trackingType) {
                case ALL:
                case LIVING:
                case NEUTRAL:
                case HOSTILE:
                    for (EntityLiving e : (ArrayList<EntityLiving>) getAllEntities(player.getPositionVector(), trackingRange, player.world, EntityLiving.class, bounds)) {
                        if(!checkRender(e)) continue;
                        renderTarget(e.getPositionVector(), player, customRenderConditions(conditionStates, e, focusedEntity));
            }   }

            this.postRender();
            GL11.glTranslated(ClientSurface.instances.lastBind.x, ClientSurface.instances.lastBind.y, ClientSurface.instances.lastBind.z);
        }

        public long customRenderConditions(long customConditions, Entity e, Entity focusedEntity){
            if(focusedEntity != null && focusedEntity.getPersistentID().equals(e.getPersistentID()))
                customConditions |= ((long) 1 << WidgetModifierConditionType.IS_FOCUSED_ENTITY);

            if(e instanceof EntityMob)
                customConditions |= ((long) 1 << WidgetModifierConditionType.IS_HOSTILE);

            if(!(e instanceof EntityMob) && (e instanceof EntityLiving))
                customConditions |= ((long) 1 << WidgetModifierConditionType.IS_NEUTRAL);

            if(e instanceof EntityLiving)
                customConditions |= ((long) 1 << WidgetModifierConditionType.IS_LIVING);

            if(e instanceof EntityItem)
                customConditions |= ((long) 1 << WidgetModifierConditionType.IS_ITEM);

            if(e instanceof EntityPlayer)
                customConditions |= ((long) 1 << WidgetModifierConditionType.IS_PLAYER);

            return customConditions;
        }


        public boolean checkRender(Entity e){
            switch(trackingType) {
                case ALL:
                    return true;

                case UNIQUE:
                    if(e.getUniqueID().equals(uniqueEntityID))
                        return true;

                    return false;

                case HOSTILE:
                    if(!(e instanceof EntityMob))
                        return false;

                    if(trackingEntityName.length() == 0)
                        return true;

                    if(!e.getName().toLowerCase().equals(trackingEntityName))
                        return false;

                    return true;

                case LIVING:
                    if(!(e instanceof EntityLiving))
                        return false;

                    if(trackingEntityName.length() == 0)
                        return true;

                    if(!e.getName().toLowerCase().equals(trackingEntityName))
                        return false;

                    return true;

                case NEUTRAL:
                    if(!(e instanceof EntityLiving))
                        return false;

                    if(e instanceof EntityMob)
                        return false;

                    if(trackingEntityName.length() == 0)
                        return true;

                    if(!e.getName().toLowerCase().equals(trackingEntityName))
                        return false;

                    return true;

                case ITEM:
                    if(!(e instanceof EntityItem))
                        return false;

                    if(trackingEntityName.length() == 0)
                        return true;

                    if(trackingEntityMetaIndex != -1 && ((EntityItem) e).getItem().getMetadata() != trackingEntityMetaIndex)
                        return false;

                    if(!((EntityItem) e).getItem().getItem().getRegistryName().toString().toLowerCase().equals(trackingEntityName))
                        return false;

                    return true;

                default:
                    return false;
            }
        }

        public void renderTarget(Vec3d pos, EntityPlayer player, long conditionStates) {
            GL11.glTranslated(pos.x, pos.y, pos.z);

            this.applyModifierList(conditionStates, applyModifiersList);
            this.addPlayerRotation(player, pos);

            int color = WidgetModifierList.getCurrentColor(conditionStates, 0);

            if(objFile.facesTri.size() > 0) {
                buffer.begin(GL11.GL_TRIANGLES, malisisVertexFormat);
                renderList(objFile.facesTri, color);
                TESR.draw();
            }

            if(objFile.facesQuad.size() > 0) {
                buffer.begin(GL11.GL_QUADS, malisisVertexFormat);
                renderList(objFile.facesQuad, color);
                TESR.draw();
            }
            this.removePlayerRotation(player, pos);
            this.revokeModifierList(applyModifiersList);

            GL11.glTranslated(-pos.x, -pos.y, -pos.z);
        }
    }

    public static boolean checkDistance(Vec3d src, Vec3d target, int range){
        double dx = target.x - src.x;
        double dy = target.y - src.y;
        double dz = target.z - src.z;
        if(Math.sqrt(dx * dx + dy * dy + dz * dz) <= range) return true;

        return false;
    }

}
