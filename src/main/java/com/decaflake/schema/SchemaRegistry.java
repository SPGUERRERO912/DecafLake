package com.decaflake.schema;

import java.util.HashMap;
import java.util.Map;

public class SchemaRegistry {

    private Map<String, TableMetadata> tables = new HashMap<>();

    public void registerTable(TableMetadata table) {
        tables.put(table.getTableName().toLowerCase(), table);
    }

    public boolean tableExists(String tableName) {
        return tables.containsKey(tableName.toLowerCase());
    }
}