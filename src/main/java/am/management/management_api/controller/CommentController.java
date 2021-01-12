package am.management.management_api.controller;

import am.management.management_api.dto.CommentDao;
import am.management.management_api.dto.NotificationDao;
import am.management.management_api.entity.CommentEntity;
import am.management.management_api.mapper.CommentMapper;
import am.management.management_api.repository.CommentRepository;
import am.management.management_api.service.CommentService;
import ch.qos.logback.core.util.ExecutorServiceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private CommentRepository commentRepository;

    private ExecutorService executorService = ExecutorServiceUtil.newExecutorService();


    @PostMapping
    public ResponseEntity<?> createComment(@Valid @RequestBody CommentDao commentDao) {
        CommentEntity commentEntity = commentMapper.toEntity(commentDao);
        commentEntity.setTime(LocalDateTime.now());
        commentEntity = commentRepository.save(commentEntity);
        final CommentEntity finalCommentEntity = commentEntity;
        executorService.execute(() -> commentService.save(finalCommentEntity));
        return ResponseEntity.ok(commentEntity.getCommentId());
    }

    @GetMapping
    public ResponseEntity<List<CommentDao>> getCommentList(@RequestParam(name = "page") Integer page, @RequestParam(name = "size") Integer size) {
        List<CommentEntity> commentEntities = commentService.get(page, size);
        if (commentEntities != null) {
            List<CommentDao> result = commentEntities.stream().map(commentEntity -> CommentDao.builder()
              .commentId(commentEntity.getCommentId())
              .time(commentEntity.getTime())
              .comment(commentEntity.getComment())
              .notification(NotificationDao.builder()
                .notificationId(commentEntity.getNotification().getNotificationId())
                .time(commentEntity.getNotification().getTime())
                .delivered(commentEntity.getNotification().getDelivered())
                .build())
              .build()).collect(Collectors.toList());
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.notFound().build();
    }

}
