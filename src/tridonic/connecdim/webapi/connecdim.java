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


/**
 * The main class to securely access the connecdim cloud with your account at 
 * https://api.connecdim.com/
 *
 * @author Jaie Demaagd
 * @version 1.0
 * @since   1.0
 */
public class connecdim extends connecdimApiAccessor {    

    private boolean authok;             // function to test whether the user
                                        // logs in ok  
    
/**
 * Creates a blank connecdim without username or password information
 * @version 1.0
 * @since   1.0
 */           
    public connecdim() {
        this.authok = false;            // because the user isn't logged in as no
                                        // credentials are supplied
        
        this.ObjectError = !this.authok;
    }
    
    
    
/**
 * Constructs the class using the username and password supplied for
 * authentication
 * 
 * @version 1.0
 * @since   1.0
 * 
 * @param  username your connecdim cloud email address username
 * @param  password your connecdim cloud password
 */    
    public connecdim(String username, String password) {
        this.userName = username;
        this.passKey = password;    
        
        this.authok = check_auth_ok();
        this.ObjectError = !this.authok;
        
    }
    
    
     
    private boolean check_auth_ok() {
        // builds the api call to receive dali line information and returns the result
        URL api;
        try {            
            api = new URL(this.api_url + "sites/?method=get&ident=" + this.userName + "&key=" + this.passKey + "&authOnly");
            
        } catch (MalformedURLException ex) {
            return false;
        }
        
        String shtml;
        boolean ret_value = false;

        shtml = get_web_request(api);
        
        if (shtml == "") {
            return false;
            
        } else {            
            JsonElement da_json = new JsonParser().parse(shtml);
            JsonObject da_auth = da_json.getAsJsonObject();
            ret_value = da_auth.has("success");

        }

        return ret_value;
        
    }

    
/**
 * Returns the email username set when the class was created or with
 * <code>AuthSuccess()</code>
 * 
 * @version 1.0
 * @since   1.0
 * 
 * @see     tridonic.connecdim.webapi.connecdim#AuthSuccess(java.lang.String, java.lang.String)  
 * @return The email address used when logging in
 */     
    public String getUsername() {
        return this.userName;
    }
    

/**
 * Checks your login credentials against the connecdim cloud
 *  
 * @version 1.0
 * @since   1.0
 * 
 * @return true if the username and password are valid
 */     
    public boolean AuthSuccess(){
        return this.authok;
    }

    
/**
 * Checks your login with new credentials against the connecdim cloud
 *  
 * @version 1.0
 * @since   1.0
 * 
 * @param username the email username to try
 * @param password the password to try
 * 
 * @return true if the new username and password are valid
 */     
    public boolean AuthSuccess(String username, String password) {
        this.userName = username;
        this.passKey = password;    
        
        this.authok = check_auth_ok();
        this.ObjectError = !this.authok;
        
        return AuthSuccess();
    }

    
/**
 * Checks the connecdim cloud for a gatweay with the specified MacAddress
 *  
 * @version 1.0
 * @since   1.0
 * 
 * @param gateway the macaddress of the gateway
 * 
 * @return an object that holds information about the gateway
 */     
    public connecdimGateway getGateway(String gateway) {
        return (this.authok) ? new connecdimGateway(this.userName, this.passKey, gateway):null;
    }    

    
/**
 * Checks the connecdim cloud for a DALI line attached to a gateway
 *  
 * @version 1.0
 * @since   1.0
 * 
 * @param gateway the macaddress of the gateway
 * @param linenumber the gateway line number from 1-4
 * 
 * @return an object that holds information about the DALI line
 */    
    public connecdimLine getLine(String gateway, int linenumber) {
        return (this.authok) ? new connecdimLine(this.userName, this.passKey, gateway, linenumber):null;
    }

/**
 * Gets a list of DALI Addresses that have failed lamps
 *  
 * @version 1.0
 * @since   1.0
 * 
 * @return An array list of connecdimAddresses's that have failed and are
 * attached to the site
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
            api = new URL(api_url + "devices/?method=get&ident=" + this.userName + "&key=" + this.passKey + "&filter=lampFail::true|isCurrent::true");
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

    
    
/**
 * Returns a list of sites that are accessible on the connecdim cloud
 *  
 * @version 1.0
 * @since   1.0
 * 
 * @return Array List of connecdimSite objects that are accessible via the
 * connecdim cloud
 */      
    public List<connecdimSite> getSites() {              
        
        if (this.authok){
            String APIHtml = get_sites_api();
            List<connecdimSite> sites = new ArrayList<>();
            parse_data_site_objects(APIHtml, sites);
            return sites;
        
        } else {
            return null;    // becuase if the username password was wrong there
                            // is nothing to return
        
        }
        
    }

   
    
    private String get_sites_api() {
        
        // builds the api call to receive dali line information and returns the result
        URL api = null;
        try {
            api = new URL(api_url + "sites/?method=get&ident=" + this.userName + "&key=" + this.passKey);
        } catch (MalformedURLException ex) {
            Logger.getLogger(connecdim.class.getName()).log(Level.SEVERE, null, ex);
        }
        return get_web_request(api);
        
    }

    
 
    
    private void parse_data_site_objects(String HtmlReturn, List<connecdimSite> sites) {
        
        JsonElement da_json = new JsonParser().parse(HtmlReturn);
        JsonObject da_top_object = da_json.getAsJsonObject();
        
        JsonElement da_site_element;
        JsonObject da_site_object;
        
        int i;
        
        // find and pull out the line object from JSON
        if (da_top_object.has("data")) {
            JsonArray da_array = da_top_object.getAsJsonArray("data");
            
            i = 0;
            while (i < da_array.size()) {
                da_site_element = da_array.get(i);
                da_site_object = da_site_element.getAsJsonObject();
                i++;
                
                connecdimSite xSite = new connecdimSite(this.userName, this.passKey, da_site_object);
                sites.add(xSite);
                
            }            
        }        
    }    
        
   
/**
 * Unique URL for identifying the object on the connecdim cloud
 *  
 * @version 1.0
 * @since   1.0
 * 
 * @return a unique URL
 * 
 */      
    @Override
    public String getLink() {
        return "";
    }     
    
}
