package com.decaflake.cli;

import com.decaflake.executor.InsertExecutor;
import com.decaflake.parser.QueryParser;
import com.decaflake.schema.SchemaRegistry;
import com.decaflake.schema.loader.MetadataLoader;
import com.decaflake.validator.QueryValidator;
import org.apache.calcite.sql.SqlNode;

import java.util.Scanner;

public class QueryCLI {

    public void start() throws Exception {
        Scanner scanner = new Scanner(System.in);

        SchemaRegistry schemaRegistry = new SchemaRegistry();

        //load data from metadata

        MetadataLoader loader = new MetadataLoader();
        loader.loadSchemas(schemaRegistry);

        QueryParser parser = new QueryParser();
        QueryValidator validator = new QueryValidator(schemaRegistry);

        System.out.println("Welcome to DecafLake");
        System.out.println("Type 'exit' to quit");

        while (true) {
            System.out.print("decaf> ");
            String query = scanner.nextLine();

            if (query.equalsIgnoreCase("exit")) {
                break;
            }

            try {
                SqlNode sqlNode = parser.parse(query);

                validator.validate(sqlNode);

                System.out.println("Query is valid!");
                System.out.println(sqlNode);

                InsertExecutor insertExecutor =
                        new InsertExecutor();

                insertExecutor.execute(sqlNode);

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        scanner.close();
    }
}