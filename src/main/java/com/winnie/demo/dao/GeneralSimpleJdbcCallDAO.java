package com.winnie.demo.dao;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * General SimpleJdbcCall Data Access Object for all procedure/function call
 *
 * @author Winnie Lin
 * @date 2020/09/25
 * @remark
 */
@Repository
public class GeneralSimpleJdbcCallDAO {
    private static Log logger = LogFactory.getLog(GeneralSimpleJdbcCallDAO.class);

    private ConcurrentHashMap<String, SimpleJdbcCall> simpleJdbcCallPool = new ConcurrentHashMap<String, SimpleJdbcCall>();

    @Autowired
    private DataSource dataSource;

    // 1. Stored procedure -----------------------------------------------------------------------------------------

    /**
     * Execute the Stored procedure with schemaName、package and parameters, specify the name of the catalog that contains the stored procedure.
     * <p>
     * To provide consistency with the Oracle DatabaseMetaData, this is used to specify the package name if the procedure is declared as part of a package.
     *
     * @param procedureName   the name of the stored procedure
     * @param parameterSource the SqlParameterSource containing the parameter values to be used in the call
     * @return a Map of output params
     */
    public Map<String, Object> doCallProcedure(String procedureName, SqlParameterSource parameterSource) {
        JSONObject logParams = new JSONObject();

        if (logger.isDebugEnabled()) {
            logParams.put("procedureName", procedureName);

            Arrays.stream(Optional.ofNullable(parameterSource.getParameterNames()).orElse(new String[]{})).filter(row -> {
                return parameterSource.getValue(row) != null;
            }).forEach(row -> {
                logParams.put(row, StringUtils.abbreviate(parameterSource.getValue(row).toString(), 103));
            });
        }

        Instant start = Instant.now();
        try {
            // 1. get Procedure unique name --------------------------------------------------------------------------
            String name = new StringBuilder()
                    .append(procedureName)
                    .append("(")
                    .append(StringUtils.join(parameterSource.getParameterNames(), ", "))
                    .append(")")
                    .toString();

            // 2. Get SimpleJdbcCall from pool ---------------------------------------------------------------------------
            SimpleJdbcCall simpleJdbcCall = simpleJdbcCallPool.computeIfAbsent(name, (key) -> {
                return new SimpleJdbcCall(dataSource).withProcedureName(procedureName);
            });

            // 3. execute DB Stored function ------------------------------------------------------------------------------
            return simpleJdbcCall.execute(parameterSource);

        } finally {
            if (logger.isDebugEnabled()) {
                logParams.put("elapsedTime", Duration.between(start, Instant.now()).toMillis());
                logger.debug(logParams);
            }
        }
    }

    /**
     * Execute the Stored procedure without parameters
     *
     * @param procedureName the name of the stored procedure
     * @return a Map of output params
     */
    public Map<String, Object> doCallProcedure(String procedureName) {
        if (logger.isDebugEnabled()) {
            logger.debug(new JSONObject());
        }

        return doCallProcedure(procedureName, EmptySqlParameterSource.INSTANCE);
    }
}