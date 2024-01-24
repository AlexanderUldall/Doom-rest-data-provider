package provider.configuration;

import lombok.Getter;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Getter
public class Configuration {

    private String kickChannelId;
    private String youtubeChannelId;
    private String twitchChannelId;

    public Configuration() throws IOException {
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String appConfigPath = rootPath + "application.properties";

        Properties appProps = new Properties();
        appProps.load(new FileInputStream(appConfigPath));

        this.kickChannelId = appProps.getProperty("kickChannelId");
        this.youtubeChannelId = appProps.getProperty("youtubeChannelId");
        this.twitchChannelId = appProps.getProperty("twitchChannelId");

    }
}
