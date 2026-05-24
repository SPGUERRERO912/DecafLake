package com.decaflake.schema;

import java.util.List;

public class TableMetadata {

    private String tableName;
    private List<ColumnMetadata> columns;

    public TableMetadata() {
    }

    public TableMetadata(String tableName, List<ColumnMetadata> columns) {
        this.tableName = tableName;
        this.columns = columns;
    }

    public String getTableName() {
        return tableName;
    }

    public List<ColumnMetadata> getColumns() {
        return columns;
    }
}