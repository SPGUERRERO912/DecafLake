package com.decaflake.parser;

import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.parser.SqlParser;

public class QueryParser {

    public SqlNode parse(String query) throws Exception {
        SqlParser parser = SqlParser.create(query);
        return parser.parseQuery();
    }
}