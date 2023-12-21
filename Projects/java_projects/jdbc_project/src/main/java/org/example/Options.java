package org.example;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import lombok.Data;

@Data
@Parameters(separators = "=")
public class Options {
    @Parameter(names = "--address", description = "Level of verbosity")
    private String address;

    @Parameter(names = "--db", description = "datebase for connection, can be empty")
    private String db = "";

    @Parameter(names = "--user", description = "user for connection", required = true)
    private String user;

    @Parameter(names = "--passwd", description = "password for connection", required = true)
    private String passwd;

    @Parameter(names = "--sqlfile", description = "sql file to execute")
    private String sqlFile;

    @Parameter(names = "--sql", description = "sql to execute")
    private String sql;

    @Parameter(names = "--configfile", description = "config file")
    private String configFile;

    @Parameter(names = {"-q", "--query"}, description = "set for query sentence with output")
    private boolean query = false;

    @Parameter(names = {"-u", "--update"}, description = "set for update sentence without output")
    private boolean update = false;

    @Parameter(names = {"-p", "--printlog"}, description = "set for print sql and result")
    private boolean printLog = false;
}
