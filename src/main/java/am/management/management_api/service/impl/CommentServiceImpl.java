package am.management.management_api.service.impl;


import am.management.management_api.entity.CommentEntity;
import am.management.management_api.entity.NotificationEntity;
import am.management.management_api.exceptions.BadRequestException;
import am.management.management_api.repository.CommentRepository;
import am.management.management_api.repository.NotificationRepository;
import am.management.management_api.service.CommentService;
import am.management.management_api.service.NotificationService;
import am.management.management_api.util.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private BusinessLogic businessLogic;

    @Override
    public CommentEntity getById(long id) {
        CommentEntity commentEntity = commentRepository.findById(id).orElseThrow(
          () -> new BadRequestException("Comment with id " + id + " does not exist")
        );
        return commentEntity;
    }

    @Transactional
    @Override
    public CommentEntity save(CommentEntity commentEntity) {
        commentEntity = commentRepository.findById(commentEntity.getCommentId()).orElseThrow(BadRequestException::new);
        NotificationEntity notificationEntity = new NotificationEntity();
        notificationEntity.setComment(commentEntity);
        notificationEntity.setDelivered(false);
        notificationEntity = notificationRepository.save(notificationEntity);
        commentEntity.setNotification(notificationEntity);
        businessLogic.doSomeWorkOnCommentCreation(commentEntity);
        if (businessLogic.isSuccessDoSomeWorkOnCommentCreation) {
            businessLogic.doSomeWorkOnNotification(notificationEntity);
        }

        return commentEntity;
    }

    @Override
    public void delete(long id) {
        CommentEntity commentEntity = commentRepository.findById(id).orElseThrow(
          () -> new BadRequestException("Comment with id " + id + " was deleted. ")
        );
        commentRepository.delete(commentEntity);
    }

    @Override
    public List<CommentEntity> get(int page, int perPage) {
        validatePageNumberAndSize(page, perPage);
        Page<CommentEntity> commentEntities = this.commentRepository
          .findAll(PageRequest.of(page, perPage, Sort.Direction.ASC, "commentId"));
        return commentEntities.stream().collect(Collectors.toCollection(LinkedList::new));
    }

    private void validatePageNumberAndSize(int page, int size) {
        if (page < 0 || size < 0) {
            throw new BadRequestException("Page number can not be less then 0 ");
        }
        if (size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
        }
    }
}
