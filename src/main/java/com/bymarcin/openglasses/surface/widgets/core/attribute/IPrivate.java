package com.bymarcin.openglasses.surface.widgets.core.attribute;

import java.util.UUID;

public interface IPrivate extends IAttribute{
	public UUID setOwner(String playerUUID);
	public String getOwner();
	public UUID getOwnerUUID();
}
