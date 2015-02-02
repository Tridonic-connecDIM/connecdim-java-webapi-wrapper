/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tridonic.connecdim.webapi;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * DALI line that is associated to a connecdim gateway. Usually accessed with 
 * connecdimGateway.getLine( <i>Line Number</i> )
 *  
 * @version 1.1
 * @since   1.0
 * 
 * @author Jaie Demaagd
 */
public class connecdimLine extends connecdimAddressContainer {

    /**
     * DALI line that is associated to a connecdim gateway. Usually accessed with 
     * connecdimGateway.getLine( <i>Line Number</i> )
     *  
 * @version 1.0
 * @since   1.0
 * 
     * @param userName the username for authenticating to the cloud
     * @param passKey the password for authenticating to the cloud
     * @param gateway the macaddress of a gateway 
     * @param linenumber the line number
     * 
     */    
    public connecdimLine(String userName, String passKey, String gateway, int linenumber) {
        
        this.userName = userName;
        this.passKey = passKey;
        
        String APIHtml = get_line_api(gateway, linenumber);
        this.da_object = parse_da_object(APIHtml);
        
        ObjectError = !((this.da_object != null) && this.da_object.has("name"));
        this.address_container_filter = (ObjectError) ? "" : "|lineId::" + this.getId();
                                // we check that the object exists or we
                                // will get a fail back as there will be no id field
                                // in the JSON
            
    }   
    
    protected connecdimLine(String userName, String passKey, JsonObject lineJson) {
        
        this.userName = userName;
        this.passKey = passKey;
        
        this.da_object = lineJson;
        this.address_container_filter = "|lineId::" + this.getId();
        
        ObjectError = !((this.da_object != null) && this.da_object.has("name"));
        this.address_container_filter = (ObjectError) ? "" : "|lineId::" + this.getId();    
    }      
    
    
    
    /**
     * Returns a DALI address
     * 
     * @param addressnumber the DALI address number of the address assuming
     * the address starts from 0
     *  
     * @version 1.0
     * @since   1.0
     * 
     * @return the address object
     * 
     */   
    public connecdimAddress getAddress(int addressnumber) {
        return new connecdimAddress(this.userName, this.passKey, getId(), addressnumber, false);   
        
    }

    
    /**
     * Returns the first DALI address record used for testing
     * 
     * @param addressnumber the DALI address number of the address assuming
     * the address starts from 0
     * @param isFirst specifies to return the first record
     *  
     * @version 1.0
     * @since   1.0
     * 
     * @return the address object
     * 
     */       
    public connecdimAddress getAddress(int addressnumber, boolean isFirst) {
        return new connecdimAddress(this.userName, this.passKey, getId(), addressnumber, isFirst);   
        
    }
    
    //private connecdimAddress get_address_with_test(int addressnumber, boolean isFirst) {
    //    return new connecdimAddress(this.userName, this.passKey, getId(), addressnumber, isFirst);
    //}

    /**
     * Returns a DALI group in the DALI line
     *  
     * @version 1.0
     * @since   1.0
     * 
     * @param groupnumber The DALI Group ID starting from 0
     * @return the DALI group object
     * 
     */       
    public connecdimGroup getGroup(int groupnumber) {
        return new connecdimGroup(this.userName, this.passKey, getId(), groupnumber);
    }    


    /**
     * Returns a DALI scene in the DALI line
     * 
     * @param sceneNumber the DALI scene number assuming
     * the scenes starts from 0
     *  
 * @version 1.0
 * @since   1.0
 * 
     * @return the DALI scene object
     * 
     */           
    public connecdimScene getScene(int sceneNumber) {
        return new connecdimScene(this.userName, this.passKey, getId(), sceneNumber);
    }       
     
    
    
