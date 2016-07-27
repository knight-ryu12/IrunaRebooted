package ml.chromaryu.IrunaReloaded.Listeners;

import ml.chromaryu.IrunaReloaded.Main;
import ml.chromaryu.IrunaReloaded.SQL.SqlHandler;
import ml.chromaryu.IrunaReloaded.api.pastebinApi;
import ml.chromaryu.IrunaReloaded.api.urlShortenerAPI;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static ml.chromaryu.IrunaReloaded.api.formatFileSize.*;
import static ml.chromaryu.IrunaReloaded.api.formatTime.*;

import java.util.*;

/**
 * Created by chroma on 16/06/22.
 */
public class MessageListener extends ListenerAdapter {
    final String prefix = Main.configuration.getProperty("Prefix");
    Logger logger = LoggerFactory.getLogger(MessageListener.class);
    private int prefixLength = prefix.length();
    SqlHandler sh = new SqlHandler();
    String[] parsed = null;
    @Override
    public void onMessage(MessageEvent event) throws Exception {
        parsed = null;
        // Fake. adding it later
        if(event.getMessage().startsWith(prefix)) {
            parsed = event.getMessage().split(" ");
            parsed[0] = parsed[0].substring(prefixLength); // makes life easier...
            logger.info(parsed[0]);
            //Main.logger.info(String.valueOf(parsed[0].equalsIgnoreCase("hello")));
        }
        if(parsed[0].equalsIgnoreCase("hello")) {
           event.respond("Hello!");
        }
        if(parsed[0].equalsIgnoreCase("shorten")) {
            if(parsed.length == 2) {
                String u = parsed[1];
                String shorten = urlShortenerAPI.shorten(u);

                event.getChannel().send().message(shorten);
            }
        }
        if(parsed[0].equalsIgnoreCase("paste")) {
            if (parsed.length >= 2) {
                int parsedlength = parsed.length - 1;// should be return value that includes "1", I want use from "2" so I'll sub 1.
                //logger.warn(String.valueOf(parsedlength));
                if (parsedlength >= 1) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 2; i <= parsedlength; i++) {
                        assert sb != null; // Shouldn't be happen!
                        //event.respond(parsed[i]);
                        sb.append(parsed[i]);
                        sb.append(" ");
                    }
                    event.respond(pastebinApi.paste(sb.toString()));
                }
            }
        }
        if(parsed[0].equalsIgnoreCase("gc")) {
            logger.info("GC'd.");
            System.gc();
            event.respond("GC done.");
        }
        if(parsed[0].equalsIgnoreCase("getmem")) {
            Runtime runtime = Runtime.getRuntime();
            event.getChannel().send().message(
                    "Current memory usage: " +
                            formatFileSize(runtime.totalMemory() - runtime.freeMemory()) +
                            "/" +
                            formatFileSize(runtime.totalMemory()) +
                            ". Total memory that can be used: " +
                            formatFileSize(runtime.maxMemory()) +
                            ".  Available Processors: " +
                            (runtime.availableProcessors())

            );
        }
        if(parsed[0].equalsIgnoreCase("gettime")) {
            String TZ = "GMT";
            Calendar cal;
            Locale locale;
            cal = Main.th.cal;
            locale = Locale.ENGLISH;
            if(parsed.length == 1) {
                cal.setTimeZone(TimeZone.getTimeZone(TZ));
            } else if (parsed.length == 2) {
                TZ = parsed[1];
                cal.setTimeZone(TimeZone.getTimeZone(TZ));
            }
            String ret = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, locale) + ", " + cal.getDisplayName(Calendar.MONTH, Calendar.LONG, locale) + " " + (cal.get(Calendar.DAY_OF_MONTH)) + ", " + cal.get(Calendar.YEAR) + " at " + cal.get(Calendar.HOUR_OF_DAY) + ":" + formattime(cal.get(Calendar.MINUTE)) + ":" + formattime(cal.get(Calendar.SECOND)) + " ("+ TZ + ")";
            event.getChannel().send().message(ret);
        }
        if(parsed[0].equalsIgnoreCase("addfactoid")) {
            // should be parsed[1] is keyword.
            String keyword = parsed[1];
            String message,nickname = event.getUserHostmask().getNick(),hostmask = event.getUserHostmask().getHostmask();
            boolean issucceed;
            //logger.warn(String.valueOf(parsed.length));
            int parsedlength = parsed.length-1;// should be return value that includes "1", I want use from "2" so I'll sub 1.
            //logger.warn(String.valueOf(parsedlength));
            if(parsedlength >= 1) {
                StringBuilder sb = new StringBuilder();
                for(int i=2;i<=parsedlength;i++) {
                    assert sb != null; // Shouldn't be happen!
                    //event.respond(parsed[i]);
                    sb.append(parsed[i]);
                    sb.append(" ");
                }
                assert sb != null; // Shouldn't be happen!
                message = sb.toString();
                issucceed = sh.setFactoid(keyword,message,hostmask,nickname);
                if(issucceed) {
                    event.respond("Successfully factoid "+ keyword + " has created!");
                } else {
                    throw new IllegalStateException("Shouldn't be happened, But it happened. at addfactoid.");
                }
            } else event.respond("Check arguments. " + prefix + "addfactoid <keyword> <message>");
        }
        if(parsed[0].equalsIgnoreCase("f") || parsed[0].equalsIgnoreCase("factoid")) {
            if(parsed.length >= 1) {
                String keyword = parsed[1];
                String nickname = event.getUserHostmask().getNick();
                String hostmask = event.getUserHostmask().getHostmask();
                String result = sh.getFactoid(keyword,hostmask,nickname);
                if(result == null) {
                    result = "No factoid found with keyword of " + keyword + ".";
                }
                if(result.startsWith("%A")) {
                    result = result.substring(2);
                    event.getChannel().send().action(result);
                } else event.getChannel().send().message(result);
            }
        }
        if(parsed[0].equalsIgnoreCase("factoidbyname") || parsed[0].equalsIgnoreCase("fn")) {
            if(parsed.length >= 2) {
                String nickname = parsed[1];
                List list = sh.getFactoidByNickname(nickname);
                StringBuilder sb = new StringBuilder();
                if(list != null) {
                    for (Object s : list) {
                        sb.append(String.valueOf(s));
                        sb.append(" ");
                    }
                    event.getUserHostmask().send().message(sb.toString());
                }
            }
        }
        parsed = null; // THIS LINE IS IMPORTANT, DON'T DELETE.
    }
}
