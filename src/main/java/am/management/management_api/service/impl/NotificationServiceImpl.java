package am.management.management_api.service.impl;


import am.management.management_api.entity.NotificationEntity;
import am.management.management_api.exceptions.BadRequestException;
import am.management.management_api.repository.NotificationRepository;
import am.management.management_api.service.NotificationService;
import am.management.management_api.util.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public NotificationEntity getById(long id) {
        NotificationEntity notificationEntity = notificationRepository.findById(id).orElseThrow(
          () -> new BadRequestException("Notification with id " + id + " does not exist")
        );
        return notificationEntity;
    }

    @Override
    public NotificationEntity save(NotificationEntity notificationEntity) {
        notificationEntity = notificationRepository.save(notificationEntity);
        if (notificationEntity != null) {
            return notificationEntity;
        }
        return null;
    }

    @Override
    public void delete(long id) {
        NotificationEntity notificationEntity = notificationRepository.findById(id).orElseThrow(
          () -> new BadRequestException("Notification with id " + id + " does not exist")
        );
        notificationRepository.delete(notificationEntity);
    }

    @Override
    public List<NotificationEntity> get(int page, int perPage) {
        validatePageNumberAndSize(page, perPage);
        Page<NotificationEntity> notificationEntity = this.notificationRepository
          .findAll(PageRequest.of(page, perPage, Sort.Direction.ASC, "notificationId"));
        return notificationEntity.stream().collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public NotificationEntity updateNotification(NotificationEntity notificationEntity) {
        notificationEntity = notificationRepository.save(notificationEntity);
        return notificationEntity;
    }

    private void validatePageNumberAndSize(int page, int size) {
        if (page < 0) {
            throw new BadRequestException("Page number can not be less then 0 ");
        }
        if (size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
        }
    }
}
