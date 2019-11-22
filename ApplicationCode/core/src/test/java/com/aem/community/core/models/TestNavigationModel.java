
package com.aem.community.core.models;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.junit.Before;
import org.junit.Test;

import com.day.cq.wcm.api.designer.Style;

import junitx.util.PrivateAccessor;

/**
 * JUnit test verifying the NavigationModel
 */
public class TestNavigationModel {

    //@Inject
    private NavigationModel navigation;
    
    Style currentStyle = mock(Style.class);
    Resource homePage = mock(Resource.class);
    Resource childPage1 = mock(Resource.class);
    Resource childPage2 = mock(Resource.class);
    Resource jcrContentResource1 = mock(Resource.class);
    Resource jcrContentResource2 = mock(Resource.class);
    ValueMap valueMap1 = mock(ValueMap.class);
    ValueMap valueMap2 = mock(ValueMap.class);
    SlingHttpServletRequest request = mock(SlingHttpServletRequest.class);
    ResourceResolver resourceResolver = mock(ResourceResolver.class);
    List<Resource> lstResource = new ArrayList<Resource>();
    List<Resource> lstChildResource = new ArrayList<Resource>();
    Resource childResPage1 = mock(Resource.class);
    Resource childResPage2 = mock(Resource.class);
    
    Resource jcrContentChildResource1 = mock(Resource.class);
    Resource jcrContentChildResource2 = mock(Resource.class);
    ValueMap childValueMap1 = mock(ValueMap.class);
    ValueMap childValueMap2 = mock(ValueMap.class);

    String rootPagePath = "/content/AEM63App/en";

    @Before
    public void setup() throws Exception {
		when(currentStyle.get("navigationRootPath", StringUtils.EMPTY)).thenReturn(rootPagePath);
        navigation = new NavigationModel();
        PrivateAccessor.setField(navigation, "request", request);
        PrivateAccessor.setField(navigation, "resourceResolver", resourceResolver);
        PrivateAccessor.setField(navigation, "currentStyle", currentStyle);
    }
    
	/**
	 * This test will check the validation for hideInNav for two pages. For one it
	 * is true and for other it is false. So the final list should only have one
	 * page element.
	 * 
	 * @throws Exception
	 */
    @Test
    public void testGetHeaderNavigationListWithNoSubPages() throws Exception {
		lstResource.add(childPage1);
		lstResource.add(childPage2);
        when(resourceResolver.getResource(rootPagePath)).thenReturn(homePage);
        when(homePage.getChildren()).thenReturn(lstResource);
        when(childPage1.getChild("jcr:content")).thenReturn(jcrContentResource1);
        when(childPage2.getChild("jcr:content")).thenReturn(jcrContentResource2);
        
        when(jcrContentResource1.getValueMap()).thenReturn(valueMap1);
        when(valueMap1.containsKey("hideInNav")).thenReturn(false);
        when(valueMap1.get("jcr:title")).thenReturn("Menu 1");
        
        when(jcrContentResource2.getValueMap()).thenReturn(valueMap2);
        when(valueMap2.containsKey("hideInNav")).thenReturn(true);
        when(valueMap2.get("jcr:title")).thenReturn("Menu 2");
        
        when(childPage1.hasChildren()).thenReturn(false);
        when(childPage2.hasChildren()).thenReturn(false);
        navigation.init();
        List<HeaderNavigationVO> msg = navigation.getHeaderNavigationList();
        assertNotNull(msg);
        assertTrue(msg.size() == 1);
    }
    
