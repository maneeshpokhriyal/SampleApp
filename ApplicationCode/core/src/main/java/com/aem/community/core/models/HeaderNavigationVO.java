package com.aem.community.core.models;

import java.util.List;

import com.aem.community.core.utils.CommonUtils;

public class HeaderNavigationVO {

	/**
	 * The navigation link display text.
	 */
	private String displayText;

	public String getDisplayText() {
		return displayText;
	}

	public void setDisplayText(String displayText) {
		this.displayText = displayText;
	}

	public String getLinkURL() {
		return linkURL;
	}

	public void setLinkURL(String linkURL) {
		this.linkURL = linkURL;
	}

	/**
	 * The navigation link URL
	 */
	private String linkURL;

	/**
	 * The hasSubNavigation property
	 */
	
	private boolean hasSubNavigation;

	public boolean isHasSubNavigation() {
		return hasSubNavigation;
	}

	/**
	 * @param displayText
	 * @param linkURL
	 */
	private List<SubNavigationVO> subLinksList;
	
	public HeaderNavigationVO(String displayText, String linkURL) {
		this.displayText = displayText;
		this.linkURL = linkURL;
	}

	public List<SubNavigationVO> getSubLinksList() {
		return subLinksList;
	}

	public void setSubLinksList(List<SubNavigationVO> subLinksList) {
		this.subLinksList = subLinksList;
	}

	/**
	 * @param hasSubNavigation
	 *            the hasSubNavigation to set
	 */
	public void setHasSubNavigation(boolean hasSubNavigation) {
		this.hasSubNavigation = hasSubNavigation;
	}

}
