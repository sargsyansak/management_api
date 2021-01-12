package am.management.management_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString(exclude = "notification")
@Builder
public class CommentDao {

    private Long commentId;

    @NotNull(message = "Field of comment cannot be missing or empty.")
    private String comment;

    private LocalDateTime time;

    NotificationDao notification;
}
