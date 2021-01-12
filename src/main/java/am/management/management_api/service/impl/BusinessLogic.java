package am.management.management_api.service.impl;

import am.management.management_api.entity.CommentEntity;
import am.management.management_api.entity.NotificationEntity;
import am.management.management_api.repository.CommentRepository;
import am.management.management_api.repository.NotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class BusinessLogic {


    public boolean isSuccessDoSomeWorkOnCommentCreation;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private NotificationRepository notificationRepository;


    public static void sleepAndRandomThrowRuntimeException(int seconds, int
      exceptionProbabilityProc) {
        try {
            Thread.sleep((long) (seconds * 1000 * Math.random()));
        } catch (InterruptedException e) {

        }
        int randomProc = (int) (100 * Math.random());
        if (exceptionProbabilityProc > randomProc) throw new RuntimeException();
    }


    public void doSomeWorkOnCommentCreation(CommentEntity retVal) {
        if (retVal != null) {
            try {
                sleepAndRandomThrowRuntimeException(1, 30);
                log.info("Comment with {} commentId was successfully saved but not delivered yet", retVal.getCommentId());
                isSuccessDoSomeWorkOnCommentCreation = true;
            } catch (RuntimeException ex) {
                notificationRepository.deleteById(retVal.getNotification().getNotificationId());
                commentRepository.deleteComment(retVal.getCommentId());
                log.warn("Comment with {} commentId was deleted ", retVal.getCommentId());
                isSuccessDoSomeWorkOnCommentCreation = false;
            }
        }
    }

    public void doSomeWorkOnNotification(NotificationEntity retVal) {
        try {
            sleepAndRandomThrowRuntimeException(2, 10);
            retVal.setTime(LocalDateTime.now());
            retVal.setDelivered(true);
            retVal = notificationRepository.save(retVal);
            log.warn("Notification for comment with {} commentId is delivered ", retVal.getComment().getCommentId());
        } catch (RuntimeException ex) {
            retVal.setTime(LocalDateTime.now());
            retVal.setDelivered(false);
            retVal = notificationRepository.save(retVal);
            log.warn("Notification with {} notificationId for comment with {} commentId not delivered ", retVal.getNotificationId(), retVal.getComment().getCommentId());
        }
    }
}
