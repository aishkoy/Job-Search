package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dto.WorkExperienceInfoDto;
import kg.attractor.job_search.exception.WorkExperienceNotFoundException;
import kg.attractor.job_search.mapper.WorkExperienceMapper;
import kg.attractor.job_search.entity.WorkExperienceInfo;
import kg.attractor.job_search.repository.WorkExperienceRepository;
import kg.attractor.job_search.service.WorkExperienceInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkExperienceInfoServiceImpl implements WorkExperienceInfoService {
    private final WorkExperienceMapper workExperienceMapper;
    private final WorkExperienceRepository workExperienceRepository;

    @Override
    public Long createWorkExperience(WorkExperienceInfoDto createWorkExperienceInfoDto) {
        WorkExperienceInfo workExperienceInfo = workExperienceMapper.toEntity(createWorkExperienceInfoDto);
        workExperienceRepository.save(workExperienceInfo);
        log.info("Создан опыт работы для резюме с id {}", createWorkExperienceInfoDto.getResume().getId());
        return workExperienceInfo.getId();
    }

    @Override
    public List<WorkExperienceInfoDto> getWorkExperienceInfoByResumeId(Long resumeId) {
        List<WorkExperienceInfoDto> workExperiences = workExperienceRepository.findAllByResumeId(resumeId)
                .stream()
                .map(workExperienceMapper::toDto)
                .toList();
        log.info("Получено опытов работы: {}", workExperiences.size());
        return workExperiences;
    }

    @Override
    public Long updateWorkExperienceInfo(Long id, WorkExperienceInfoDto workExperienceInfoDto) {
        if (!workExperienceInfoDto.getResume().getId().equals(id)) {
            throw new WorkExperienceNotFoundException("Не существует опыта работы с таким id");
        }

        WorkExperienceInfo workExperienceInfo = workExperienceMapper.toEntity(workExperienceInfoDto);
        workExperienceRepository.save(workExperienceInfo);

        log.info("Обновлена информация о опыте работы {}", workExperienceInfoDto.getId());
        return workExperienceInfo.getId();
    }

    @Override
    public WorkExperienceInfoDto getWorkExperienceById(Long id) {
        WorkExperienceInfo workExperienceInfo =
                workExperienceRepository.findById(id)
                        .orElseThrow(WorkExperienceNotFoundException::new);
        log.info("Получен опыт работы с id {}", id);
        return workExperienceMapper.toDto(workExperienceInfo);
    }

    @Override
    public HttpStatus deleteWorkExperienceInfo(Long id) {
        getWorkExperienceById(id);
        workExperienceRepository.deleteById(id);
        log.info("Удален опыт работы {}", id);
        return HttpStatus.OK;
    }
}