    private String get_line_api (String gateway, int linenumber) {
        
        // builds the api call to receive dali line information and returns the result
        URL api = null;
        try {
            api = new URL(this.api_url + "lines/?method=get&ident=" + this.userName + "&key=" + this.passKey + "&filter=gatewayMac::" + gateway + "|" + "number::" + linenumber);
        } catch (MalformedURLException ex) {
            Logger.getLogger(connecdimLine.class.getName()).log(Level.SEVERE, null, ex);
        }
        return get_web_request(api);
        
    }  
    

/**
* Retrieve all active DALI Addresses that are associated with this Line, note 
* that the array index is not related to the DALI address number
*  
* @version 1.1
* @since   1.1
* 
* @return Array list of connecdimGateway objects
* 
*/        
    public List<connecdimAddress> getAddressesUnordered() {              
        
        if (!ObjectError){
            String APIHtml = get_addresses_api();
            List<connecdimAddress> addresses = new ArrayList<>();
            parse_data_addresses_objects(APIHtml, addresses);
            return addresses;
        
        } else {
            return null;    // becuase if the username password was wrong there
                            // is nothing to return
        
        }
    }       
    
    
/**
* Retrieve the most recent ten records of historical data for the address
* 
* @version 1.1
* @since   1.1
* 
* @param address The DALI address the history should relate to
* 
* @return Array list of connecdimGateway objects
* 
*/
    public List<connecdimAddress> getAddressHistory(int address) {              
        
        return getAddressHistory(address, 10);

    }       
        
    
/**
* Retrieve historical data for the address up to a certain number of records
*  
* @version 1.1
* @since   1.1
* 
* @param address The DALI address the history should relate to
* @param limit The number of historical cloud records
* 
* @return Array list of connecdimGateway objects
* 
*/        
    public List<connecdimAddress> getAddressHistory(int address, int limit) {              
        
        if (!ObjectError){
            String APIHtml = get_history_api(address, limit);
            List<connecdimAddress> addresses = new ArrayList<>();
            parse_data_addresses_objects(APIHtml, addresses);
            return addresses;
        
        } else {
            return null;    // becuase if the username password was wrong there
                            // is nothing to return
        
        }
    }       
    
    
/**
* Ask the server for the addresses api collection
*  
* @version 1.1
* @since   1.1
* 
* @return string api reply from server
* 
*/       
    private String get_addresses_api () {
        
        // builds the api call to receive dali line information and returns the result
        URL api = null;
        try {
            api = new URL(this.api_url + "devices/?method=get&ident=" + this.userName + "&key=" + this.passKey + "&filter=lineId::" + this.getId() + "|isCurrent::true&limit=64" );
        } catch (MalformedURLException ex) {
            Logger.getLogger(connecdimLine.class.getName()).log(Level.SEVERE, null, ex);
        }
        return get_web_request(api);
        
    }       
    
    
/**
* Ask the server for the addresses api collection
*  
* @version 1.1
* @since   1.1
* 
* @return string api reply from server
* 
*/       
    private String get_history_api (int address, int limit) {
        
        // builds the api call to receive dali line information and returns the result
        URL api = null;
        try {
            api = new URL(this.api_url + "devices/?method=get&ident=" + this.userName + "&key=" + this.passKey + "&filter=lineId::" + this.getId() + "|address::" + address + "&limit=" + limit);
        } catch (MalformedURLException ex) {
            Logger.getLogger(connecdimLine.class.getName()).log(Level.SEVERE, null, ex);
        }
        return get_web_request(api);
        
    }      
    
/**
* Parse the api values and turn them into address objects
*  
* @version 1.1
* @since   1.1
* 
* @return string api reply from server
* 
*/  
    private void parse_data_addresses_objects(String HtmlReturn, List<connecdimAddress> lines) {
        
        JsonElement da_json = new JsonParser().parse(HtmlReturn);
        JsonObject da_top_object = da_json.getAsJsonObject();
        
        JsonElement da_line_element;
        JsonObject da_line_object;
        
        int i;
        
        // find and pull out the line object from JSON
        if (da_top_object.has("data")) {
            JsonArray da_array = da_top_object.getAsJsonArray("data");
            
            i = 0;
            while (i < da_array.size()) {
                da_line_element = da_array.get(i);
                da_line_object = da_line_element.getAsJsonObject();
                i++;
                
                connecdimAddress xAddress = new connecdimAddress(this.userName, this.passKey, da_line_object);
                lines.add(xAddress);
                
            }            
        }   
    }    
    
    
/**
 * Returns the name of the line as set in the connecdim cloud
 *  
 * @version 1.0
 * @since   1.0
 * 
 * @return the name of the line
 */ 
    public String getName() {
        return (ObjectError) ? null:this.da_object.get("name").getAsString();
        
    }
    
/**
 * Returns the number of the line in relation to the gateway being 1-4
 *  
 * @version 1.0
 * @since   1.0
 * 
 * @return the number of the line
 */     
    public int getNumber() {
        return (ObjectError) ? -1:this.da_object.get("number").getAsInt();
        
    }
 
    
/**
 * Returns the number of bus errors recorded on the DALI line
 * for troubleshooting
 *  
 * @version 1.0
 * @since   1.0
 * 
 * @return the number of errors on the line
 */      
    public int getBusErrors() {
        return (ObjectError) ? -1:this.da_object.get("totalBusErrors").getAsInt();
        
    }    
 
/**
 * Returns a unique id for identifying this DALI line on the connecdim cloud
 *  
 * @version 1.0
 * @since   1.0
 * 
 * @return a unique id
 */      
    public int getId() {
        return (ObjectError) ? -1:this.da_object.get("lineId").getAsInt();
        
    }       
   
/**
 * Whether the line has unaddressed devices
 *  
 * @version 1.0
 * @since   1.0
 * 
 * @return true if the line has unaddressed devices connected
 */          
    public boolean isStatus() {
        return (ObjectError) ? false:this.da_object.get("status").getAsBoolean();        
        
    }  

/**
 * Whether the line is removed from the connecdim cloud
 *  
 * @version 1.0
 * @since   1.0
 * 
 * @return true if the line is not visible on the cloud
 */              
    public boolean isRemoved() {
        return (ObjectError) ? false:this.da_object.get("removed").getAsBoolean();
        
    }  

/**
 * The last time the gateway reported to the server
 *  
 * @version 1.0
 * @since   1.0
 * 
 * @return date of the last report
 */          
    public Date getLastUpdated() {
        return json_get_date("lastUpdated");
        
    }  
   
    

    
}
