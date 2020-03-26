package models.common;

import models.UserModel;

import java.text.DecimalFormat;

public class ResellerRecord {
	public UserModel user;
	public double rate;
	public double profit;

	public ResellerRecord(double rate) {
		this.rate = rate;
	}

	@Override
	public String toString() {
		if (user == null) {
			return "no reseller";
		}

		DecimalFormat df = new DecimalFormat("#.##");
		return user.toString() + "-" + df.format(rate);
	}
}
