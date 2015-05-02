package com.bymarcin.openglasses.vbo.computer;

import com.bymarcin.openglasses.utils.OGUtils;
import com.bymarcin.openglasses.vbo.model.Model;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;

public class ModelBuilder extends BasicModelObject {

	public ModelBuilder(String id) {
		model = new Model(id);
	}

	public ModelBuilder() {

	}

	@Callback(direct = true)
	public Object[] popMatrix(Context context, Arguments arguments) throws Exception {
		if (arguments.count() == 0) {
			if (!model.isFinalize()) {
				model.popMatrix();
			} else {
				throw new Exception(OGUtils.getLocalization("og.object.error.isfinalize"));
			}
		}
		return null;
	}

	@Callback(direct = true)
	public Object[] pushMatrix(Context context, Arguments arguments) throws Exception {
		if (arguments.count() == 0) {
			if (!model.isFinalize()) {
				model.pushMatrix();
			} else {
				throw new Exception(OGUtils.getLocalization("og.object.error.isfinalize"));
			}
		}
		return null;
	}

	@Callback(direct = true)
	public Object[] rotate(Context context, Arguments arguments) throws Exception {
		if (arguments.count() == 4) {
			float angle = (float) arguments.checkDouble(0);
			int x = arguments.checkInteger(1);
			int y = arguments.checkInteger(2);
			int z = arguments.checkInteger(3);
			if (!model.isFinalize()) {
				model.rotate(angle, x, y, z);
			} else {
				throw new Exception(OGUtils.getLocalization("og.object.error.isfinalize"));
			}
		}
		return null;
	}

	@Callback(direct = true)
	public Object[] translate(Context context, Arguments arguments) throws Exception {
		if (arguments.count() == 3) {
			float x = (float) arguments.checkDouble(0);
			float y = (float) arguments.checkDouble(1);
			float z = (float) arguments.checkDouble(2);
			if (!model.isFinalize()) {
				model.translate(x, y, z);
			} else {
				throw new Exception(OGUtils.getLocalization("og.object.error.isfinalize"));
			}
		}
		return null;
	}

	@Callback(direct = true)
	public Object[] addVertex(Context context, Arguments arguments) throws Exception {
		if (arguments.count() == 3) {
			float x = (float) arguments.checkDouble(0);
			float y = (float) arguments.checkDouble(1);
			float z = (float) arguments.checkDouble(2);
			if (!model.isFinalize()) {
				model.addVertex(x, y, z);
			} else {
				throw new Exception(OGUtils.getLocalization("og.object.error.isfinalize"));
			}
		}
		return null;
	}

	@Callback(direct = true)
	public Object[] setColor(Context context, Arguments arguments) throws Exception {
		if (arguments.count() == 4) {
			float r = (float) arguments.checkDouble(0);
			float g = (float) arguments.checkDouble(1);
			float z = (float) arguments.checkDouble(2);
			float a = (float) arguments.checkDouble(3);
			if (!model.isFinalize()) {
				model.setColor(r, g, z, a);
			} else {
				throw new Exception(OGUtils.getLocalization("og.object.error.isfinalize"));
			}
		}
		return null;
	}

	@Callback(direct = true)
	public Object[] scale(Context context, Arguments arguments) throws Exception {
		if (arguments.count() == 3) {
			float x = (float) arguments.checkDouble(0);
			float y = (float) arguments.checkDouble(1);
			float z = (float) arguments.checkDouble(2);
			if (!model.isFinalize()) {
				model.scale(x, y, z);
			} else {
				throw new Exception(OGUtils.getLocalization("og.object.error.isfinalize"));
			}
		}
		return null;
	}

	@Callback(direct = true)
	public Object[] create(Context context, Arguments arguments) throws Exception {
		if (arguments.count() == 0) {
			if (model.isValid() && !model.isFinalize()) {
				model.setFinalize(true);
				return new Object[] { new ModelLua(model) };
			} else {
				throw new Exception(model.lastError()!=null ? model.lastError() : OGUtils.getLocalization("og.object.error.isfinalize"));
			}
		}
		return new Object[] { arguments.count() };
	}

}
