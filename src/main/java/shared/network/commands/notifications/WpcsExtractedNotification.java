package shared.network.commands.notifications;

import shared.clientInfo.ClientWpc;
import shared.constants.GameConstants;

import java.util.ArrayList;
import java.util.HashMap;

public class WpcsExtractedNotification implements Notification {
    public String username;
    public final HashMap<String, ArrayList<ClientWpc>> wpcsByUser;
    public final int timeToCompleteTask = GameConstants.CHOOSE_WPC_WAITING_TIME;

    public WpcsExtractedNotification(HashMap<String, ArrayList<ClientWpc>> wpcsByUser) {
        this.wpcsByUser = wpcsByUser;
    }

    @Override
    public void handle(NotificationHandler handler) {
        handler.handle(this);
    }
}