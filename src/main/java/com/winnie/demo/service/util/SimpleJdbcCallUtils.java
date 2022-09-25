package com.winnie.demo.service.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * SimpleJdbcCall Utility
 * @author Winnie Lin
 * @date 2022/09/25
 * @remark
 */
@UtilityClass
public class SimpleJdbcCallUtils {
    /** logger */
    private static Log logger = LogFactory.getLog(SimpleJdbcCallUtils.class);

    static {
        ConvertUtils.register(new LongConverter(0), Long.class);
        ConvertUtils.register(new ShortConverter(0), Short.class);
        ConvertUtils.register(new IntegerConverter(0), Integer.class);
        ConvertUtils.register(new DoubleConverter(0), Double.class);
        ConvertUtils.register(new FloatConverter(0), Float.class);
        ConvertUtils.register(new BigDecimalConverter(BigDecimal.ZERO), BigDecimal.class);
        ConvertUtils.register(new DateConverter(null), Date.class);
    }

    /**
     * Map to Java Bean, Column name->Java field name, ex. CARD_TYPE -> cardType、 USER_ID -> userId、 NAME -> name
     * @param <T> T
     * @param srcMap the source Map to transfer
     * @param clazz the class to instantiate
     * @return T the new instance java bean
     */
    @SneakyThrows
    public <T> T convertMap2Bean(Map<?, ?> srcMap, Class<T> clazz) {
        if (srcMap == null) {
            return null;
        }

        if (logger.isTraceEnabled()) {
            JSONObject logParams = new JSONObject();
            logParams.put("clazz", clazz);

            srcMap.keySet().stream().filter(row -> {
                return srcMap.get(row) != null;
            }).forEach(row -> {
                logParams.put(row.toString(),srcMap.get(row).toString());
            });

            logger.trace(logParams);
        }

        Map<String, Object> targetMap = new HashMap<String, Object>();
        srcMap.keySet().stream().forEach(columnName -> {
            targetMap.put(NamingRuleUtils.convertColumnName2FieldName((String) columnName), srcMap.get(columnName));
        });

        T result = BeanUtils.instantiateClass(clazz);
        org.apache.commons.beanutils.BeanUtils.populate(result, targetMap);

        return result;
    }

    /**
     * Map List to Bean List, Column name->Java field name, ex. CARD_TYPE -> cardType, USER_ID -> userId, NAME -> name
     * @param mapList the source Map list to transfer
     * @param clazz the class to instantiate
     * @return List<T> the new instance java bean List
     */
    @SneakyThrows
    public <T> List<T> convertMapList2BeanList(List<?> mapList, Class<T> clazz) {
        if (mapList == null) {
            return new ArrayList<T>();
        }

        return mapList.stream().map(row -> {
            return convertMap2Bean((Map<?, ?>) row, clazz);
        }).collect(Collectors.toList());
    }
}