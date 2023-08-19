package newfarma.apinewfarma.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class Util {
	
	public static long parseDateToLong(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = sdf.parse(dateString);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }
	
	public static long reduceHoursToDate(String dateString, int hours) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 Date date = sdf.parse(dateString);
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    calendar.add(Calendar.HOUR_OF_DAY, hours);
	    return calendar.getTime().getTime();
	   
	}

    public static Map dtoTomap(Object dto){
        ObjectMapper mapObject = new ObjectMapper();
        return mapObject.convertValue(dto, Map.class);
    }
    /*public static Map dtoTomapWhereQuery(Object dto){
        ObjectMapper mapObject = new ObjectMapper();
        Map<String, Object> data  = mapObject.convertValue(dto, Map.class);
        Map<String, Object> response = new HashMap();
        for(  Map.Entry<String, Object> entry: data.entrySet() ){
            System.out.println("Key:"+entry.getKey() +", val" + entry.getValue());
            if(entry.getValue() != null && ( entry.getKey()!="offset" && entry.getKey()!="xpage" && entry.getKey()!="page")  ) {
                response.put(entry.getKey(),entry.getValue() );
            }
        }
        return response;
    }*/

}
