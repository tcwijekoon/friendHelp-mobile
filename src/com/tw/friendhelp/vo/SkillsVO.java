package com.tw.friendhelp.vo;

import java.io.Serializable;

public class SkillsVO implements Serializable {
	private int skill_id;
	private String skill;

	public int getSkill_id() {
		return skill_id;
	}

	public String getSkill() {
		return skill;
	}

	public void setSkill_id(int skill_id) {
		this.skill_id = skill_id;
	}

	public void setSkill(String skill) {
		this.skill = skill;
	}

}
