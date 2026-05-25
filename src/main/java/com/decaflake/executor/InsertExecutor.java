package com.decaflake.executor;

import com.decaflake.parquet.ParquetService;
import com.decaflake.transaction.TransactionLogService;
import org.apache.calcite.sql.SqlBasicCall;
import org.apache.calcite.sql.SqlInsert;
import org.apache.calcite.sql.SqlLiteral;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlNodeList;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class InsertExecutor {

    private final TransactionLogService logService =
            new TransactionLogService();

    private final ParquetService parquetService =
            new ParquetService();

    public void execute(SqlNode sqlNode) throws Exception {

        if (!(sqlNode instanceof SqlInsert insert)) {
            return;
        }

        String tableName =
                insert.getTargetTable().toString();

        SqlBasicCall valuesCall =
                (SqlBasicCall) insert.getSource();

        SqlBasicCall row =
                (SqlBasicCall) valuesCall.operand(0);

        List<String> values = new ArrayList<>();

        for (SqlNode value : row.getOperandList()) {

            SqlLiteral literal = (SqlLiteral) value;

            values.add(
                    literal.getValue().toString()
            );
        }

        int version =
                logService.getNextVersion(tableName);

        String parquetName =
                String.format(
                        "part-%04d.parquet",
                        version
                );

        String parquetPath =
                "tables/" +
                        tableName.toLowerCase() +
                        "/data/" +
                        parquetName;

        File dataDir = new File(
                "tables/" +
                        tableName.toLowerCase() +
                        "/data"
        );

        dataDir.mkdirs();

        parquetService.writeParquet(
                parquetPath,
                values
        );

        logService.createInsertLog(
                tableName,
                version,
                parquetName
        );

        System.out.println(
                "Inserted successfully!"
        );
    }
}