package tridonic.connecdim.webapi;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DALI group that is associated to a connecdim Line. Usually accessed with 
 * connecdimLine.getGroup().get( <i>groupName</i> )
 * 
 * @author Jaie Demaagd
 */
public class connecdimGroup extends connecdimApiAccessor {

    private int lineID;
    private int group;
    
    /**
     * Constructor used with the group and lineId associated with the cloud
     * 
     * @param userName the username for authenticating to the cloud
     * @param passKey the password for authenticating to the cloud
     * @param lineID the unique lineId of the connecdim cloud
     * @param group the DALI group number with range 0-15
     * 
     */    
    public connecdimGroup(String userName, String passKey, int lineID, int group) {

        this.userName = userName;
        this.passKey = passKey;
        this.lineID = lineID;
        this.group = group;
        
        String APIHtml = get_group_api(group);
        this.da_object = parse_da_object(APIHtml);
        
        ObjectError = !((this.da_object != null) && this.da_object.has("name"));        
        
    }    

   

/**
 * Unique ID number that identifies the group 
 *  
 * @version 1.0
 * @since   1.0
 * 
 * @return unique cloud group ID
 * 
 */ 
    public int getId() {
        return (ObjectError) ? -1:this.da_object.get("groupId").getAsInt();
    }    

/**
 * DALI group number from 0 to 15
 *  
 * @version 1.0
 * @since   1.0
 * 
 * @return DALI group number
 * 
 */     
    public int getNumber() {
        return (ObjectError) ? -1:this.da_object.get("number").getAsInt();
    }    
    
/**
 * Name of the group as set on the DALI Cloud
 *  
 * @version 1.0
 * @since   1.0
 * 
 * @return the dali group name
 * 
 */     
    public String getName() {
        return (ObjectError) ? null:this.da_object.get("name").getAsString();
    }  
    
    
/**
 * The area of the room in m2 useful for calculating space and energy metrics
 *  
 * @version 1.0
 * @since   1.0
 * 
 * @return area in m2
 * 
 */     
    public int getArea() {
        return (ObjectError) ? -1:this.da_object.get("area").getAsInt();
    }        
    
    
    private String get_group_api (int group) {
        
        URL api = null;
        
        try {
            api = new URL(this.api_url + "groups/?method=get&ident=" + this.userName + "&key=" + this.passKey + "&filter=lineId::" + this.lineID + "|number::" + group );
        } catch (MalformedURLException ex) {
            Logger.getLogger(connecdimGroup.class.getName()).log(Level.SEVERE, null, ex);
        }
        return get_web_request(api);
        
    }        
      
}
