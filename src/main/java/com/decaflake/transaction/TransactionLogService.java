package com.decaflake.transaction;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TransactionLogService {

    private static final String TABLES_PATH = "tables";

    public int getNextVersion(String tableName) {

        File logDir = new File(
                TABLES_PATH + "/" +
                        tableName.toLowerCase() +
                        "/_decaf_log"
        );

        if (!logDir.exists()) {
            logDir.mkdirs();
            return 1;
        }

        File[] logs = logDir.listFiles((dir, name) ->
                name.endsWith(".json")
        );

        if (logs == null || logs.length == 0) {
            return 1;
        }

        int maxVersion = 0;

        for (File log : logs) {

            String name = log.getName()
                    .replace(".json", "");

            int version = Integer.parseInt(name);

            if (version > maxVersion) {
                maxVersion = version;
            }
        }

        return maxVersion + 1;
    }

    public void createInsertLog(
            String tableName,
            int version,
            String parquetFile
    ) throws IOException {

        File logFile = new File(
                TABLES_PATH + "/" +
                        tableName.toLowerCase() +
                        "/_decaf_log/" +
                        String.format("%020d.json", version)
        );

        Map<String, Object> logData = new HashMap<>();

        logData.put("version", version);
        logData.put("operation", "INSERT");
        logData.put("add", parquetFile);

        ObjectMapper mapper = new ObjectMapper();

        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(logFile, logData);
    }
}