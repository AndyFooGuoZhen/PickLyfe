package picklyfe.registration.Chat;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(length = 100)
    private String userName;

    @Column(length = 100)
    private String toWhom;

    @NotNull
    @Lob
    private String content;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "sent")
    private Date sent = new Date();

    @NotNull
    @Column(name = "Message_Type")
    private MessageType messageType;


    public Message() {};

    public Message(String userName, String content, MessageType messageType) {
        this.userName = userName;
        this.toWhom = null;
        this.content = content;
        this.messageType = messageType;
    }

    public Message(String userName, String toWhom, String content, MessageType messageType) {
        this.userName = userName;
        this.toWhom = toWhom;
        this.content = content;
        this.messageType = messageType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getToWhom() {
        return toWhom;
    }

    public void setToWhom(String toWhom) {
        this.toWhom = toWhom;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getSent() {
        return sent;
    }

    public void setSent(Date sent) {
        this.sent = sent;
    }

    public MessageType getMsgType() {
        return messageType;
    }

    public void setMsgType(MessageType msgType) {
        this.messageType = msgType;
    }
}