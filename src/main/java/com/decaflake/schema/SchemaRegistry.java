package com.decaflake.schema;

import java.util.HashMap;
import java.util.Map;

public class SchemaRegistry {

    private Map<String, TableMetadata> tables = new HashMap<>();

    public ColumnMetadata getColumn(
            String tableName,
            String columnName
    ) {

        TableMetadata table =
                tables.get(tableName.toLowerCase());

        if (table == null) {
            return null;
        }

        return table.getColumns()
                .stream()
                .filter(column ->
                        column.getName()
                                .equalsIgnoreCase(columnName)
                )
                .findFirst()
                .orElse(null);
    }

    public void registerTable(TableMetadata table) {
        tables.put(table.getTableName().toLowerCase(), table);
    }

    public boolean tableExists(String tableName) {
        return tables.containsKey(tableName.toLowerCase());
    }

    public boolean columnExists(String tableName, String columnName) {

        TableMetadata table =
                tables.get(tableName.toLowerCase());

        if (table == null) {
            return false;
        }

        return table.getColumns()
                .stream()
                .anyMatch(column ->
                        column.getName()
                                .equalsIgnoreCase(columnName)
                );
    }
}