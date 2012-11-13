
package ads.logic;

import java.util.regex.Pattern;

/**
 * Check the format of email is valid or not.
 * 
 */
public class EmailChecker {
    // define a pattern rfc2822 of email expression 
    private static final Pattern rfc2822 = Pattern.compile(
            "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$"
    );
    
  /**
   * method checkEmail()checks whether the specified string email 
   * matches the pattern expression defined.
   * @param email the string of specified input
   * @return  true if the string matches with the pattern expression; false 
   * if does not match
   */  
    public static boolean checkEmail(String email) {
        if (!rfc2822.matcher(email).matches()) {
            return false;
        } else {
            return true;
        }
    }
}
