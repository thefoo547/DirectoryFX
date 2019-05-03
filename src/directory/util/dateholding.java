package directory.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class dateholding {
    private static final String DATE_FORMAT="dd.MM.yy";
    private static final DateTimeFormatter dtf=DateTimeFormatter.ofPattern(DATE_FORMAT);

    public static String format(LocalDate t){
        if(t==null){
            throw new NullPointerException("Error de manejo en fechas");
        }
        return dtf.format(t);
    }

    public static LocalDate parse (String t) throws DateTimeParseException {
        return dtf.parse(t, LocalDate::from);
    }
    public static boolean validDate(String dateString) {
        // Try to parse the String.
        return dateholding.parse(dateString) != null;
    }
}
