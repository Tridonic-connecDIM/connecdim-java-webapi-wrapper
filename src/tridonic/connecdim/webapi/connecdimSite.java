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
 * DALI site which is the physical location of the building that is associated 
 * with the connecdim cloud. Usually accessed with connecdim.getSites().get( <i>x</i> )
 *  
 * @version 1.0
 * @since   1.0
 * 
 * @author Jaie Demaagd
 */
public class connecdimSite extends connecdimAddressContainer{
   
    protected connecdimSite(String userName, String passKey, JsonObject SiteObject) {
        
        this.userName = userName;
        this.passKey = passKey;

        this.da_object = SiteObject;
        ObjectError = !((this.da_object != null) && this.da_object.has("name"));
        
    }      


    
/**
 * A unique id of the scene as assigned by the connecdim cloud
 *  
 * @version 1.0
 * @since   1.0
 * 
 * @return dali scene unique id
 */   
    public int getId() {
        return (ObjectError) ? -1:this.da_object.get("siteId").getAsInt();
    }    

/**
 * The name of the site as set in the connecdim cloud
 *  
 * @version 1.0
 * @since   1.0
 * 
 * @return site name
 */      
    public String getName() {
        return (ObjectError) ? null:this.da_object.get("name").getAsString();
    }  
    
    
/**
 * The street address of the site as set in the connecdim cloud
 *  
 * @version 1.0
 * @since   1.0
 * 
 * @return site street
 */      
    public String getStreet() {
        return (ObjectError) ? null:this.da_object.get("street").getAsString();
    }    

/**
 * The city name of the site as set in the connecdim cloud
 *  
 * @version 1.0
 * @since   1.0
 * 
 * @return city name
 */    
    public String getCity() {
        return (ObjectError) ? null:this.da_object.get("city").getAsString();
    }   
    
/**
 * The state name of the site as set in the connecdim cloud
 *  
 * @version 1.0
 * @since   1.0
 * 
 * @return state name
 */     
    public String getState() {
        return (ObjectError) ? null:this.da_object.get("state").getAsString();
    }   
    
/**
 * The country name of the site as set in the connecdim cloud
 *  
 * @version 1.0
 * @since   1.0
 * 
 * @return country name
 */     
    public String getCountry() {
        return (ObjectError) ? null:this.da_object.get("country").getAsString();
    }    
    
/**
 * The post code of the site as set in the connecdim cloud
 *  
 * @version 1.0
 * @since   1.0
 * 
 * @return post code
 */     
    public String getPostCode() {
        return (ObjectError) ? null:this.da_object.get("postcode").getAsString();
    }    
   
/**
 * Whether the DALI address numbering should start from 0 or 1
 *  
 * @version 1.0
 * @since   1.0
 * 
 * @return 0 or 1 address numbering offset
 */     
    public int getAddressBase() {
        // bitwise X 0 0
        int base = json_get_int("formatSettings");
        return base & 1;
    }   

/**
 * Whether the DALI group numbering should start from 0 or 1
 *  
 * @version 1.0
 * @since   1.0
 * 
 * @return 0 or 1 group numbering offset
 */         
    public int getGroupBase() {
        // bitwise 0 X 0
        int base = json_get_int("formatSettings");
        int answer = base & 2;
        return (answer == 2) ? 1 : 0;

    }  
    
/**
 * Whether the DALI scene numbering should start from 0 or 1
 *  
 * @version 1.0
 * @since   1.0
 * 
 * @return 0 or 1 scene numbering offset
 */         
    public int getSceneBase() {
        // bitwise 0 0 X
        int base = json_get_int("formatSettings");
        int answer = base & 4;
        return (answer == 4) ? 1 : 0;
    }  

     /**
     * DALI Gateways that are associated with this site
     *  
 * @version 1.0
 * @since   1.0
 * 
     * @return Array list of connecdimGateway objects
     * 
     */        
    public List<connecdimGateway> getGateways() {              
        
        if (!ObjectError){
            String APIHtml = get_gateways_api();
            List<connecdimGateway> gateways = new ArrayList<>();
            parse_data_gateway_objects(APIHtml, gateways);
            return gateways;
        
        } else {
            return null;    // becuase if the username password was wrong there
                            // is nothing to return
        
        }
    }    

    
    private void parse_data_gateway_objects(String HtmlReturn, List<connecdimGateway> gateways) {
        
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
                
                connecdimGateway xGateway = new connecdimGateway(this.userName, this.passKey, da_object);
                gateways.add(xGateway);
                
            }            
        }   
    }    
    
    private String get_gateways_api() {
        
        // builds the api call to receive dali line information and returns the result
        URL api = null;
        try {
            api = new URL(api_url + "gateways/?method=get&ident=" + this.userName + "&key=" + this.passKey + "&filter=siteId::" + this.getId());
        } catch (MalformedURLException ex) {
            Logger.getLogger(connecdim.class.getName()).log(Level.SEVERE, null, ex);
        }
        return get_web_request(api);
        
    }

    
    //////////////////////////////////////////////////////////////////////////
    //                                                                      //
    //  Returns the number of an int element                                //
    //                                                                      //
    private int json_get_int(String JsonName) {
       
        int result = -1;
                        
        if ((this.da_object != null) && this.da_object.has(JsonName)) {
            result= this.da_object.get(JsonName).getAsInt();
        }
            
        return result;
        
    }    

    
}
