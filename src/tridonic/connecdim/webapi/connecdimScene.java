/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tridonic.connecdim.webapi;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DALI scene that is associated to a connecdim Line. Usually accessed with 
 * connecdimLine.getScene().get( <i>groupName</i> )
 *  
 * @version 1.0
 * @since   1.0
 * 
 * @author Jaie Demaagd
 */
public class connecdimScene extends connecdimApiAccessor {
    
    private int lineID;
    private int scene;
    
    
    /**
     * DALI scene that is associated to a connecdim line. Usually accessed with 
     * connecdimLine.getScene( <i>Scene Number</i> )
     *  
 * @version 1.0
 * @since   1.0
 * 
     * @param userName the username for authenticating to the cloud
     * @param passKey the password for authenticating to the cloud
     * @param lineID the unique lineId of the connecdim cloud
     * @param scene the DALI scene number with range 0-15
     * 
     */     
    public connecdimScene(String userName, String passKey, int lineID, int scene) {

        this.userName = userName;
        this.passKey = passKey;
        this.lineID = lineID;
        this.scene = scene;
        
        String APIHtml = get_scene_api(scene);
        this.da_object = parse_da_object(APIHtml);
        
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
        return (ObjectError) ? -1:this.da_object.get("sceneId").getAsInt();
    }   

/**
 * Returns the number of the scene as in the DALI standard being 0 to 15
 *  
 * @version 1.0
 * @since   1.0
 * 
 * @return dali scene number
 */        
    public int getNumber() {
        return (ObjectError) ? -1:this.da_object.get("number").getAsInt();
    }   

/**
 * The name of the scene as set in the connecdim cloud
 *  
 * @version 1.0
 * @since   1.0
 * 
 * @return dali scene name
 */            
    public String getName() {
        return (ObjectError) ? null:this.da_object.get("name").getAsString();
    }   
    
    
    
    private String get_scene_api (int scene) {
        
        URL api = null;
        
        try {
            // if its a test drop the current field out so that the test is always against the same historical row
            // otherwise the data would change each test and it would never work
            api = new URL(this.api_url + "scenes/?method=get&ident=" + this.userName + "&key=" + this.passKey + "&filter=lineId::" + this.lineID + "|number::" + scene );
        } catch (MalformedURLException ex) {
            Logger.getLogger(connecdimScene.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println(api.toString());
        return get_web_request(api);
        
    }        
 
    
    
        
          
}
