package am.management.management_api.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@Table(name = "notification")
@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class NotificationEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long notificationId;

    private LocalDateTime time;

    private Boolean delivered;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "comment_id", nullable = false)
    CommentEntity comment;

    @Override
    public String toString() {
        return "NotificationEntity{" +
          "notificationId=" + notificationId +
          ", time=" + time +
          ", delivered=" + delivered +
          '}';
    }
}
