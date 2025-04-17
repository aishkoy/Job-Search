package kg.attractor.job_search.mapper;
import kg.attractor.job_search.dto.resume.ResumeFormDto;
import kg.attractor.job_search.dto.resume.ResumeDto;
import kg.attractor.job_search.entity.Resume;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface ResumeMapper {
    ResumeDto toDto(Resume resume);
    ResumeDto toDto(ResumeFormDto resumeFormDto);
    Resume toEntity(ResumeFormDto resumeDto);
    Resume toEntity(ResumeDto resumeDto);
    ResumeFormDto toFormDto(ResumeDto resume);
}
