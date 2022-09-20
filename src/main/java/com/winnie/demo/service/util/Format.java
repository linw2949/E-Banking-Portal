package com.winnie.demo.service.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;

import java.text.ParseException;

@UtilityClass
public class Format {
    private static Log logger = LogFactory.getLog(Format.class);

    /**
     * @param date the date need to be converted (DD-MM-YYYY)
     * @return String converted date (YYYY-MM-DD)
     */
    public String dateFormat(String date) {
        if (logger.isDebugEnabled()) {
            JSONObject logParams = new JSONObject();
            logParams.put("date", date);

            logger.debug(logParams);
        }

        try {
            return DateFormatUtils.format(DateUtils.parseDate(date, "dd-MM-yyyy"), "yyyy-MM-dd");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
