package com.aem.community.core.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;

import com.aem.community.core.constants.CommonConstants;
import com.day.cq.wcm.api.Page;

public class CommonUtils {

	private CommonUtils() {
	    throw new IllegalStateException("Utility class");
	  }
	/**
	 *
	 * @param path : path
	 * @return String
	 */
	public static String getURL(final String path) {
		if (StringUtils.isBlank(path)) {
			return path;
		} 
		final int protocolIndex = path.indexOf(":/");
		if(protocolIndex > -1){
			return path;
		} else if (path.endsWith(".html") || path.contains(".html#") || path.contains(".html?")) {
			return path;
		} else if (path.startsWith("/content/dam/")) {
			return path;
		} else {
			return path.concat(".html");
		}
	}
	
	/**
	 *
	 * @param page : page
	 * @return String
	 */
	public static String getURL(final Page page) {
		return getURL(page.getPath());
	}
	
	/**
	 * @param resource
	 * @return
	 */
	public static Resource getJCRContentResource(Resource resource) {
		return resource.getChild(CommonConstants.JCR_CONTENT);
	}
	
	/**
	 * @param request
	 * @return
	 */
	public static String extractLanguageCode(SlingHttpServletRequest request) {
		String languageCode = "en";
		String uri = request.getRequestURI();
		if (StringUtils.isBlank(uri)) {
			return languageCode;
		}
		String[] tokens = StringUtils.split(uri, "/");
		if (StringUtils.startsWith(uri, "/content") && tokens.length >= 3) {
			languageCode = tokens[2];
		} else if (StringUtils.startsWith(uri, "/editor") && tokens.length >= 4) {
			languageCode = tokens[3];
		} else if (StringUtils.startsWith(uri, "/conf")) {
			languageCode = "en";
		} else if (tokens.length >= 1) {
			languageCode = tokens[0];
		}
		languageCode = StringUtils.substringBefore(languageCode, ".");
		return languageCode;
	}
	
	/**
	 * @param page
	 * @return
	 */
	public static String getPageTitle(Page page) {
		if (page == null) {
			return StringUtils.EMPTY;
		}
		if (StringUtils.isNotBlank(page.getPageTitle())) {
			return page.getPageTitle();
		}
		return getTitle(page);
	}

	/**
	 * @param page
	 * @return
	 */
	private static String getTitle(Page page) {
		return page.getTitle();
	}
}
