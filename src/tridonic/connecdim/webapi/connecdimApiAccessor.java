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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

/**
 * Contains common methods that are refactored from classes for connecting to the 
 * connecdim cloud
 *  
 * @version 1.0
 * @since   1.0
 * 
 * @author Jaie Demaagd
 */
class connecdimApiAccessor {
    
    protected static final String api_url = "https://api.connecdim.com/api/"; 

    protected String userName;             // because we need to generate URL's
                                        // for the API that include these
                                        // credentials
    protected String passKey;    
    
    protected boolean ObjectError;             // used so that we don't generate
                                            // errors when we have to parse
                                            // null json when an object does
                                            // not exist 
    
    protected JsonObject da_object;            // cache of the JSON object that this
                                            // class is based on
    

    
    /**
     * Used to know if any of the other fields contain meaningful information
     *  
 * @version 1.0
 * @since   1.0
 * 
     * @return true if the connecdim cloud returned information
     */       
    public boolean isValid() {
        return !ObjectError;
        
    }     
    
    //////////////////////////////////////////////////////////////////////////
    //                                                                      //
    //  takes care of reading the api over https                            //
    //  so that java doesn't freak out                                      //
    //                                                                      //
    protected String get_web_request(URL wwwRequest) {

       String website_html="";
       
        try {
            
            System.setProperty("jsse.enableSNIExtension", "false");     // because without this a
                                                                        // servername exception is raised
                                                                        // and java wont process the
            HttpsURLConnection con = (HttpsURLConnection)wwwRequest.openConnection();
            
            BufferedReader br = 
		new BufferedReader(
			new InputStreamReader(con.getInputStream()));
            
            website_html = br.readLine();

	   br.close();            
            
            
       } catch (IOException ex) {
            return "";          // this occurs when the username password
                                // fails and we get a http 403 error back
       }
       
       return website_html;
        
    } 
    
    
    //
    //  because each API call is returned in a data object this function
    //
    protected JsonObject parse_da_object(String HtmlReturn) {
        JsonObject result = null;
        
        JsonElement da_json = new JsonParser().parse(HtmlReturn);
        JsonObject da_top_object = da_json.getAsJsonObject();
        
        // find and pull out the line object from JSON
        if (da_top_object.has("data")) {
            JsonArray da_array = da_top_object.getAsJsonArray("data");
            
            if (da_array.size() >= 1) {
                da_json = da_array.get(0);
                result = da_json.getAsJsonObject();
                
            }                         
        }
            
        return result;        
        
    }       
    
    
    
    //////////////////////////////////////////////////////////////////////////
    //                                                                      //
    //  Because the api returns the date in UTC we convert that into a date //
    //      object to make it a nicer response                              //
    //                                                                      //
    protected Date json_get_date(String JsonName) {
       
        int TimeAsStamp;
        Date result = null;
                        
        if ((this.da_object != null) && this.da_object.has(JsonName)) {
            TimeAsStamp = this.da_object.get(JsonName).getAsInt();
            result = new Date(TimeAsStamp * 1000L);
        }
            
        return result;
        
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
    public String getLink() {
        return (this.ObjectError) ? null:this.da_object.get("link").getAsString();
    }     
    
}
