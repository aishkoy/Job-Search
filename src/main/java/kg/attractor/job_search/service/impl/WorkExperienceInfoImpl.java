package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dao.WorkExperienceDao;
import kg.attractor.job_search.dto.WorkExperienceInfoDto;
import kg.attractor.job_search.exception.WorkExperienceNotFoundException;
import kg.attractor.job_search.mapper.WorkExperienceMapper;
import kg.attractor.job_search.model.WorkExperienceInfo;
import kg.attractor.job_search.service.WorkExperienceInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkExperienceInfoImpl implements WorkExperienceInfoService {
    private final WorkExperienceDao workExperienceDao;
    private final WorkExperienceMapper workExperienceMapper;

    @Override
    public Long createWorkExperience(WorkExperienceInfoDto createWorkExperienceInfoDto) {
        log.info("Создан опыт работы для резюме с id {}", createWorkExperienceInfoDto.getResumeId());
        return workExperienceDao.createWorkExperience(workExperienceMapper.toEntity(createWorkExperienceInfoDto));
    }

    @Override
    public List<WorkExperienceInfoDto> getWorkExperienceInfoByResumeId(Long resumeId) {
        return workExperienceDao.getWorkExperienceInfoByResumeId(resumeId)
                .stream()
                .map(workExperienceMapper::toDto)
                .toList();
    }

    @Override
    public Long updateWorkExperienceInfo(Long id, WorkExperienceInfoDto workExperienceInfoDto) {
        if (!workExperienceInfoDto.getResumeId().equals(id)) {
            throw new WorkExperienceNotFoundException("Не существует опыта работы с таким id");
        }

        log.info("Обновлена информация о опыте работы {}", workExperienceInfoDto.getId());
        return workExperienceDao.updateWorkExperience(workExperienceMapper.toEntity(workExperienceInfoDto));
    }

    @Override
    public WorkExperienceInfoDto getWorkExperienceById(Long id) {
        WorkExperienceInfo workExperienceInfo = workExperienceDao.getWorkExperienceInfoById(id)
                .orElseThrow(WorkExperienceNotFoundException::new);
        return workExperienceMapper.toDto(workExperienceInfo);
    }

    @Override
    public HttpStatus deleteWorkExperienceInfo(Long id) {
        getWorkExperienceById(id);
        workExperienceDao.deleteWorkExperience(id);
        log.info("Удален опыт работы {}", id);
        return HttpStatus.OK;
    }
}
