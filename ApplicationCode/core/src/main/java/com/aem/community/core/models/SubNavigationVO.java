package com.aem.community.core.models;

import com.aem.community.core.utils.CommonUtils;

public class SubNavigationVO {
	
	private String displayText;
	private String linkURL;

	
	public SubNavigationVO(String displayText, String linkURL) {
		this.displayText = displayText;
		this.linkURL = linkURL;
	}

	/**
	 * @return the displayText
	 */
	public String getDisplayText() {
		return displayText;
	}

	/**
	 * @param displayText the displayText to set
	 */
	public void setDisplayText(String displayText) {
		this.displayText = displayText;
	}

	/**
	 * @return the linkURL
	 */
	public String getLinkURL() {
		return CommonUtils.getURL(linkURL);
	}

	/**
	 * @param linkURL the linkURL to set
	 */
	public void setLinkURL(String linkURL) {
		this.linkURL = linkURL;
	}
}
