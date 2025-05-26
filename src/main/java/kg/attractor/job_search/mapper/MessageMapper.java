package kg.attractor.job_search.mapper;

import kg.attractor.job_search.dto.MessageDto;
import kg.attractor.job_search.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    @Mapping(target = "response.vacancy", ignore = true)
    @Mapping(target = "response.resume", ignore = true)
    @Mapping(target = "response.isConfirmed", ignore = true)
    Message toEntity(MessageDto messageDto);

    @Mapping(target = "response.vacancy", ignore = true)
    @Mapping(target = "response.resume", ignore = true)
    @Mapping(target = "response.isConfirmed", ignore = true)
    MessageDto toDto(Message message);
}