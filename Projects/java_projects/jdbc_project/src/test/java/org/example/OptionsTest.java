package org.example;

import com.beust.jcommander.JCommander;
import org.junit.Assert;
import org.junit.Test;

public class OptionsTest {
    @Test
    public void test1() {
        String[] argv = {
                "--address=182.42.233.101:9030",
                "--db=db_test",
                "--user=db",
                "--passwd=xxx",
                "--sqlfile=/home/test.sql",
                "--update"
        };
        Options options = new Options();
        JCommander.newBuilder().addObject(options).build().parse(argv);

        Assert.assertEquals("182.42.233.101:9030", options.getAddress());
        Assert.assertEquals("db_test", options.getDb());
        Assert.assertEquals("db", options.getUser());
        Assert.assertEquals("xxx", options.getPasswd());
        Assert.assertEquals("/home/test.sql", options.getSqlFile());
        Assert.assertTrue(options.isUpdate());
        Assert.assertFalse(options.isQuery());
    }
}