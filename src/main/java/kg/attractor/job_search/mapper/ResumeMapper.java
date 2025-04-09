package kg.attractor.job_search.mapper;
import kg.attractor.job_search.dto.resume.ResumeFormDto;
import kg.attractor.job_search.dto.resume.ResumeDto;
import kg.attractor.job_search.model.Resume;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface ResumeMapper {
    ResumeDto toDto(Resume resume);
    Resume toEntity(ResumeFormDto resumeDto);
}
