/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.logic;

import javax.mail.MessagingException;
import ads.resources.datacontroller.UserController;
/**
 *
 * @author MFA
 */
public class NotificationCoordinator {

    public NotificationCoordinator() {
    }
    public static void notifyReceiver_DeliveryBooked(String receiverName, String receiverAddress, String bookingDateAndTime) throws MessagingException
    {
        String subject="You have a delivery booked !";
        String messageText="Hi "+receiverName+"\nYou have a delivery booked at time "
                            +bookingDateAndTime+"\nRegards.\nAuto Delivery System Admin.";
        
        new Email().send(receiverAddress, subject, messageText, null);
    }
    public static void notifySender_DeliveryDelivered(String senderName, String senderAddress, String bookingDateAndTime) throws MessagingException
    {
        String subject="Your delivery is booked !";
        String messageText="Hi "+senderName+"\nYour delivery which are booked at "
                           +bookingDateAndTime+"are successfully delivered."+
                           "\nRegards.\nAuto Delivery System Admin.";
        
        new Email().send(senderAddress, subject, messageText, null);
    }
            
}
