package util;

import java.util.regex.Pattern;

public class Validator {
    private static final Pattern MOBILE_PATTERN = Pattern.compile("^[6-9][0-9]{9}$");

    public static boolean isValidMobile(String value) {
        return value != null && MOBILE_PATTERN.matcher(value).matches();
    }
}
