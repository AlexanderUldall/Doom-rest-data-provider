package provider.reader;

import provider.Enums.StreamingSite;

public interface StreamReaderI {
    public void start();

    public void onChannelMessage(StreamingSite streamingSite, String userName, String chatMessage);
}
