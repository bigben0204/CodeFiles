package org.example;

import com.beust.jcommander.JCommander;
import com.google.common.base.Strings;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

// java -jar DorisJDBC.jar --address=xxx.xxx.xxx.xxx:9030 --db=db_test --user=db --passwd=xxx --sql="show databases" --query [--configfile=config.properties]
// java -jar DorisJDBC.jar --address=xxx.xxx.xxx.xxx:9030 --db=db_test --user=db --passwd=xxx --sqlfile="/xxx/test.sql" --update [--configfile=config.properties]
public class Main {
    public static void main(String[] args) throws Exception {
        Options options = new Options();
        JCommander.newBuilder().addObject(options).build().parse(args);

        Properties config = new ResourcesLoader(getConfigFile(options.getConfigFile())).readProperties();
        try (JdbcExecutor executor = new JdbcExecutor(config, options)) {
            executor.execute(getSqls(options));
            // executeTpchInsertData(executor, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void executeTpchInsertData(JdbcExecutor executor, Options options)
            throws SQLException, InterruptedException, IOException {
        File tpchDataDir = new File(options.getSqlFile());
        File[] files = tpchDataDir.listFiles();
        TpchLineItemGenerator generator = new TpchLineItemGenerator(5);
        String[] executeSqls = {""};
        for (File file : files) {
            System.out.println(String.format("=============processing file: %s==============", file.getAbsolutePath()));
            int lineNumber = 1;

            List<String> sqls = generator.getSqls(file.getAbsolutePath());
            for (String sql : sqls) {
                System.out.println(String.format("---------------inserting lines: %d-%d---------------", lineNumber,
                        lineNumber + 4));
                lineNumber += 5;

                executeSqls[0] = sql;
                executor.execute(executeSqls);
                Thread.sleep(1000);
            }
        }
    }

    private static String[] getSqls(Options options) {
        String sqlContent = getContent(options);
        String[] sqls = sqlContent.split(";");
        return sqls;
    }

    private static String getConfigFile(String configFile) {
        if (configFile == null) {
            configFile = "/properties/config.properties";
        } else if (!new File(configFile).exists()) {
            System.err.println(String.format("config file %s does not exist", configFile));
            System.exit(1);
        }
        return configFile;
    }

    private static String getContent(Options options) {
        // use input sql if provided
        if (!Strings.isNullOrEmpty(options.getSql())) {
            return options.getSql();
        }

        return FileReader.readContent(options.getSqlFile());
    }
}