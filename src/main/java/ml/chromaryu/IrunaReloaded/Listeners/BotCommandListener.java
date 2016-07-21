package ml.chromaryu.IrunaReloaded.Listeners;

import ml.chromaryu.IrunaReloaded.Main;
import ml.chromaryu.IrunaReloaded.SQL.SqlHandler;
import org.pircbotx.User;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by chroma on 16/07/02.
 */
public class BotCommandListener extends ListenerAdapter {
    final String prefix = Main.configuration.getProperty("Prefix");
    Logger logger = LoggerFactory.getLogger(BotCommandListener.class);
    private int prefixLength = prefix.length();
    SqlHandler sh = new SqlHandler();
    String[] parsed = null;
    @Override
    public void onMessage(MessageEvent event) throws Exception {
        parsed = null;
        if(event.getMessage().startsWith(prefix)) {
            parsed = event.getMessage().split(" ");
            parsed[0] = parsed[0].substring(prefixLength); // makes life easier...
            //logger.info(parsed[0]);
        }
        if(parsed[0].equalsIgnoreCase("modifyPerm")) {
            //int i = 0;
            logger.debug(String.valueOf(parsed.length));
            if(parsed.length == 3) {
                logger.debug(String.valueOf(sh.getPermissionLevel(event.getUserHostmask().getHostmask(), event.getUserHostmask().getNick())));
                if(sh.getPermissionLevel(event.getUserHostmask().getHostmask(),event.getUserHostmask().getNick()) >= 3) {
                    event.getChannel().getUsers().stream().filter(u -> u.getNick().equalsIgnoreCase(parsed[1])).forEach(u -> {
                        if (sh.getPermissionLevel(u.getHostmask(), u.getNick()) == -1) {
                            sh.addPermissionLevel(u.getHostmask(), u.getNick(), Integer.parseInt(parsed[2]));
                        } else {
                            sh.modifyPermissionLevel(u.getHostmask(), u.getNick(), Integer.parseInt(parsed[2]));
                        }

                        logger.debug("caller: modp " + u.getHostmask() + " " + u.getNick());
                    });
                    /*for(String user : userList) {
                        if(user.equalsIgnoreCase(parsed[1])){
                            if(sh.getPermissionLevel())
                        }
                        logger.info(user);
                    }*/


                }
            }
        }
        if(parsed[0].equalsIgnoreCase("join")) {
            int permlv = sh.getPermissionLevel(event.getUserHostmask().getHostname(),event.getUserHostmask().getNick());
            if(permlv >= 2) {
                if(parsed.length >= 2) {
                    event.getBot().send().joinChannel(parsed[1]);
                    event.getUserHostmask().send().message("Joining " + parsed[1]);
                }
            } else {
                event.getUserHostmask().send().message("You don't have a permission to do that.");
            }

        }
        if(parsed[0].equalsIgnoreCase("part")) {
            StringBuilder sb = new StringBuilder();
            int permlv = sh.getPermissionLevel(event.getUserHostmask().getHostname(), event.getUserHostmask().getNick());
            if (permlv >= 2) {
                if (parsed.length >= 2) {
                    int parsedlength = parsed.length - 1;// should be return value that includes "1", I want use from "2" so I'll sub 1.
                    //logger.warn(String.valueOf(parsedlength));
                    if (parsedlength >= 1) {
                        for (int i = 2; i <= parsedlength; i++) {
                            assert sb != null; // Shouldn't be happen!
                            //event.respond(parsed[i]);
                            sb.append(parsed[i]);
                            sb.append(" ");
                        }
                    }
                    event.getBot().send().quitServer(sb.toString());
                    event.getUserHostmask().send().message("parting " + event.getChannel().getName());
                } else {
                    event.getUserHostmask().send().message("You don't have a permission to do that.");
                }

            }
        }
        parsed = null;
    }

}
