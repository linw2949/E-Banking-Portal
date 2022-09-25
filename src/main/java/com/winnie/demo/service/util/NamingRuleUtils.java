package com.winnie.demo.service.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;

import java.util.StringTokenizer;

/**
 * Naming Rule Utility
 * @author Winnie Lin
 */
@UtilityClass
public class NamingRuleUtils {
    /** logger */
    private static Log logger = LogFactory.getLog(NamingRuleUtils.class);

    /**
     * Transfer table's column name to Java filed name, ex. CARD_TYPE -> cardType、 USER_ID -> userId、 NAME -> name
     * @param name column
     * @return String field name
     */
    public String convertColumnName2FieldName(String name) {
        if (logger.isTraceEnabled()) {
            JSONObject logParams = new JSONObject("name", name);
            logger.trace(logParams);
        }

        StringBuilder sb = new StringBuilder();

        String[] tokens = StringUtils.split(name, "_");
        if (tokens.length > 1 && tokens[0].length() == 1) {
            for (String token : tokens) {
                sb.append(WordUtils.capitalizeFully(token));
            }
            return sb.toString();
        }

        StringTokenizer parser = new StringTokenizer(name, "_");
        boolean isFirst = true;
        while (parser.hasMoreTokens()) {
            String s = parser.nextToken().toLowerCase();
            if (isFirst) {
                sb.append(s);
                isFirst = false;
            } else {
                char c = Character.toUpperCase(s.charAt(0));
                sb.append(c);
                sb.append(s.substring(1, s.length()));
            }
        }

        return sb.toString();
    }
}