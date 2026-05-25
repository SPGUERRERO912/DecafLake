package com.decaflake.parquet;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;

import org.apache.parquet.avro.AvroParquetWriter;
import org.apache.parquet.io.OutputFile;
import org.apache.parquet.io.PositionOutputStream;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ParquetService {

    public void writeParquet(
            String filePath,
            List<String> values
    ) throws Exception {

        String schemaStr = """
        {
          "type": "record",
          "name": "Row",
          "fields": [
            {"name": "ID", "type": "int"},
            {"name": "NAME", "type": "string"}
          ]
        }
        """;

        Schema schema = new Schema.Parser()
                .parse(schemaStr);

        OutputFile outputFile =
                new LocalOutputFile(
                        new File(filePath)
                );

        var writer =
                AvroParquetWriter
                        .<GenericRecord>builder(outputFile)
                        .withSchema(schema)
                        .build();

        GenericRecord record =
                new GenericData.Record(schema);

        record.put(
                "ID",
                Integer.parseInt(values.get(0))
        );

        record.put(
                "NAME",
                values.get(1)
        );

        writer.write(record);

        writer.close();
    }

    static class LocalOutputFile
            implements OutputFile {

        private final File file;

        public LocalOutputFile(File file) {
            this.file = file;
        }

        @Override
        public PositionOutputStream create(
                long blockSizeHint
        ) throws IOException {

            return new LocalPositionOutputStream(
                    new FileOutputStream(file)
            );
        }

        @Override
        public PositionOutputStream createOrOverwrite(
                long blockSizeHint
        ) throws IOException {

            return create(blockSizeHint);
        }

        @Override
        public boolean supportsBlockSize() {
            return false;
        }

        @Override
        public long defaultBlockSize() {
            return 0;
        }
    }

    static class LocalPositionOutputStream
            extends PositionOutputStream {

        private final FileOutputStream out;
        private long position = 0;

        public LocalPositionOutputStream(
                FileOutputStream out
        ) {
            this.out = out;
        }

        @Override
        public long getPos() {
            return position;
        }

        @Override
        public void write(int b)
                throws IOException {

            out.write(b);
            position++;
        }

        @Override
        public void close()
                throws IOException {

            out.close();
        }
    }
}