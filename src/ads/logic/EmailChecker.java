/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.logic;

import java.util.regex.Pattern;

/**
 *
 * @author mgamell
 */
public class EmailChecker {
    private static final Pattern rfc2822 = Pattern.compile(
            "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$"
    );

    public static boolean checkEmail(String email) {
        if (!rfc2822.matcher(email).matches()) {
            return false;
        } else {
            return true;
        }
    }
}
