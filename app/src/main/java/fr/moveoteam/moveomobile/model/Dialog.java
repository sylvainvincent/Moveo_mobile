package fr.moveoteam.moveomobile.model;

/**
 * Created by Sylvain on 12/05/15.
 */
public class Dialog {

    int recipientId;
    String recipientLastName;
    String recipientFirstName;
    String message;
    boolean read;
    String date;
    boolean inbox;

    public Dialog(){}

    public Dialog(int recipientId, String recipientLastName, String recipientFirstName, String message, String date, boolean inbox) {
        this.recipientId = recipientId;
        this.recipientLastName = recipientLastName;
        this.recipientFirstName = recipientFirstName;
        this.message = message;
        this.date = date;
        this.inbox = inbox;
    }

    public Dialog(int recipientId, String recipientLastName, String recipientFirstName, String message, String date, boolean read, boolean inbox) {
        this.recipientId = recipientId;
        this.recipientLastName = recipientLastName;
        this.recipientFirstName = recipientFirstName;
        this.message = message;
        this.date = date;
        this.read = read;
        this.inbox = inbox;
    }

    public int getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(int recipientId) {
        this.recipientId = recipientId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRecipientLastName() {
        return recipientLastName;
    }

    public void setRecipientLastName(String recipientLastName) {
        this.recipientLastName = recipientLastName;
    }

    public String getRecipientFirstName() {
        return recipientFirstName;
    }

    public void setRecipientFirstName(String recipientFirstName) {
        this.recipientFirstName = recipientFirstName;
    }

    public boolean isInbox() {
        return inbox;
    }

    public void setInbox(boolean inbox) {
        this.inbox = inbox;
    }

    @Override
    public String toString() {
        return "Dialog{" +
                "recipientId=" + recipientId +
                ", recipientLastName='" + recipientLastName + '\'' +
                ", recipientFirstName='" + recipientFirstName + '\'' +
                ", message='" + message + '\'' +
                ", read=" + read +
                ", date='" + date + '\'' +
                ", inbox=" + inbox +
                '}';
    }
}
