package client;

import javax.management.Notification;

public interface ServerMessageObserver {
    void notify(Notification notification);
}
