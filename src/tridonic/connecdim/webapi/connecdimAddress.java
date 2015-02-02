/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tridonic.connecdim.webapi;

import com.google.gson.JsonObject;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

/**
 * DALI device with a DALI Address usually created with 
 * <code>connecdimLine.getAddress( <i>number</i> )</code>
 *  
 * @version 1.0
 * @since   1.0
 * 
 * @author Jaie Demaagd
 */
public class connecdimAddress extends connecdimApiAccessor { 
    
    
/**
 * Constructor
 *  
 * @version 1.0
 * @since   1.0
 * 
 * @param userName Your connecdim cloud user name
 * @param passKey Your connecdim cloud password
 * @param lineID The id of the line that the address is connected to
 * @param address The dali address assuming 0-63 numbering
 * 
 */
    public connecdimAddress(String userName, String passKey, int lineID, int address) {
        constructAddress(userName, passKey, lineID, address, false);
        
    }
    
    
    // this function is pureley for testing and enables us to run a test on an 
    // address with the first piece of data because by default the latest status 
    // is returned and the results would never be the same to allow a test to pass
    //
    protected connecdimAddress(String userName, String passKey, int lineID, int address, boolean isTest) {
        constructAddress(userName, passKey, lineID, address, isTest);
        
    }

    // creates the object from JSON
    //
    protected connecdimAddress(String userName, String passKey, JsonObject addressData) {
        this.userName = userName;
        this.passKey = passKey;
        
        this.da_object = addressData;
        this.ObjectError = !((this.da_object != null) && this.da_object.has("deviceTypeName"));
        
        
    }    
    
    private void constructAddress(String userName, String passKey, int lineID, int address, boolean isTest)  {
        this.userName = userName;
        this.passKey = passKey;
        //this.address = address;
        
        String APIHtml = get_address_api(lineID, address, isTest);
        this.da_object = parse_da_object(APIHtml);
        
        this.ObjectError = !((this.da_object != null) && this.da_object.has("deviceTypeName"));
        
    }
    
    
    private String get_address_api (int lineid, int address, boolean isTest) {
        
        URL api;
        
        try {

            // if its a test drop the current field out so that the test is always against the same historical row
            // otherwise the data would change each test and it would never work
            if (isTest) {
                api = new URL(this.api_url + "devices/?method=get&ident=" + this.userName + "&key=" + this.passKey + "&filter=lineId::" + lineid + "|address::" + address );
            } else {
                api = new URL(this.api_url + "devices/?method=get&ident=" + this.userName + "&key=" + this.passKey + "&filter=lineId::" + lineid + "|address::" + address + "|isCurrent::true");
            }

            return get_web_request(api);            
            
        } catch (MalformedURLException e) {
            return null;
            
        }

        
    }    
 
 /**
 *  DALI Specification
 * @version 1.0
 * @since   1.0
 */
    public String getDeviceTypeName() {
        return (this.ObjectError) ? null:this.da_object.get("deviceTypeName").getAsString();
        
    }

 /**
 * Returns a unique identifier for the address
 * 
 * @version 1.0
 * @since   1.0
 * @return Unique URL for the connecdim cloud
 */    
    public int getId() {
        return (this.ObjectError) ? -1:this.da_object.get("deviceId").getAsInt();
    }    

 /**
 *  DALI Specification
 * @version 1.0
 * @since   1.0
 * 
 * @return DALI address
 */    
    public int getAddress() {
        return (this.ObjectError) ? -1:this.da_object.get("address").getAsInt();
    }

 /**
 *  DALI Specification
 * @version 1.0
 * @since   1.0
 * 
 * @return DALI Arc Power
 */    
    public int getDimLevel() {
        return (this.ObjectError) ? -1:this.da_object.get("dimLevel").getAsInt();
    }

 /**
 *  DALI Specification
 * @version 1.0
 * @since   1.0
 * 
 * @return DALI Lamp Fail
 */    
    public boolean isLampFail() {
        return (this.ObjectError) ? false:this.da_object.get("lampFail").getAsBoolean();
    }

 /**
 *  DALI Specification
 * @version 1.0
 * @since   1.0
 * 
 * @return DALI Power Recover Flag
 */    
    public boolean isPowerRecover() {
        return (this.ObjectError) ? false:this.da_object.get("powerRecover").getAsBoolean();
    }

 /**
 *  DALI Specification
 * @version 1.0
 * @since   1.0
 * 
 * @return DALI Limit Error
 */    
    public boolean isLimitError() {
        return (this.ObjectError) ? false:this.da_object.get("limitError").getAsBoolean();
    }
    

