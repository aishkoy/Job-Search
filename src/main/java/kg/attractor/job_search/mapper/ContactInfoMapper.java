package kg.attractor.job_search.mapper;

import kg.attractor.job_search.dto.ContactInfoDto;
import kg.attractor.job_search.model.ContactInfo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ContactInfoMapper {
    ContactInfoDto toDto(ContactInfo contactInfo);
    ContactInfo toEntity(ContactInfoDto contactInfoDto);
}
