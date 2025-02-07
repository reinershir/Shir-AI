package io.github.reinershir.boot.dto.req;

import java.util.ArrayList;

import io.github.reinershir.auth.core.model.Role;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Role")
public class RoleDTO extends Role{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5734358829121162547L;
	private ArrayList<Long> menuIds;

	public ArrayList<Long> getMenuIds() {
		return menuIds;
	}

	public void setMenuIds(ArrayList<Long> menuIds) {
		this.menuIds = menuIds;
	}

	
	 
}
