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
import static tridonic.connecdim.webapi.connecdimApiAccessor.api_url;

/**
 * DALI gateway that is installed into a site usually created with 
 * connecdim.getGateways( <i>macaddress</i> ) or 
 * connecdimLine.getGateways().get(<i>x</i>)
 *  
 * @version 1.0
 * @since   1.0
 * 
 * @author Jaie Demaagd
 */
public class connecdimGateway extends connecdimAddressContainer {
    
    
/**
 * Constructs gateway with the macaddress specified using the username and password
 * for the connecdim cloud
 *  
 * @version 1.0
 * @since   1.0
 * 
     * @param userName  the username for verification on the cloud
     * @param passKey   the password for verification on the cloud
     * @param gatewayMac    the mac address of the gateway
 * 
 * @author Jaie Demaagd
 */    
    public connecdimGateway(String userName, String passKey, String gatewayMac)  {
        this.userName = userName;
        this.passKey = passKey;
        
        String APIHtml = get_gateway_api(gatewayMac);
        this.da_object = parse_da_object(APIHtml);
        
        this.ObjectError = !((this.da_object != null) && this.da_object.has("name"));          
        this.address_container_filter = (ObjectError) ? "" : "|gatewayMac::" + this.getMac();
                                // we check that the object exists or we
                                // will get a fail back as there will be no id field
                                // in the JSON
        
    }
    
    protected connecdimGateway(String userName, String passKey, JsonObject gatewayJSON)  {
        this.userName = userName;
        this.passKey = passKey;
        this.da_object = gatewayJSON;
        
        this.ObjectError = !((this.da_object != null) && this.da_object.has("name"));          
        this.address_container_filter = (ObjectError) ? "" : "|gatewayMac::" + this.getMac();
        
    }    
    
    

/**
 * Unique ID number that identifies the gateway 
 *  
 * @version 1.0
 * @since   1.0
 * 
 * @return unique cloud gateway ID
 * 
 */ 
    public int getId() {
        return this.da_object.get("gatewayId").getAsInt();
    }      

/**
 * 12 digit Hex Mac Adddress of the gateway 
 *  
 * @version 1.0
 * @since   1.0
 * 
 * @return 12 digit Hex Mac Adddress of the gateway 
 * 
 */     
    public String getMac() {
        return (this.ObjectError) ? null:this.da_object.get("mac").getAsString();
    }      

/**
 * Name of the gateway
 *  
 * @version 1.0
 * @since   1.0
 * 
 * @return name of the gateway as documented in the connecdim cloud
 * 
 */         
    public String getName() {
        return (this.ObjectError) ? null:this.da_object.get("name").getAsString();
    }      

/**
 * TimeZone set to the gateway used for calculating the timeoffset and daylight savings
 *  
 * @version 1.0
 * @since   1.0
 * 
 * @return The timezone setting assigned to the gateway
 * 
 */        
    public String getTimeZone() {
        return (this.ObjectError) ? null:this.da_object.get("timezone").getAsString();
    }      

/**
 * Time Offset past GMT that the box should be using
 *  
 * @version 1.0
 * @since   1.0
 * 
 * @return The time offset of the gateway
 * 
 */            
    public int getTimeOffset() {
        return (this.ObjectError) ? null:this.da_object.get("timeOffset").getAsInt();
    }      
    

/**
 * If there is an error in the reported time and timeoffset
 *  
 * @version 1.0
 * @since   1.0
 * 
 * @return true if the gateway fails a time reporting test
 * 
 */     
    public Boolean isTimeError() {
        return (this.ObjectError) ? false:this.da_object.get("timeError").getAsBoolean();
    }      

/**
 * IP address of the internet connection used at this site which is useful for
 * knowing if gateways are not on site or on a different internet connection
 *  
 * @version 1.0
 * @since   1.0
 * 
 * @return the IP Address assigned to the site by the internet provider
 * 
 */       
    public String getExternalIP() {
        return (this.ObjectError) ? null:this.da_object.get("externalIP").getAsString();
    }      

/**
 * IP address of the local network connection of the gateway which can be used
 * to assist in finding the gateway locally
 *  
 * @version 1.0
 * @since   1.0
 * 
 * @return the IP Address of the gateway on the internal local area network
 * 
 */           
    public String getInternalIP() {
        return (this.ObjectError) ? null:this.da_object.get("internalIP").getAsString();
    }      

/**
 * Whether the gateway is removed or still connected and can be used to confirm
 * that a gateway is connected to the internet
 *  
 * @version 1.0
 * @since   1.0
 * 
 * @return true if the gateway is not connected
 * 
 */      
    public Boolean isRemoved() {
        return (this.ObjectError) ? false:this.da_object.get("removed").getAsBoolean();
    }      

/**
 * The date and time that the gateway last reported to the server, used for estimating
 * how long a gateway has been removed
 *  
 * @version 1.0
 * @since   1.0
 * 
 * @return true if the gateway is not connected
 * 
 */     
    public Date getLastUpdated() {
        return (this.ObjectError) ? null:json_get_date("lastUpdated");
    }      
        
    
    
    
    
    private String get_gateway_api (String gateway) {
        
        // builds the api call to receive dali line information and returns the result
        URL api = null;
        try {
            api = new URL(this.api_url + "gateways/?method=get&ident=" + this.userName + "&key=" + this.passKey + "&filter=mac::" + gateway );
        } catch (MalformedURLException ex) {
            Logger.getLogger(connecdimLine.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("get_gateway_api::" + api.toString());
        return get_web_request(api);
        
    }     
       

/**
 * Gets the DALI Lines that are connected to a DALI gateway
 *  
 * @version 1.0
 * @since   1.0
 * 
 * @return An array list of connecdimLine's that are attached to the DALI gateway
 * 
 * @author Jaie Demaagd
 */       
    public List<connecdimLine> getLines() {              
        
        if (!ObjectError){
            String APIHtml = get_lines_api();
            List<connecdimLine> lines = new ArrayList<>();
            parse_data_line_objects(APIHtml, lines);
            return lines;
        
        } else {
            return null;    // becuase if the username password was wrong there
                            // is nothing to return
        
        }
    }    
    
    private String get_lines_api() {
        
        // builds the api call to receive dali line information and returns the result
        URL api = null;
        try {
            api = new URL(api_url + "lines/?method=get&ident=" + this.userName + "&key=" + this.passKey + "&filter=gatewayMac::" + this.getMac());
        } catch (MalformedURLException ex) {
            Logger.getLogger(connecdim.class.getName()).log(Level.SEVERE, null, ex);
        }
        return get_web_request(api);
        
    }

    
 
    
    private void parse_data_line_objects(String HtmlReturn, List<connecdimLine> lines) {
        
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
                
                connecdimLine xLine = new connecdimLine(this.userName, this.passKey, da_line_object);
                lines.add(xLine);
                
            }            
        }   
    }

   

    
    
    
}
