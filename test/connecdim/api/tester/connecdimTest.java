/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connecdim.api.tester;

import tridonic.connecdim.webapi.connecdimAddress;
import tridonic.connecdim.webapi.connecdimSite;
import tridonic.connecdim.webapi.connecdimGroup;
import tridonic.connecdim.webapi.connecdim;
import tridonic.connecdim.webapi.connecdimLine;
import tridonic.connecdim.webapi.connecdimScene;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import tridonic.connecdim.webapi.connecdimGateway;
/**
 *
 * @author jaie
 */
public class connecdimTest {
    
    
    public connecdimTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void testInvalidPassword() {
        // test out of range line
        connecdim MyFailTest = new connecdim("guest@tridonic.com", "blahlblah");
        assertFalse(MyFailTest.AuthSuccess());
        assertNull(MyFailTest.getSites());
        assertNull(MyFailTest.getLine("0013480213DA", 20)); 
    }
    
 
    @Test
    public void testSiteAndAuthenticateLater() {
        // test out of range line
        connecdim MyTest = new connecdim();
        assertFalse(MyTest.AuthSuccess());
        assertTrue(MyTest.AuthSuccess("guest@tridonic.com", "i_love_connecdim"));
        
        assertNotNull(MyTest);
        assertNotNull(MyTest.getFailedDevices().get(0));
        
        assertEquals(974509, MyTest.getFailedDevices().get(0).getId());

    }    
    
    
    @Test
    public void testSite() {
        // test out of range line
        connecdim MyTest = new connecdim("guest@tridonic.com", "i_love_connecdim");
        assertTrue(MyTest.AuthSuccess());
        assertEquals("guest@tridonic.com", MyTest.getUsername());
        
        List<connecdimSite> mysites = MyTest.getSites();
        System.out.println("Numer of Sites Returned is:" + mysites.size());
        connecdimSite MySite = mysites.get(0);
        
        assertTrue(MySite.isValid());
        
        assertEquals(1, MySite.getId());
        assertEquals("Tridonic Wollongong", MySite.getName());
        assertEquals("13 First Avenue", MySite.getStreet());
        assertEquals("Unanderra", MySite.getCity());
        assertEquals("NSW", MySite.getState());
        assertEquals("Australia", MySite.getCountry());
        assertEquals("2526", MySite.getPostCode());
        
        assertEquals(0, MySite.getAddressBase());
        assertEquals(0, MySite.getGroupBase());
        assertEquals(0, MySite.getSceneBase());
        
        assertEquals("00134801DD18", MySite.getGateways().get(0).getMac());        
        
        mysites.get(0).getGateways().get(0).getMac();
        
    }
    
    
    @Test
    public void testNoLine() {
        // test out of range line
        connecdim MyTest = new connecdim("guest@tridonic.com", "i_love_connecdim");
        connecdimLine MyLine = MyTest.getLine("00134801DD25", 20);
        assertFalse(MyLine.isValid());
        assertNull(MyLine.getName());
    }
    
    
    @Test
    public void testGetLine() throws IOException {
        
        connecdim MyTest = new connecdim("guest@tridonic.com", "i_love_connecdim");
        connecdimLine MyLine = MyTest.getLine("00134801DD25", 1);
        assertTrue(MyLine.isValid());
        
        assertEquals("Everything", MyLine.getName());
        assertEquals(1, MyLine.getNumber());
        assertEquals(0, MyLine.getBusErrors());
        assertEquals(19, MyLine.getId());
        assertEquals(new Date("Wed, 21 Jan 2015 04:36:29 GMT"), MyLine.getLastUpdated());
        assertEquals("https://api.connecdim.com/api/Lines/", MyLine.getLink());
        
        assertFalse(MyLine.isStatus());
        assertFalse(MyLine.isRemoved());
        
        MyLine = MyTest.getLine("00134801DD18", 1);
        assertEquals(974509, MyLine.getFailedDevices().get(0).getId());
        assertEquals(0, MyLine.getAddressesUnordered().get(0).getAddress());
        
        assertEquals(3, MyLine.getAddressHistory(3).get(0).getAddress());
        assertEquals(20, MyLine.getAddressHistory(20).get(0).getAddress());
        
    }
    
