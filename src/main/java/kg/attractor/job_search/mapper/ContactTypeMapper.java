package kg.attractor.job_search.mapper;

import kg.attractor.job_search.dto.ContactTypeDto;
import kg.attractor.job_search.entity.ContactType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ContactTypeMapper {
    ContactTypeDto toDto(ContactType contactType);
    ContactType toEntity(ContactTypeDto contactTypeDto);
}
