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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static tridonic.connecdim.webapi.connecdimApiAccessor.api_url;

/**
 * Parent class with methods that grab multiple addresses
 * 
 * @version 1.0
 * @since   1.0
 * 
 * @author jaie
 */

class connecdimAddressContainer extends connecdimApiAccessor {
    
    protected String address_container_filter = ""; // so that we can filter
                                                    // returned addresses
    
    
/**
 * Gets a list of DALI Addresses that have failed lamps
 *  
 * @version 1.0
 * @since   1.0
 * 
 * @return An array list of connecdimAddresses's that have failed and are
 * attached to the DALI gateway
 * 
 */           
    public List<connecdimAddress> getFailedDevices() {              
        
        if (!ObjectError){
            String APIHtml = get_failed_devices_api();
            List<connecdimAddress> devices = new ArrayList<>();
            parse_data_address_objects(APIHtml, devices);
            return devices;
        
        } else {
            return null;    // becuase if the username password was wrong there
                            // is nothing to return
        
        }
    }    
    
    private String get_failed_devices_api() {
        
        // builds the api call to receive dali line information and returns the result
        URL api = null;
        try {
            api = new URL(api_url + "devices/?method=get&ident=" + this.userName + "&key=" + this.passKey + "&filter=lampFail::true|isCurrent::true" + this.address_container_filter);
        } catch (MalformedURLException ex) {
            Logger.getLogger(connecdim.class.getName()).log(Level.SEVERE, null, ex);
        }
        return get_web_request(api);

    }    
 

    
    private void parse_data_address_objects(String HtmlReturn, List<connecdimAddress> devices) {
        
        JsonElement da_json = new JsonParser().parse(HtmlReturn);
        JsonObject da_top_object = da_json.getAsJsonObject();
        
        JsonElement da_element;
        JsonObject da_object;
        
        int i;
        
        // find and pull out the line object from JSON
        if (da_top_object.has("data")) {
            JsonArray da_array = da_top_object.getAsJsonArray("data");
            
            i = 0;
            while (i < da_array.size()) {
                da_element = da_array.get(i);
                da_object = da_element.getAsJsonObject();
                i++;
                
                connecdimAddress xDevice = new connecdimAddress(this.userName, this.passKey, da_object);
                devices.add(xDevice);
                
            }            
        }   
    }     
    
}
