package am.management.management_api.mapper;


import am.management.management_api.dto.NotificationDao;
import am.management.management_api.entity.NotificationEntity;
import org.springframework.stereotype.Component;

@Component
@org.mapstruct.Mapper(componentModel = "spring")
public interface NotificationMapper extends Mapper<NotificationEntity, NotificationDao> {
}
