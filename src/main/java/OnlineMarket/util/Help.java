package OnlineMarket.util;

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class Help {

    public static boolean isInteger(String s) {
        if(s == null || s.isEmpty()) return false;
        s = s.trim();
        for(int i = 0; i < s.length(); i++) {
            if(i == 0 && s.charAt(i) == '-') {
                if(s.length() == 1) return false;
                else continue;
            }
            if(Character.digit(s.charAt(i),10) < 0) return false;
        }
        return true;
    }

    private static final NavigableMap<Long, String> suffixes = new TreeMap<>();
    static {
        suffixes.put(1_000L, "k");
        suffixes.put(1_000_000L, "M");
        suffixes.put(1_000_000_000L, "G");
        suffixes.put(1_000_000_000_000L, "T");
        suffixes.put(1_000_000_000_000_000L, "P");
        suffixes.put(1_000_000_000_000_000_000L, "E");
    }

    public static String format(long value) {
        //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
        if (value == Long.MIN_VALUE) return format(Long.MIN_VALUE + 1);
        if (value < 0) return "-" + format(-value);
        if (value < 1000) return Long.toString(value); //deal with easy case

        Map.Entry<Long, String> e = suffixes.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        long truncated = value / (divideBy / 1000); //the number part of the output times 10
        if(truncated < 100){
            boolean hasDecimal = (truncated / 10d)!= (truncated / 10);
            return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
        }else if(truncated > 100 && truncated < 1000){
            boolean hasDecimal = (truncated / 100d)!= (truncated / 100);
            return hasDecimal ? (truncated / 100d) + suffix : (truncated / 100) + suffix;
        }else{
            boolean hasDecimal = (truncated / 1000d)!= (truncated / 1000);
            return hasDecimal ? (truncated / 1000d) + suffix : (truncated / 1000) + suffix;
        }

    }

}
