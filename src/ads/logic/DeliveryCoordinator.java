/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.logic;

import ads.resources.data.Delivery;
import java.util.ArrayList;

/**
 *
 * @author MFA
 */
public class DeliveryCoordinator {
    private ArrayList<Delivery> pendingDeliveries;
    public DeliveryCoordinator()
    {
        pendingDeliveries = new ArrayList<Delivery>();
    }
    
    public void bookDelivery(String urgency, ArrayList<String[]> targetList)
    {
        //TODO: Assign delivery to the ADSuser
            // First add the deliveries to the user in the userlist
        
        //Add the assigned deliveries to the pending deliveries
        for(int i=0; i<targetList.size(); i++) {
            Delivery newDel=new Delivery(targetList.get(i)[1], urgency,targetList.get(i)[0]);
            pendingDeliveries.add(newDel);
        }
    }
    
}
