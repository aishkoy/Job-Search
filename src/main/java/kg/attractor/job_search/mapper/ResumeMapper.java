package kg.attractor.job_search.mapper;

import kg.attractor.job_search.dto.ResumeDto;
import kg.attractor.job_search.entity.Resume;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring",
        uses = {EducationInfoMapper.class, WorkExperienceMapper.class, ContactInfoMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)

public interface ResumeMapper {
    ResumeDto toDto(Resume resume);

    Resume toEntity(ResumeDto resumeDto);
}