 /**
 *  DALI Specification Device Type 1 Emergency
 * @version 1.0
 * @since   1.0
 * 
 * @return DALI Emergency Status
 */    
    public int getEmergencyStatus() {
        return json_get_int("emergencyStatus");
    }    

    
    
/**
 *  DALI Specification Device Type 1 Emergency
 * @version 1.0
 * @since   1.0
 */     
    public int getChargePercent() {
        return json_get_int("chargePercent");
    }  

/**
 *  DALI Specification Device Type 1 Emergency
 * @version 1.0
 * @since   1.0
 */     
    public int getDurationTestResult() {
        return json_get_int("durationTestResult");
    }  

/**
 *  DALI Specification Device Type 1 Emergency
 * @version 1.0
 * @since   1.0
 */     
    public int getTotalBatteryTime() {
        return json_get_int("totalBatteryTime");
    }  

/**
 *  DALI Specification Device Type 1 Emergency
 * @version 1.0
 * @since   1.0
 */     
    public int getTotalOperatingTime() {
        return json_get_int("totalOperatingTime");
    }      
    
/**
 *  DALI Specification Device Type 1 Emergency
 * @version 1.0
 * @since   1.0
 */ 
    public int getOperatingHours() {
        return json_get_int("operatingHours");
    }    

/**
 *  DALI Specification Device Type 1 Emergency
 * @version 1.0
 * @since   1.0
 */     
    public int getOperatingMinutes() {
        return json_get_int("operatingMinutes");
    }  

/**
 *  DALI Specification Device Type 1 Emergency
 * @version 1.0
 * @since   1.0
 */     
    public int getRatedDuration() {
        return json_get_int("ratedDuration");
    }  

/**
 *  DALI Specification Device Type 1 Emergency
 * @version 1.0
 * @since   1.0
 */     
    public int getRatedDurationRaw() {
        return json_get_int("ratedDurationRaw");
    }  

/**
 *  DALI Specification Device Type 1 Emergency
 * @version 1.0
 * @since   1.0
 */     
    public int getEmergencyMode() {
        return json_get_int("emergencyMode");
    }     

/**
 *  DALI Specification Device Type 1 Emergency
 * @version 1.0
 * @since   1.0
 */     
    public int getEmergencyModeStart() {
        return json_get_int("emergencyModeStart");
    }    

/**
 *  DALI Specification Device Type 1 Emergency
 * @version 1.0
 * @since   1.0
 */     
    public int getEmergencyFeatures() {
        return json_get_int("emergencyFeatures");
    }  

/**
 *  DALI Specification Device Type 1 Emergency
 * @version 1.0
 * @since   1.0
 */     
    public int getEmergencyFailures() {
        return json_get_int("emergencyFailures");
    }  

/**
 *  DALI Specification Device Type 1 Emergency
 * @version 1.0
 * @since   1.0
 */     
    public int getFunctionTestStart() {
        return json_get_int("functionTestStart");
    }  

/**
 *  DALI Specification Device Type 1 Emergency
 * @version 1.0
 * @since   1.0
 */     
    public int getFunctionTestEnd() {
        return json_get_int("functionTestEnd");
    }     

/**
 *  DALI Specification Device Type 1 Emergency
 * @version 1.0
 * @since   1.0
 */ 
    public int getDurationTestStart() {
        return json_get_int("durationTestStart");
    }  

/**
 *  DALI Specification Device Type 1 Emergency
 * @version 1.0
 * @since   1.0
 */     
    public int getDurationTestEnd() {
        return json_get_int("durationTestEnd");
    }  

/**
 *  DALI Specification Device Type 1 Emergency
 * @version 1.0
 * @since   1.0
 */     
    public int getCircuitFailStart() {
        return json_get_int("circuitFailStart");
    }  

/**
 *  DALI Specification Device Type 1 Emergency
 * @version 1.0
 * @since   1.0
 */     
    public int getBatteryDurationFailStart() {
        return json_get_int("batteryDurationFailStart");
    }     

/**
 *  DALI Specification Device Type 1 Emergency
 * @version 1.0
 * @since   1.0
 */     
    public int getBatteryFailStart() {
        return json_get_int("batteryFailStart");
    }     

/**
 *  DALI Specification Device Type 1 Emergency
 * @version 1.0
 * @since   1.0
 */     
    public int getLampReplaced() {
        return json_get_int("lampReplaced");
    }
    
/**
 *  DALI Specification Device Type 1 Emergency
 * @version 1.0
 * @since   1.0
 */     
    public int getBatteryReplaced() {
        return json_get_int("batteryReplaced");
    }
    
/**
 *  DALI Specification Device Type 1 Emergency
 * @version 1.0
 * @since   1.0
 */     
    public int getUnitReplaced() {
        return json_get_int("unitReplaced");
    }
    
/**
 *  DALI Specification Device Type 1 Emergency
 * @version 1.0
 * @since   1.0
 */     
    public int getTestTimes() {
        return json_get_int("testTimes");
    }    
    
    
    
/**
 * The number of times the devices lamp has turned on and off
 * 
 * @version 1.0
 * @since   1.0
 */ 
    public int getStrikeCount() {
        return (this.ObjectError) ? -1:this.da_object.get("strikeCount").getAsInt();
    }

/**
 * The location of the light in 3D space used for creating 3D models
 * 
 * @version 1.0
 * @since   1.0
 */     
    public String getLocation() {
        return (this.ObjectError) ? null:this.da_object.get("location").getAsString();

    }

/**
 * The model of the device as named in the cloud
 * @version 1.0
 * @since   1.0
 */     
    public String getModel() {
        return (this.ObjectError) ? null:this.da_object.get("model").getAsString();

    }

/**
 * The wattage of the device when using the maximum allowed power
 * @version 1.0
 * @since   1.0
 */     
    public float getWattage() {
        return (this.ObjectError) ? -1:this.da_object.get("wattage").getAsFloat();
    }  

/**
 * The calculated amount of power the device has used over its lifetime
 * @version 1.0
 * @since   1.0
 */     
    public float getPowerConsumption() {
        return (this.ObjectError) ? -1:this.da_object.get("powerConsumption").getAsFloat();
    }
    
/**
 * The code returned by the device or 0
 * @version 1.0
 * @since   1.0
 */     
    public int getErrorState() {
        return (this.ObjectError) ? -1:this.da_object.get("errorState").getAsInt();
    }    
    
/**
 * The number of bus errors detected while communicating with this device, useful for
 * identifying error prone electrical wiring or devices
 * @version 1.0
 * @since   1.0
 */     
    public int getCommErrors() {
        return (this.ObjectError) ? -1:this.da_object.get("commErrors").getAsInt();
    }    

/**
 * The date the device was first seen on the connecdim cloud
 * @version 1.0
 * @since   1.0
 */     
    public Date getFirstReported() {
        return (this.ObjectError) ? null:json_get_date("firstReported");
    }   

/**
 * the local time of the gateway when the last change event occurred
 * @version 1.0
 * @since   1.0
 */     
    public Date getLocalTime() {
        return (this.ObjectError) ? null:json_get_date("localTime");
    } 

/**
 * If the connecdim cloud has detected that the gateway may have potentially
 * bad local time based on comparisons with the server time
 * @version 1.0
 * @since   1.0
 */     
    public boolean isLocalTimeFlagged() {
        return (this.ObjectError) ? null:this.da_object.get("localTimeFlagged").getAsBoolean();
        
    } 
  
/**
 * Time of the last change was reported to the server
 * @version 1.0
 * @since   1.0
 */     
    public Date getSystemTime() {
        return (this.ObjectError) ? null:json_get_date("systemTime");
    }     

/**
 * Whether this record is live (current), otherwise it is historical
 * @version 1.0
 * @since   1.0
 * @return true if this is the latest record
 */     
    public boolean isCurrent() {
        return (this.ObjectError) ? null:this.da_object.get("isCurrent").getAsBoolean();
    } 

/**
 * The device has been flagged for deletion and is not visible on the connecdim cloud
 * @version 1.0
 * @since   1.0
 */     
    public boolean isRemoved() {
        return (this.ObjectError) ? null:this.da_object.get("isRemoved").getAsBoolean();
    } 

/**
 * This is the first report of the device as it has never been seen on the cloud before
 * @version 1.0
 * @since   1.0
 */     
    public boolean isNew() {
        return (this.ObjectError) ? null:this.da_object.get("isNew").getAsBoolean();
    } 
    
    //////////////////////////////////////////////////////////////////////////
    //                                                                      //
    //  Because some of the default emergency fields return a null when     //
    //      the device is not an emergency, this function ensures an error  //
    //      code of -1 is returned instead of null which isn't allowed      //
    //      as a return for int in java                                     //
    //                                                                      //
    private int json_get_int(String JsonName) {
       
        int result = -1;
                        
        if ((this.da_object != null) && this.da_object.has(JsonName)) {
            result = (this.da_object.get(JsonName).isJsonNull()) ? -1 : this.da_object.get(JsonName).getAsInt();
        }
            
        return result;
        
    }
  
}
