package com.yunnex.checkversionupdate;

import com.yunnex.vpay.lib.http.NoNull;
import com.yunnex.vpay.lib.http.ResponseBase;

public class AppDetailsResponse extends ResponseBase
{
	private UpdateBean appRecommendedItem;

	public UpdateBean getAppRecommendedItem()
	{
		return appRecommendedItem;
	}

	public void setAppRecommendedItem(UpdateBean appItem)
	{
		this.appRecommendedItem = appItem;
	}

	@Override
	public String toString() {
		return "AppDetailsResponse{" +
				"appRecommendedItem=" + appRecommendedItem +
				"} " + super.toString();
	}
}
