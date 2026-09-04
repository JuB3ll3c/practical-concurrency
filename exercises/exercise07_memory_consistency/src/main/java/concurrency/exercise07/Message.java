package concurrency.exercise07;

public class Message {
    private String content;
    private boolean ready = false;

    public void setMessage(String content) {
        this.content = content;
        this.ready = true;
    }

    public String getContent() {
        return content;
    }

    public boolean isReady() {
        return ready;
    }
}