    @Test
    public void testGetFalseAddress()  {  
        
        connecdim MyTest = new connecdim("guest@tridonic.com", "i_love_connecdim");
        connecdimLine MyLine = MyTest.getLine("00134801DD25", 1);  
        connecdimAddress MyAddress = MyLine.getAddress(200);
        assertFalse(MyAddress.isValid());
    
    }    
    
        
    @Test
    public void testGetAddress() {        
        
        connecdim MyTest = new connecdim("guest@tridonic.com", "i_love_connecdim");
        connecdimLine MyLine = MyTest.getLine("00134801DD25", 1);  
        
        connecdimAddress MyAddress = MyLine.getAddress(1, true);
        assertTrue(MyAddress.isValid());
        
        assertEquals("LED Module", MyAddress.getDeviceTypeName());
        assertEquals(503110, MyAddress.getId());
        assertEquals(1, MyAddress.getAddress());
        assertEquals(38, MyAddress.getDimLevel());
        assertEquals(false, MyAddress.isLampFail());
        assertEquals(false, MyAddress.isPowerRecover());
        assertEquals(false, MyAddress.isLimitError());
        
        assertEquals(-1, MyAddress.getEmergencyStatus());
        assertEquals(-1, MyAddress.getChargePercent());
        assertEquals(-1, MyAddress.getDurationTestResult());
        assertEquals(-1, MyAddress.getTotalBatteryTime());
        assertEquals(-1, MyAddress.getTotalOperatingTime());
        assertEquals(-1, MyAddress.getOperatingHours());
        assertEquals(-1, MyAddress.getOperatingMinutes());
        assertEquals(-1, MyAddress.getRatedDuration());
        assertEquals(-1, MyAddress.getRatedDurationRaw());
        assertEquals(-1, MyAddress.getEmergencyMode());
        assertEquals(-1, MyAddress.getEmergencyModeStart());
        assertEquals(-1, MyAddress.getEmergencyFeatures());
        assertEquals(-1, MyAddress.getEmergencyFailures());
        assertEquals(-1, MyAddress.getFunctionTestStart());
        assertEquals(-1, MyAddress.getFunctionTestEnd());
        assertEquals(-1, MyAddress.getDurationTestStart());
        assertEquals(-1, MyAddress.getDurationTestEnd());
        assertEquals(-1, MyAddress.getCircuitFailStart());
        assertEquals(-1, MyAddress.getBatteryDurationFailStart());
        assertEquals(-1, MyAddress.getBatteryFailStart());
        assertEquals(-1, MyAddress.getLampReplaced());
        assertEquals(-1, MyAddress.getBatteryReplaced());
        assertEquals(-1, MyAddress.getUnitReplaced());
        assertEquals(-1, MyAddress.getTestTimes());     
        
        
        assertEquals(0, MyAddress.getStrikeCount());
        assertEquals("Unknown", MyAddress.getLocation());
        assertEquals("Unknown", MyAddress.getModel());
        assertEquals(60.5, MyAddress.getWattage(), .001);
        assertEquals(0, MyAddress.getPowerConsumption(), .001);
        assertEquals(0, MyAddress.getErrorState());
        assertEquals(0, MyAddress.getCommErrors());
        assertEquals(new Date("Mon, 22 Jul 2013 14:54:26 GMT"), MyAddress.getFirstReported());
        assertTrue(new java.util.Date().after(MyAddress.getLocalTime()));
        assertFalse(MyAddress.isLocalTimeFlagged());
        assertTrue(new java.util.Date().after(MyAddress.getSystemTime()));
        assertFalse(MyAddress.isCurrent());
        assertFalse(MyAddress.isRemoved());
        assertTrue(MyAddress.isNew());
        assertEquals("https://api.connecdim.com/api/Devices/503110", MyAddress.getLink());

        
    } 
    
    
    
    @Test
    public void testGetGroup()  {        
        
        connecdim MyTest = new connecdim("guest@tridonic.com", "i_love_connecdim");
        connecdimLine MyLine = MyTest.getLine("00134801DD25", 1);     
        
        connecdimGroup MyGroup = MyLine.getGroup(1);

        assertTrue(MyGroup.isValid());
        
        assertEquals(182, MyGroup.getId());
        assertEquals(1, MyGroup.getNumber());

        assertEquals("Group 1", MyGroup.getName());
        assertEquals(0, MyGroup.getArea());
        assertEquals("https://api.connecdim.com/api/Groups/182", MyGroup.getLink());
        
    }     

    
    
    @Test
    public void testGetScene() {        
        
        connecdim MyTest = new connecdim("guest@tridonic.com", "i_love_connecdim");
        connecdimLine MyLine = MyTest.getLine("00134801DD25", 1);   
        
        connecdimScene MyScene = MyLine.getScene(1);
        assertTrue(MyScene.isValid());
        
        assertEquals(236, MyScene.getId());
        assertEquals(1, MyScene.getNumber());

        assertEquals("Scene 1", MyScene.getName());
        assertEquals("https://api.connecdim.com/api/Scenes/236", MyScene.getLink());
        
    }     
    
    @Test
    public void testGetGateway() {

        connecdim MyTest = new connecdim("guest@tridonic.com", "i_love_connecdim");
        connecdimGateway MyGW = MyTest.getGateway("00134801DD18");
        
        assertEquals(13, MyGW.getId());
        assertEquals("00134801DD18", MyGW.getMac());
        assertEquals("Bench Testing Interface", MyGW.getName());
        assertEquals("Australia/Sydney", MyGW.getTimeZone()) ;
        assertEquals(1000, MyGW.getTimeOffset());
        assertFalse(MyGW.isTimeError());
        assertEquals("115.70.52.245", MyGW.getExternalIP());
        assertEquals("172.24.45.175", MyGW.getInternalIP());
        assertFalse(MyGW.isRemoved());
        assertEquals(new Date("Fri, 25 Jul 2014 15:16:45 GMT"), MyGW.getLastUpdated());
        assertEquals("https://api.connecdim.com/api/Gateways/13", MyGW.getLink());
        
        assertEquals(18, MyGW.getLines().get(0).getId());
        assertEquals(974509, MyGW.getFailedDevices().get(0).getId());
        
    }
            
    
    
}
