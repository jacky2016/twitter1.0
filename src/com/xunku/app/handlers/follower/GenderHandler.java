package com.xunku.app.handlers.follower;

import com.xunku.app.enums.GenderEnum;
import com.xunku.app.interfaces.IAccount;
import com.xunku.app.interfaces.IFollowerHandler;

public class GenderHandler implements IFollowerHandler {

	int male;
	int female;
	int unknow;

	@Override
	public void process(IAccount follower) {
		if (follower.getGender() == GenderEnum.Male) {
			male++;
		} else if (follower.getGender() == GenderEnum.Famale) {
			female++;
		} else {
			unknow++;
		}

	}

	public int getMale() {
		return male;
	}

	public int getFemale() {
		return female;
	}

	public int getUnknow() {
		return unknow;
	}

}
