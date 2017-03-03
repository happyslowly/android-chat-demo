package info.codesert.chatpp;

import java.util.Date;
import java.util.UUID;

public class ChatMessage {
    UUID msgId;
    String body, sender, receiver;
    Date time;
    boolean isMine;// Did I send the message.

    public ChatMessage(String sender, String receiver, String body, boolean isMine) {
        msgId = UUID.randomUUID();
        this.body = body;
        this.sender = sender;
        this.receiver = receiver;
        this.isMine = isMine;
        this.time = new Date();
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "msgId=" + msgId +
                ", body='" + body + '\'' +
                ", sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", time=" + time +
                ", isMine=" + isMine +
                '}';
    }
}