	/**
	 * This test will check the validation for hideInNav for two pages. For both it
	 * is false. So the final list should only have one page element.
	 * 
	 * Also one of the navigation items will have child pages.
	 * 
	 * @throws Exception
	 */
    @Test
    public void testGetHeaderNavigationListWithSubPages() throws Exception {
		lstResource.add(childPage1);
		lstResource.add(childPage2);
        when(resourceResolver.getResource(rootPagePath)).thenReturn(homePage);
        when(homePage.getChildren()).thenReturn(lstResource);
        when(childPage1.getChild("jcr:content")).thenReturn(jcrContentResource1);
        when(childPage2.getChild("jcr:content")).thenReturn(jcrContentResource2);
        
        when(jcrContentResource1.getValueMap()).thenReturn(valueMap1);
        when(valueMap1.containsKey("hideInNav")).thenReturn(false);
        when(valueMap1.get("jcr:title")).thenReturn("Menu 1");
        
        when(jcrContentResource2.getValueMap()).thenReturn(valueMap2);
        when(valueMap2.containsKey("hideInNav")).thenReturn(false);
        when(valueMap2.get("jcr:title")).thenReturn("Menu 2");
        
        when(childPage1.hasChildren()).thenReturn(true);
        when(childPage2.hasChildren()).thenReturn(false);
        
        lstChildResource.add(childResPage1);
		lstChildResource.add(childResPage2);
        when(childPage1.getChildren()).thenReturn(lstChildResource);
        when(childResPage1.getChild("jcr:content")).thenReturn(jcrContentChildResource1);
        when(childResPage2.getChild("jcr:content")).thenReturn(jcrContentChildResource2);
        
        when(childResPage1.getPath()).thenReturn("childpage1path");
        when(childResPage2.getPath()).thenReturn("childpage2path");
        
        when(resourceResolver.getResource("childpage1path")).thenReturn(childResPage1);
        when(resourceResolver.getResource("childpage2path")).thenReturn(childResPage2);
        
        when(jcrContentChildResource1.getValueMap()).thenReturn(childValueMap1);
        when(childValueMap1.containsKey("hideInNav")).thenReturn(false);
        when(childValueMap1.get("jcr:title")).thenReturn("Sub Menu 1");
        
        when(jcrContentChildResource2.getValueMap()).thenReturn(childValueMap2);
        when(childValueMap2.containsKey("hideInNav")).thenReturn(false);
        when(childValueMap2.get("jcr:title")).thenReturn("Sub  Menu 2");
        navigation.init();
        List<HeaderNavigationVO> msg = navigation.getHeaderNavigationList();
        assertNotNull(msg);
        assertTrue(msg.size() == 2);
        assertNotNull(msg.get(0).getSubLinksList());
        assertNull(msg.get(1).getSubLinksList());
        assertTrue(msg.get(0).getSubLinksList().size() == 2);
    }
    
    @Test
    public void testGetHeaderNavigationRootPagePathBlank() throws Exception {
    	when(currentStyle.get("navigationRootPath", StringUtils.EMPTY)).thenReturn("");
    	navigation.init();
        List<HeaderNavigationVO> msg = navigation.getHeaderNavigationList();
    	assertNotNull(msg);
        assertTrue(msg.size() == 0);
    }
    
    @Test
    public void testGetHeaderNavigationHomePageNull() throws Exception {
    	when(resourceResolver.getResource(rootPagePath)).thenReturn(null);
    	navigation.init();
        List<HeaderNavigationVO> msg = navigation.getHeaderNavigationList();
    	assertNotNull(msg);
        assertTrue(msg.size() == 0);
    }
    
    @Test
    public void testGetHeaderNavigationJCRContentNull() throws Exception {
    	lstResource.add(childPage1);
    	lstResource.add(childPage2);
        when(resourceResolver.getResource(rootPagePath)).thenReturn(homePage);
        when(homePage.getChildren()).thenReturn(lstResource);
        when(childPage1.getChild("jcr:content")).thenReturn(null);
        when(childPage2.getChild("jcr:content")).thenReturn(null);
    	navigation.init();
        List<HeaderNavigationVO> msg = navigation.getHeaderNavigationList();
    	assertNotNull(msg);
        assertTrue(msg.size() == 0);
    }
}
