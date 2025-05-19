package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dto.EducationInfoDto;
import kg.attractor.job_search.entity.Resume;
import kg.attractor.job_search.exception.nsee.EducationInfoNotFoundException;
import kg.attractor.job_search.mapper.EducationInfoMapper;
import kg.attractor.job_search.entity.EducationInfo;
import kg.attractor.job_search.repository.EducationInfoRepository;
import kg.attractor.job_search.service.interfaces.EducationInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EducationInfoServiceImpl implements EducationInfoService {
    private final EducationInfoMapper educationInfoMapper;
    private final EducationInfoRepository educationInfoRepository;

    @Override
    public Long createEducationInfo(EducationInfoDto educationInfoDto) {
        EducationInfo educationInfo = educationInfoMapper.toEntity(educationInfoDto);
        educationInfo.setResume(Resume.builder().id(educationInfoDto.getResumeId()).build());
        educationInfoRepository.save(educationInfo);
        log.info("Created education: {}", educationInfoDto);
        return educationInfo.getId();
    }


    @Override
    public Long updateEducationInfo(EducationInfoDto educationInfoDto) {
        EducationInfo educationInfo = educationInfoMapper.toEntity(educationInfoDto);
        educationInfo.setResume(Resume.builder().id(educationInfoDto.getResumeId()).build());
        educationInfoRepository.save(educationInfo);
        log.info("Updating education: {}", educationInfoDto);
        return educationInfo.getId();
    }

    @Override
    public HttpStatus deleteEducationInfo(Long id) {
        getEducationInfoById(id);
        educationInfoRepository.deleteById(id);
        log.info("Deleted education: {}", id);
        return HttpStatus.OK;
    }

    @Override
    public EducationInfoDto getEducationInfoById(Long id) {
        EducationInfo educationInfo = educationInfoRepository.findById(id)
                .orElseThrow(EducationInfoNotFoundException::new);
        log.info("Retrieved education: {}", educationInfo);
        return educationInfoMapper.toDto(educationInfo);
    }
}
