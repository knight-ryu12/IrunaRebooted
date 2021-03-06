package ml.chromaryu.IrunaReloaded;

import ml.chromaryu.IrunaReloaded.Listeners.BotCommandListener;
import ml.chromaryu.IrunaReloaded.Listeners.GameListener;
import ml.chromaryu.IrunaReloaded.Listeners.MessageListener;
import ml.chromaryu.IrunaReloaded.SQL.SqlHandler;
import ml.chromaryu.IrunaReloaded.SQL.sqlitehandler;
import org.pircbotx.Configuration;

import java.io.*;
import java.util.List;

/**
 * Created by chroma on 16/06/22.
 */
public class botEssential {
    public static Configuration.Builder CreateConfig(String name,String server,List<String> chan,String realname,String password) {
        return new Configuration.Builder()
                .setName(name)
                .addServer(server)
                .addAutoJoinChannels(chan)
                .setRealName(realname)
                .setAutoReconnect(true)
                .setNickservNick("NickServ")
                .setNickservPassword(password);
    }
    public static void MakeMysqlTables() {
        SqlHandler sh = new SqlHandler();
        sh.maketables();
        Main.logger.info("Sql initialization complete!");
    }
    public static void MakeSqliteTables() {
        sqlitehandler sh = new sqlitehandler();
        sh.maketables();
    }
    public static void writeConfig(File f) {
        String[] defaultconfig = {
                "AutoJoinChannels = ",
                "BotName = ",
                "Server = ",
                "Prefix = :",
                "realname = ",
                "password = "
        };
        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f)));
            for (String s : defaultconfig) {
                pw.println(s);
            }
            pw.flush();
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
