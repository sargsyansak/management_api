package am.management.management_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
@Builder
@ToString(exclude = "comment")
public class NotificationDao {

    private Long notificationId;

    private LocalDateTime time;

    private Boolean delivered;

    CommentDao comment;
}
