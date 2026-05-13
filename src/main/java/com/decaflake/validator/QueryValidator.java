package com.decaflake.validator;

import com.decaflake.schema.SchemaRegistry;
import org.apache.calcite.sql.SqlIdentifier;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlSelect;

public class QueryValidator {

    private SchemaRegistry schemaRegistry;

    public QueryValidator(SchemaRegistry schemaRegistry) {
        this.schemaRegistry = schemaRegistry;
    }

    public void validate(SqlNode sqlNode) {

        if (sqlNode instanceof SqlSelect select) {

            SqlNode fromNode = select.getFrom();

            if (fromNode instanceof SqlIdentifier tableIdentifier) {
                String tableName = tableIdentifier.getSimple();

                if (!schemaRegistry.tableExists(tableName)) {
                    throw new RuntimeException(
                            "Table '" + tableName + "' does not exist."
                    );
                }
            }
        }
    }
}