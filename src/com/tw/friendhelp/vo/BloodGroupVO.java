package com.tw.friendhelp.vo;

import java.io.Serializable;

public class BloodGroupVO implements Serializable {
	private int bld_grp_id;
	private String bld_group;

	public int getBld_grp_id() {
		return bld_grp_id;
	}

	public String getBld_group() {
		return bld_group;
	}

	public void setBld_grp_id(int bld_grp_id) {
		this.bld_grp_id = bld_grp_id;
	}

	public void setBld_group(String bld_group) {
		this.bld_group = bld_group;
	}

}
