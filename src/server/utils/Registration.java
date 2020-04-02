package utils;
import utils.Tokens;

import java.util.Date;

/**
 * Handles registration and login system, with the additional
 * functionality of getting user details given a token from
 * the client.
 */
public class Registration {
    /**
     * Implements register functionality.
     * @param name - String, User's name
     * @param sdate - Date, user's start date
     * @param salary - int, user's salary
     * @param periodicity - int, Periodicity of pay
     * @param pwd - String, the user password
     * @return token - String?, a token if success, else null
     */
    public static String register(String name, Date sdate, int salary, int periodicity, String pwd) {
        // Generate a username. TODO: Need a util.

        // Input validation. TODO: Need a util

        // Try insert

        // Return token
    }

    /**
     * Implement login functionality
     * @param username - String, username
     * @param pwd - String, password
     * @return token - String?, a token if success, else null
     */
    public static String login(String username, String pwd) {
        // Input validation

        // Authorize with DB

        // Return token or null
    }
}
