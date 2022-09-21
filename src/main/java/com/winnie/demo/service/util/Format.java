package com.winnie.demo.service.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class Format {
    private static Log logger = LogFactory.getLog(Format.class);
    private final Map<String,String> formatMap = new HashMap<>();
    static {
        formatMap.put("01", "yyyy-MM-dd");
        formatMap.put("02", "dd-MM-yyyy");
        formatMap.put("03", "yyyyMMdd");
    }
    /**
     * @param date the date need to be converted
     * @param type the format type "01": yyyyMMdd-> yyyy-MM-dd ; "02": yyyyMMdd -> dd-MM-yyyy
     * @return String converted date
     */
    public String dateFormat(String date, String type) {
        if (logger.isDebugEnabled()) {
            JSONObject logParams = new JSONObject();
            logParams.put("date", date);
            logParams.put("type", type);

            logger.debug(logParams);
        }
        try {
            Date date1 = DateUtils.parseDate(date, "yyyyMMdd");
            return DateFormatUtils.format(date1, formatMap.get(type));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * @param date the date need to be converted
     * @param type the format type "01": yyyy-MM-dd ; "02": dd-MM-yyyy ; "03": yyyyMMdd
     * @return String converted date
     */
    public String dateFormat(Date date, String type) {
        if (logger.isDebugEnabled()) {
            JSONObject logParams = new JSONObject();
            logParams.put("date", date);
            logParams.put("type", type);

            logger.debug(logParams);
        }
        return DateFormatUtils.format(date, formatMap.get(type));
    }
}
