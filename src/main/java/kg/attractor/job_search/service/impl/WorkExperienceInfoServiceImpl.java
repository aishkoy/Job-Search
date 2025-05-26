package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dto.WorkExperienceInfoDto;
import kg.attractor.job_search.entity.Resume;
import kg.attractor.job_search.exception.nsee.WorkExperienceNotFoundException;
import kg.attractor.job_search.mapper.WorkExperienceMapper;
import kg.attractor.job_search.entity.WorkExperienceInfo;
import kg.attractor.job_search.repository.WorkExperienceRepository;
import kg.attractor.job_search.service.interfaces.WorkExperienceInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkExperienceInfoServiceImpl implements WorkExperienceInfoService {
    private final WorkExperienceMapper workExperienceMapper;
    private final WorkExperienceRepository workExperienceRepository;

    @Override
    public Long createWorkExperience(WorkExperienceInfoDto createWorkExperienceInfoDto) {
        WorkExperienceInfo workExperienceInfo = workExperienceMapper.toEntity(createWorkExperienceInfoDto);
        workExperienceInfo.setResume(Resume.builder().id(createWorkExperienceInfoDto.getResumeId()).build());
        workExperienceRepository.save(workExperienceInfo);
        log.info("Создан опыт работы для резюме с id {}", createWorkExperienceInfoDto.getResumeId());
        return workExperienceInfo.getId();
    }

    @Override
    public Long updateWorkExperienceInfo(WorkExperienceInfoDto workExperienceInfoDto) {
        WorkExperienceInfo workExperienceInfo = workExperienceMapper.toEntity(workExperienceInfoDto);
        workExperienceInfo.setResume(Resume.builder().id(workExperienceInfoDto.getResumeId()).build());
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
