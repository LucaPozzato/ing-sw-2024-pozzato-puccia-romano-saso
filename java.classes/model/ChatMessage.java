public class ChatMessage{

    private int messageId;
    private String message;
    private Player sender;
    private List<Player> receiver;
    private long timeStamp;

    public ChatMessage(String message, Player sender, List<Player> receiver,  long timeStamp){
        this.messageId = getChatMessages().size(); //da decidere, per ora index della lista dei messaggi
        this.message = message;
        this.sender = sender;
        this.receiver = new List<Player>();
        this.timeStamp = timeStamp;
    }

    public int getMessageId() {
        return messageId;
    }

    public String getMessage() {
        return message;
    }

    public Player getSender() {
        return sender;
    }

    public List<Player> getReceiver() {
        return receiver;
    }

    public long getTimeStamp() {
        return timeStamp;
    }
}