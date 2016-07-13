package ml.chromaryu.IrunaReloaded;

import ml.chromaryu.IrunaReloaded.Listeners.BotCommandListener;
import ml.chromaryu.IrunaReloaded.Listeners.GameListener;
import ml.chromaryu.IrunaReloaded.Listeners.MessageListener;
import ml.chromaryu.IrunaReloaded.threads.currenttimethread;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.Listener;
import org.pircbotx.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

/**
 * Created by chroma on 16/06/22.
 */
public class Main {
    public static currenttimethread th;
    public static Properties configuration = new Properties();
    public static Logger logger = LoggerFactory.getLogger(Main.class);
    public static String path = System.getProperty("user.dir");
    public static void main(String[] args) throws Exception {
        //Logger logger = LoggerFactory.getLogger(Main.class);
        List<String> chanList = new ArrayList<>();
        InputStream is = new FileInputStream(new File(path + "/Config.properties"));
        configuration.load(is);
        String[] chan = configuration.getProperty("AutoJoinChannels").split(",");
        Collections.addAll(chanList, chan); //Nailed and lol
        Configuration.Builder conf = botEssential.CreateConfig(configuration.getProperty("BotName"), configuration.getProperty("Server"), chanList, configuration.getProperty("realname"), configuration.getProperty("password"));
        //List<Listener> Listenerlist = new ArrayList<>();
        conf.addListener(new BotCommandListener());
        conf.addListener(new GameListener());
        conf.addListener(new MessageListener());
        Configuration c = conf.buildConfiguration();
        th = new currenttimethread();
        th.start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            th.sd = true;
        }, "Shutdown-thread"));
        //logger.
        PircBotX bot = new PircBotX(c);
        bot.startBot();
    }
}
