package am.management.management_api.controller;

import am.management.management_api.dto.CommentDao;
import am.management.management_api.dto.NotificationDao;
import am.management.management_api.entity.NotificationEntity;
import am.management.management_api.mapper.NotificationMapper;
import am.management.management_api.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationMapper notificationMapper;

    @GetMapping
    public ResponseEntity<List<NotificationDao>> getNotificationList(@RequestParam(name = "page") Integer page, @RequestParam(name = "size") Integer size) {
        List<NotificationEntity> notificationEntities = notificationService.get(page, size);
        if (notificationEntities != null) {
            return ResponseEntity.ok(notificationEntities.stream().map(notificationEntity -> NotificationDao.builder()
              .notificationId(notificationEntity.getNotificationId())
              .time(notificationEntity.getTime())
              .delivered(notificationEntity.getDelivered())
              .comment(CommentDao.builder()
                .commentId(notificationEntity.getComment().getCommentId())
                .time(notificationEntity.getComment().getTime())
                .comment(notificationEntity.getComment().getComment())
                .build())
              .build()).collect(Collectors.toList()));
        }
        return ResponseEntity.notFound().build();
    }
}
