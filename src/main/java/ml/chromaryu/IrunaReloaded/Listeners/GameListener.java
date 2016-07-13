package ml.chromaryu.IrunaReloaded.Listeners;

import ml.chromaryu.IrunaReloaded.Main;
import ml.chromaryu.IrunaReloaded.SQL.SqlHandler;
import ml.chromaryu.IrunaReloaded.api.RandomNumber;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

/**
 * Created by chroma on 16/06/26.
 */
public class GameListener extends ListenerAdapter {
    SqlHandler sh = new SqlHandler();
    String[] parsed = null;
    final String prefix = Main.configuration.getProperty("Prefix");
    private int prefixLength = prefix.length();
    private boolean isgamerunning = false;
    private int randnumber, answerednum, numhigh = 99,numlow = 0, Cnumhigh = 99, Cnumlow = 0;
    @Override
    public void onMessage(MessageEvent event) throws Exception {
        if(event.getMessage().startsWith(prefix)) {
            parsed = event.getMessage().split(" ");
            parsed[0] = parsed[0].substring(prefixLength);
            Main.logger.info(parsed[0]);
            //Main.logger.info(String.valueOf(parsed[0].equalsIgnoreCase("hello")));
        }
        if(parsed[0].equalsIgnoreCase("g")) {
            if(parsed.length <= 1) {
                event.getChannel().send().message("no argument specified.");
            } else {
                switch (parsed[1]){
                    case "startgame":
                        if(isgamerunning) {
                            event.getChannel().send().message("Game is already running");
                        } else {
                            isgamerunning = true;
                            Cnumhigh = numhigh;
                            Cnumlow = numlow;
                            randnumber = RandomNumber.showRandomInteger(numlow,numhigh);
                            Main.logger.info(String.valueOf(randnumber));
                            event.getChannel().send().message("Game has been started by:" + event.getUser().getNick() + "　Join with " + prefix + "guess ! " );
                        }
                        break;
                }
            }
        }
        if(parsed[0].equalsIgnoreCase("guess")) {
            if(isgamerunning) {
                if(parsed.length == 2) {
                    try {
                        answerednum = Integer.parseInt(parsed[1]);
                    } catch (NumberFormatException e) {
                        event.getChannel().send().message("Please, Input integer :D");
                        e.printStackTrace();
                    }
                        if(answerednum == randnumber) {
                            event.getChannel().send().message("You are correct! game is over!");
                            isgamerunning = false;
                        } else if (answerednum < randnumber) {
                            event.getChannel().send().message("I think correct number is higher than you think.");
                        } else {
                            event.getChannel().send().message("I think correct number is lower than that.");
                        }
                }
            }
        }
        parsed = null;
    }

}
