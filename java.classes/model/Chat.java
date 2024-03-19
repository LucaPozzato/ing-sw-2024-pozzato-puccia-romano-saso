public class Chat{

    private List<ChatMessage> chatMessages;

    public Chat(){
        chatMessages = new List<ChatMessage>();
    }

    public List<ChatMessage> getChatMessages() {
        return chatMessages;
    }
    public addMessage(ChatMessage message){
        chatMessages.add(message);
    }
    public removeMessage(ChatMessage message){
        chatMessages.remove(message);
    }
}