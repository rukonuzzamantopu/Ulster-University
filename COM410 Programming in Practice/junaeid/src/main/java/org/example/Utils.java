package org.example;

public class Utils {
    public static int safeParseInt(String s, int defaultVal) {
        if (s == null) return defaultVal;
        s = s.trim();
        if (s.isEmpty()) return defaultVal;
        try { return Integer.parseInt(s); } catch (Exception e) { return defaultVal; }
    }
}

