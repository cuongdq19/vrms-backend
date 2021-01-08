package org.lordrose.vrms.domains;

import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.lordrose.vrms.domains.bases.TimeAuditable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_notification")
public class Notification extends TimeAuditable<LocalDateTime> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "notify_at")
    private LocalDateTime notifyAt;

    @Column(name = "is_sent")
    private Boolean isSent;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public com.google.firebase.messaging.Notification toFirebaseNotification() {
        return com.google.firebase.messaging.Notification.builder()
                .setTitle(title)
                .setBody(content)
                .build();
    }

    private AndroidNotification toAndroidNotification() {
        return AndroidNotification.builder()
                .setTitle(title)
                .setBody(content)
                .setImage("https://scontent.fsgn5-1.fna.fbcdn.net/v/t1.0-9/96808934_593149278073991_2108251900594880512_n.jpg?_nc_cat=101&ccb=2&_nc_sid=09cbfe&_nc_ohc=nhzaL6s6qCsAX8Jbndl&_nc_ht=scontent.fsgn5-1.fna&oh=938a6da6f7cb4c08db2b5181be72253e&oe=601F3CDF")
                .setColor("#FF0000")
                .build();
    }

    public AndroidConfig toAndroidConfig() {
        return AndroidConfig.builder()
                .setPriority(AndroidConfig.Priority.HIGH)
                .setNotification(toAndroidNotification())
                .build();
    }
}
