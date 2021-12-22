package org.sec.util;

public class StringUtil {
    public static String cleanCommand(String cmd) {
        String temp = cmd;
        if (temp.startsWith("'") || temp.startsWith("\"")) {
            temp = temp.substring(1);
        }
        if (temp.endsWith("'") || temp.endsWith("\"")) {
            temp = temp.substring(0, temp.length() - 1);
        }
        return temp;
    }
}
