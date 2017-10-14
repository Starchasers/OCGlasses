package com.bymarcin.openglasses.surface;

import com.bymarcin.openglasses.OpenGlasses;
import com.bymarcin.openglasses.item.OpenGlassesItem;
import com.bymarcin.openglasses.surface.widgets.core.attribute.IEasing;
import com.bymarcin.openglasses.surface.widgets.core.modifiers.*;
import io.netty.buffer.ByteBuf;

import net.minecraft.entity.player.EntityPlayer;
import java.util.ArrayList;

import com.bymarcin.openglasses.utils.OGUtils;

import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class WidgetModifiers {
	public ArrayList<WidgetModifier> modifiers = new ArrayList<WidgetModifier>();
	public long lastConditionStates;
	private Vec3d lastOffset = new Vec3d(0, 0, 0);

	public WidgetModifiers(){}

	public void setCondition(int modifierIndex, short conditionIndex, boolean state){
		this.modifiers.get(modifierIndex).configureCondition(conditionIndex, state);
	}

	public void addEasing(int modifierIndex, String type, String typeIO, float duration, String list, float min, float max, String mode){
		if(!(this.modifiers.get(modifierIndex) instanceof IEasing)) return;

		((IEasing) this.modifiers.get(modifierIndex)).addEasing(type, typeIO, duration, list, min, max, mode);
	}

	public void removeEasing(int modifierIndex, String list){
		if(!(this.modifiers.get(modifierIndex) instanceof IEasing)) return;

		((IEasing) this.modifiers.get(modifierIndex)).removeEasing(list);
	}

	public void update(int modifierIndex, float[] args){
		this.modifiers.get(modifierIndex).update(args);
	}

	public int addTranslate(float x, float y, float z){
		this.modifiers.add(new WidgetModifierTranslate(x, y, z));
		return this.modifiers.size();
	}
		
	public int addScale(float x, float y, float z){
		this.modifiers.add(new WidgetModifierScale(x, y, z));
		return this.modifiers.size();
	}
		
	public int addRotate(float deg, float x, float y, float z){
		this.modifiers.add(new WidgetModifierRotate(deg, x, y, z));
		return this.modifiers.size();
	}

	public int addColor(float r, float g, float b, float alpha){
		this.modifiers.add(new WidgetModifierColor(r, g, b, alpha));
		return this.modifiers.size();
	}
	
	public int addTexture(String texloc) {
		this.modifiers.add(new WidgetModifierTexture(texloc));
		return this.modifiers.size();
	}

	public void revoke(long conditionsstates){
		int i = modifiers.size();
		while(i > 0) {
			i--;
			modifiers.get(i).revoke(conditionsstates);
		}
	}

	public void revoke(long conditionsstates, ArrayList<WidgetModifier.WidgetModifierType> modifierTypes){
		int i = modifiers.size();
		WidgetModifier e;
		while(i > 0) {
			i--;
			e = modifiers.get(i);
			for(WidgetModifier.WidgetModifierType x : modifierTypes) if(e.getType().equals(x))
				e.revoke(conditionsstates);
		}
	}

	public void remove(int element){
		this.modifiers.remove(element);		
	}
		
	public WidgetModifier.WidgetModifierType getType(int element){
		return this.modifiers.get(element).getType();	
	}
	
	public int getCurrentColor(long conditionStates, int index){
		float[] col = getCurrentColorFloat(conditionStates, index);
		return OGUtils.getIntFromColor(col[0], col[1], col[2], col[3]);
	}

	public float[] getCurrentColorFloat(int index){
		return getCurrentColorFloat(this.lastConditionStates, index);
	}

	public float[] getCurrentColorFloat(long conditionStates, int index){
		for(int i=this.modifiers.size() - 1; i >= 0; i--){
			if(this.modifiers.get(i).getType() == WidgetModifier.WidgetModifierType.COLOR &&
				this.modifiers.get(i).shouldApplyModifier(conditionStates) == true){					
				if(index > 0){
					index--;
				}
				else {
					Object[] color = this.modifiers.get(i).getValues();
					return new float[]{ (float) color[0], (float) color[1], (float) color[2], (float) color[3] };
				}
			}
		}
		return new float[]{ 1, 1, 1, 1 };
	}

	public float[] getCurrentScaleFloat(long conditionStates){
		float scaleX = 1, scaleY = 1, scaleZ = 1;
		for(int i=0; i < this.modifiers.size(); i++){
			if(this.modifiers.get(i).getType() == WidgetModifier.WidgetModifierType.SCALE && this.modifiers.get(i).shouldApplyModifier(conditionStates) == true){
				Object[] scale = this.modifiers.get(i).getValues();
				scaleX *= (float) scale[0];
				scaleY *= (float) scale[1];
				scaleZ *= (float) scale[2];
			}
		}
		return new float[]{ scaleX, scaleY, scaleZ };
	}

	public void apply(long conditionStates){
		this.lastConditionStates = conditionStates;
		for(int i=0, count = this.modifiers.size(); i < count; i++) 
			this.modifiers.get(i).apply(conditionStates);
	}

	public Vec3d getRenderPosition(long conditionStates, Vec3d offset){
		Vector4f renderPosition = this.generateGlMatrix(conditionStates);
		this.lastOffset = offset;
		renderPosition.x += offset.x;
		renderPosition.y += offset.y;
		renderPosition.z += offset.z;

		return new Vec3d(renderPosition.x, renderPosition.y, renderPosition.z);
	}

	@SideOnly(Side.SERVER)
	public Vec3d getRenderPosition(String ForPlayerName){
		EntityPlayer player = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUsername(ForPlayerName);

		long conditions = ((OpenGlassesItem) OpenGlasses.getGlasses(player)).getConditionStates(OpenGlasses.getGlassesStack(player), player);
		return this.getRenderPosition(conditions, this.lastOffset);
	}



	public Vector4f generateGlMatrix(long conditionStates){
		Matrix4f m = new Matrix4f();
		Object[] b;
		for(int i=0, count = this.modifiers.size(); i < count; i++)
			if(this.modifiers.get(i).shouldApplyModifier(conditionStates)) switch(this.modifiers.get(i).getType()){
				case TRANSLATE:
					b = this.modifiers.get(i).getValues();
					m.translate(new Vector3f((float) b[0], (float) b[1], (float) b[2])); 
					break;
				case SCALE:
					b = this.modifiers.get(i).getValues();
					m.scale(new Vector3f((float) b[0], (float) b[1], (float) b[2])); 
					break;
				case ROTATE:
					b = this.modifiers.get(i).getValues();
					m.rotate((float) b[0], new Vector3f((float) b[1], (float) b[2], (float) b[3])); 					
					break;
		}
		
		return m.transform(m, new Vector4f(0F, 0F, 0F, 1F), null);
	}
		
	public void writeData(ByteBuf buff){
		int modifierCount = this.modifiers.size();
		buff.writeInt(modifierCount);
		for(int i=0; i < modifierCount; i++) {
			buff.writeShort(this.modifiers.get(i).getType().ordinal());
			this.modifiers.get(i).writeData(buff);
		}
	}
		
	public void readData(ByteBuf buff){
		ArrayList<WidgetModifier> modifiersNew = new ArrayList<WidgetModifier>();
		for(int i = 0, modifierCount = buff.readInt(); i < modifierCount; i++){
			switch(WidgetModifier.WidgetModifierType.values()[buff.readShort()]){
				case TRANSLATE: modifiersNew.add(new WidgetModifierTranslate(0F, 0F, 0F)); break;
				case COLOR: modifiersNew.add(new WidgetModifierColor(0F, 0F, 0F, 0F)); break;
				case SCALE: modifiersNew.add(new WidgetModifierScale(0F, 0F, 0F)); break;
				case ROTATE: modifiersNew.add(new WidgetModifierRotate(0F, 0F, 0F, 0F)); break;
				case TEXTURE: modifiersNew.add(new WidgetModifierTexture(null)); break;
				default: modifiersNew.remove(i); return; //remove modifier if we get bs
			}
			modifiersNew.get(i).readData(buff);
		}
		
		this.modifiers = modifiersNew;
	}		
}
