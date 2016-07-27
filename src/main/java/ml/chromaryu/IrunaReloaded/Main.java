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
    public static File configfile = new File(path + "/Config.properties");
    public static void main(String[] args) throws Exception {
        //Logger logger = LoggerFactory.getLogger(Main.class);
        List<String> chanList = new ArrayList<>();
        InputStream is;
        if(!configfile.exists()) {
            if (configfile.createNewFile()) {
                botEssential.writeConfig(configfile);
                logger.warn("configfile has been made, edit file!");
                System.exit(0);
            }
        }
        is = new FileInputStream(configfile);
        logger.info("configuration loaded.");
        configuration.load(is);
        String[] chan = configuration.getProperty("AutoJoinChannels").split(",");
        Collections.addAll(chanList, chan); //Nailed and lol
        Configuration.Builder conf = botEssential.CreateConfig(configuration.getProperty("BotName"), configuration.getProperty("Server"), chanList, configuration.getProperty("realname"), configuration.getProperty("password"));
        //List<Listener> Listenerlist = new ArrayList<>();
        conf.addListener(new BotCommandListener());
        conf.addListener(new GameListener());
        conf.addListener(new MessageListener());
        Configuration c = conf.buildConfiguration();
        logger.info("configure has been made!");
        th = new currenttimethread();
        th.start();
        logger.info("Thread Started!");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            th.sd = true;
        }, "Shutdown-thread"));
        botEssential.MakeMysqlTables();
        logger.info("Bot configure Complete!");
        //botEssential.MakeSqliteTables();

        //logger.
        PircBotX bot = new PircBotX(c);
        bot.startBot();
    }
}
