package org.example;

import com.beust.jcommander.Strings;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TpchLineItemGenerator {
    private static String INSERT_SQL_TEMPLATE
            = "insert into lineitem(l_orderkey, l_partkey, l_suppkey, l_linenumber, l_quantity, l_extendedprice, l_discount, l_tax, l_returnflag, l_linestatus, l_shipdate, l_commitdate, l_receiptdate, l_shipinstruct, l_shipmode, l_comment) values %s;";
    private int eachInsertLines = 0;

    public TpchLineItemGenerator(int eachInsertLines) {
        this.eachInsertLines = eachInsertLines;
    }

    public List<String> getSqls(String filePath) throws IOException {
        List<String> insertSqls = new ArrayList<>();
        List<String> valuesLines = new ArrayList<>();

        int lineIndex = 0;
        LineIterator it = FileUtils.lineIterator(new File(filePath), "UTF-8");
        try {
            while (it.hasNext()) {
                ++lineIndex;
                String valuesLine = getValuesLine(it.nextLine());
                valuesLines.add(valuesLine);
                if (lineIndex % eachInsertLines == 0) {
                    insertSqls.add(String.format(INSERT_SQL_TEMPLATE, Strings.join(",", valuesLines)));
                    valuesLines.clear();
                }
            }
            if (!valuesLines.isEmpty()) {
                insertSqls.add(String.format(INSERT_SQL_TEMPLATE, Strings.join(",", valuesLines)));
            }
        } finally {
            LineIterator.closeQuietly(it);
        }

        // String fileContent = FileReader.readContent(filePath);
        // String[] lines = fileContent.split("\n");
        // List<String> insertSqls = new ArrayList<>();

        // List<String> valuesLines = new ArrayList<>();
        // for (int i = 1; i <= lines.length; i++) {
        //     String valuesLine = getValuesLine(lines[i - 1]);
        //     valuesLines.add(valuesLine);
        //     if (i % eachInsertLines == 0 || i == lines.length) {
        //         insertSqls.add(String.format(INSERT_SQL_TEMPLATE, Strings.join(",", valuesLines)));
        //         valuesLines.clear();
        //     }
        // }
        return insertSqls;
    }

    private String getValuesLine(String line) {
        String[] columnValues = line.split("\\|");
        StringBuilder sb = new StringBuilder("(");
        for (String columnValue : columnValues) {
            sb.append("'").append(columnValue).append("',");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(")");
        return sb.toString();
    }
}
