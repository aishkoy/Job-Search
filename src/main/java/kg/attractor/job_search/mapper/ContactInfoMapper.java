package kg.attractor.job_search.mapper;

import kg.attractor.job_search.dto.ContactInfoDto;
import kg.attractor.job_search.models.ContactInfo;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ContactInfoMapper {
    public ContactInfoDto toDto(ContactInfo contactInfo) {
        return ContactInfoDto.builder()
                .id(contactInfo.getId())
                .resumeId(contactInfo.getResumeId())
                .typeId(contactInfo.getTypeId())
                .value(contactInfo.getValue())
                .build();
    }

    public ContactInfo toEntity(ContactInfoDto contactInfoDto) {
        return ContactInfo.builder()
                .id(contactInfoDto.getId())
                .resumeId(contactInfoDto.getResumeId())
                .typeId(contactInfoDto.getTypeId())
                .value(contactInfoDto.getValue())
                .build();
    }
}
