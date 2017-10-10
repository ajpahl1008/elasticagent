
import co.elastic.agent.models.configuration.Configuration;

import org.junit.Assert;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ElasticAgentTest {

    @Test
    public void ReadConfigFileForUrl() throws Exception {
        Yaml yaml = new Yaml();
        InputStream in = Files.newInputStream(Paths.get("src/test/resources/elasticagent.yml"));
        Configuration config = yaml.loadAs(in, Configuration.class);
        Assert.assertEquals("http://192.168.0.214:8200", config.getConnection().getUrl());
    }

    @Test
    public void ReadConfigFileForPackageWithJDK() throws Exception {
        Yaml yaml = new Yaml();
        InputStream in = Files.newInputStream(Paths.get("src/test/resources/elasticagent.yml"));
        Configuration config = yaml.loadAs(in, Configuration.class);
        Assert.assertEquals("jdk", config.getSkip().get(2)); //should be the 3rd package in the list
    }

    @Test
    public void ReadConfigAndCheckForPackage() throws Exception {
        Yaml yaml = new Yaml();
        InputStream in = Files.newInputStream(Paths.get("src/test/resources/elasticagent.yml"));
        Configuration config = yaml.loadAs(in, Configuration.class);
        int location = config.getSkip().indexOf("jdk");
        Assert.assertEquals(location, 2); //should be the 3rd package in the list
    }

    @Test
    public void ReadConfigAndDoesntContainPackage() throws Exception {
        Yaml yaml = new Yaml();
        InputStream in = Files.newInputStream(Paths.get("src/test/resources/elasticagent.yml"));
        Configuration config = yaml.loadAs(in, Configuration.class);
        int location = config.getSkip().indexOf("dude");
        Assert.assertEquals(location, -1); //should be the 3rd package in the list
    }

}
