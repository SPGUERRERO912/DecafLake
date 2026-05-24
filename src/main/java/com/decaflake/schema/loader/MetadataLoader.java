package com.decaflake.schema.loader;

import com.decaflake.schema.SchemaRegistry;
import com.decaflake.schema.TableMetadata;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;

public class MetadataLoader {

    private static final String METADATA_PATH = "metadata";

    public void loadSchemas(SchemaRegistry registry)
            throws Exception {

        File folder = new File(METADATA_PATH);

        ObjectMapper mapper = new ObjectMapper();

        File[] files = folder.listFiles();

        if (files == null) {
            return;
        }

        for (File file : files) {

            if (file.getName().endsWith(".json")) {

                TableMetadata table =
                        mapper.readValue(
                                file,
                                TableMetadata.class
                        );

                registry.registerTable(table);

                System.out.println(
                        "Loaded table: " +
                                table.getTableName()
                );
            }
        }
    }
}