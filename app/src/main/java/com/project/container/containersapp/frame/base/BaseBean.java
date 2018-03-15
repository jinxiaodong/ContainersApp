package com.project.container.containersapp.frame.base;

import com.redstar.library.utils.JsonUtil;

import java.io.Serializable;
/*
* 所有bean类必须继承该类，以放便后面去除代码混淆
* */
public class BaseBean implements Serializable {
	@Override
	public String toString() {
		String message = "";
		try {
			message = JsonUtil.objectToString(this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return message;
	}
}
