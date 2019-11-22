package com.aem.community.core.models;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Source;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import com.day.cq.wcm.api.designer.Style;

@Model(adaptables = { SlingHttpServletRequest.class, Resource.class })
public class NavigationModel {
	
	/**
	 * request.
	 */
	@SlingObject
	private SlingHttpServletRequest request;
    @SlingObject
    private ResourceResolver resourceResolver;
    
    @Inject @Source("script-bindings")
    private Style currentStyle;
    
    private List<HeaderNavigationVO> headerNavigationList;
    
    @PostConstruct
    protected void init() {
    	headerNavigationList = new ArrayList<>();
        String rootPagePath = currentStyle.get("navigationRootPath", StringUtils.EMPTY);
        if (StringUtils.isNoneBlank(rootPagePath)) {
        	Resource homePage = resourceResolver.getResource(rootPagePath);
        	if (homePage != null) {
        		Iterable<Resource> children = homePage.getChildren();
        		for (Resource child : children) {
        			if (showInNavigation(child)) {
        				getNavigationDetails(child);
        			}
        		}
			}
		}
    }

	/**
	 * @param child
	 */
	private void getNavigationDetails(Resource child) {
		Resource jcrContentResource = child.getChild("jcr:content");
		String displayText = jcrContentResource.getValueMap().get(
				"jcr:title").toString();
		HeaderNavigationVO headerNavigationVO = new HeaderNavigationVO(displayText, child.getPath());
		boolean hasSubNavigation = child.hasChildren();
		headerNavigationVO.setHasSubNavigation(hasSubNavigation);
		if (hasSubNavigation) {
			headerNavigationVO.setSubLinksList(getSubNavigationPages(child));
		}
		headerNavigationList.add(headerNavigationVO);
	}
    
    /**
	 * The method getSubNavigationPages retrieves and returns the list of
	 * sub-navigation links.
	 * 
	 * @param resource
	 * @return
	 */
	private List<SubNavigationVO> getSubNavigationPages(Resource resource) {
		List<SubNavigationVO> subNavLinks = new ArrayList<>();
		Iterable<Resource> children = resource.getChildren();
		String displayText = null;
		String linkURL = null;
		Resource jcrContentResource = null;
		for (Resource child : children) {
			jcrContentResource = child.getChild("jcr:content");
			linkURL = child.getPath();
			if (jcrContentResource != null) {
				displayText = jcrContentResource.getValueMap().get(
						"jcr:title").toString();
			}
			if (showInNavigation(resourceResolver.getResource(linkURL))) {
				subNavLinks.add(new SubNavigationVO(displayText, linkURL));
			}
		}

		return subNavLinks;
	}
	
    /**
	 * The method showInNavigation is used to check whether the resource should
	 * be shown in top navigation or not.
	 * 
	 * @param resource
	 * @return
	 */
	private boolean showInNavigation(Resource resource) {
		Resource jcrContentResource = resource.getChild("jcr:content");
		boolean showInNav = false;
		if (jcrContentResource != null) {
			ValueMap vMap = jcrContentResource.getValueMap();
			boolean hideInNav = vMap.containsKey("hideInNav");
			if(!hideInNav) {
				showInNav = true;
			}
		}
		return showInNav;
	}

	public List<HeaderNavigationVO> getHeaderNavigationList() {
		return headerNavigationList;
	}
	
}
