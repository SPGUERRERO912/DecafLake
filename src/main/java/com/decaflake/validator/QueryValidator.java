package com.decaflake.validator;

import com.decaflake.schema.ColumnMetadata;
import com.decaflake.schema.SchemaRegistry;
import org.apache.calcite.sql.*;

public class QueryValidator {

    private SchemaRegistry schemaRegistry;

    public QueryValidator(SchemaRegistry schemaRegistry) {
        this.schemaRegistry = schemaRegistry;
    }

    public void validate(SqlNode sqlNode) {

        if (sqlNode instanceof SqlSelect select) {

            SqlNode fromNode = select.getFrom();

            String tableName = null;

            if (fromNode instanceof SqlIdentifier tableIdentifier) {

                tableName = tableIdentifier.getSimple();

                if (!schemaRegistry.tableExists(tableName)) {
                    throw new RuntimeException(
                            "Table '" + tableName + "' does not exist."
                    );
                }
            }

            SqlNodeList selectList = select.getSelectList();

            for (SqlNode node : selectList) {

                // validate table exists
                if (node instanceof SqlIdentifier identifier) {

                    String columnName = identifier.getSimple();

                    if (identifier.isStar()) {
                        continue;
                    }

                    assert tableName != null;
                    if (!schemaRegistry.columnExists(tableName, columnName)) {

                        throw new RuntimeException(
                                "Column '" + columnName +
                                        "' does not exist in table '" +
                                        tableName + "'"
                        );
                    }
                }
            }

            SqlNode whereNode = select.getWhere();

            if (whereNode != null) {

                if (whereNode instanceof SqlBasicCall condition) {

                    SqlNode left = condition.getOperandList().get(0);
                    SqlNode right = condition.getOperandList().get(1);

                    if (left instanceof SqlIdentifier columnIdentifier) {

                        String columnName =
                                columnIdentifier.getSimple();

                        // validate column exists
                        if (!schemaRegistry.columnExists(
                                tableName,
                                columnName
                        )) {

                            throw new RuntimeException(
                                    "Column '" + columnName +
                                            "' does not exist in table '" +
                                            tableName + "'"
                            );
                        }

                        ColumnMetadata column =
                                schemaRegistry.getColumn(
                                        tableName,
                                        columnName
                                );

                        String expectedType =
                                column.getType().toUpperCase();

                        // validate literal type
                        if (right instanceof SqlLiteral literal) {

                            Object value = literal.getValue();

                            switch (expectedType) {

                                case "INT" -> {
                                    if (!(value instanceof Number)) {
                                        throw new RuntimeException(
                                                "Expected INT for column '" +
                                                        columnName + "'"
                                        );
                                    }
                                }

                                case "STRING" -> {
                                    if (!(value instanceof String)) {
                                        throw new RuntimeException(
                                                "Expected STRING for column '" +
                                                        columnName + "'"
                                        );
                                    }
                                }

                                case "DOUBLE" -> {
                                    if (!(value instanceof Number)) {
                                        throw new RuntimeException(
                                                "Expected DOUBLE for column '" +
                                                        columnName + "'"
                                        );
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}