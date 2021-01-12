package am.management.management_api;


import am.management.management_api.entity.CommentEntity;
import am.management.management_api.entity.NotificationEntity;
import am.management.management_api.model.Counter;
import am.management.management_api.repository.CommentRepository;
import am.management.management_api.repository.NotificationRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ContextConfiguration(classes = ManagementApiApplication.class)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentControllerTest {


    @LocalServerPort
    int randomServerPort;

    private static volatile AtomicInteger ALL_EXECUTORS_COUNT = new AtomicInteger(0);

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Test
    public void test_for_comment_creation_proccess() throws URISyntaxException {
        final String baseUrl = "http://localhost:" + randomServerPort + "/api/comment/";
        java.net.URI uri = new URI(baseUrl);
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        for (int i = 0; i < 20; i++) {
            executorService.execute(() -> executeCommentEntityList(uri));
        }
        while (ALL_EXECUTORS_COUNT.get() < 20) {}
        System.out.println("successfullySavedComment __ " + Counter.successfullySavedComment.get());
        System.out.println("successfullyDeliveredComment __ " + Counter.successfullyDeliveredComment.get());
        System.out.println("Percentage of comments which was saved successfully is " + (Counter.successfullySavedComment.get() / 10) + " %");
        System.out.println("Percentage of comments which was saved and delivered is " + (Counter.successfullyDeliveredComment.get() / 10) + " %");
    }

    private void executeCommentEntityList(java.net.URI uri) {
        for (CommentEntity el : Stream.generate(() -> new CommentEntity("comment ___")).limit(50).collect(Collectors.toList())) {
            ResponseEntity<?> responseEntity = restTemplate.postForEntity(uri, new HttpEntity<>(el), String.class);
            Long start = System.nanoTime();
            long commentId = Long.parseLong(String.valueOf(responseEntity.getBody()));
            checkCommentValidation(commentId);
            Long end = System.nanoTime();
            Long duration = end - start;
            System.out.println("Request duration is " + duration + " ns");
        }
        ALL_EXECUTORS_COUNT.set(ALL_EXECUTORS_COUNT.get() + 1);
    }

    private void checkCommentValidation(long commentId) {
        while (true) {
            Optional<CommentEntity> optionalCommentEntity = commentRepository.findById(commentId);
            if (!optionalCommentEntity.isPresent()) {
                System.out.println("Comment was not saved.");
                break;
            } else {
                CommentEntity commentEntity = optionalCommentEntity.get();
                Optional<NotificationEntity> optionalNotificationEntity = notificationRepository.findByComment_CommentId(commentEntity.getCommentId());
                if (optionalNotificationEntity.isPresent()) {
                    NotificationEntity notificationEntity = optionalNotificationEntity.get();
                    if (notificationEntity.getDelivered()) {
                        Counter.successfullySavedComment.set(Counter.successfullySavedComment.get() + 1);
                        Counter.successfullyDeliveredComment.set(Counter.successfullySavedComment.get() + 1);
                        System.out.println("Comment was saved successfully and delivered");
                    } else {
                        System.out.println("Comment was saved successfully but not delivered");
                        Counter.successfullySavedComment.set(Counter.successfullySavedComment.get() + 1);
                    }
                    break;
                }
            }
        }


    }

}
