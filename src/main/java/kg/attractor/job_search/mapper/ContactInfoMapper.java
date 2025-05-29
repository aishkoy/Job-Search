package kg.attractor.job_search.mapper;

import kg.attractor.job_search.dto.ContactInfoDto;
import kg.attractor.job_search.entity.ContactInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ContactInfoMapper {
    @Mapping(target = "resume", ignore = true)
    ContactInfo toEntity(ContactInfoDto contactInfoDto);

    @Mapping(target = "resumeId", source = "resume.id")
    ContactInfoDto toDto(ContactInfo contactInfo);
}