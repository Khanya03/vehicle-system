package za.ac.cput.vehiclesystem.util;

public class Helper {

    public static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }
    
    public static boolean isValidMobile(String mobile) {
        String regex = "\\d{10}";
        return mobile != null && mobile.matches(regex);
    }
}
