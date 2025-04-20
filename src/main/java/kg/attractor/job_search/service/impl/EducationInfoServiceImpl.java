package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dto.EducationInfoDto;
import kg.attractor.job_search.exception.EducationInfoNotFoundException;
import kg.attractor.job_search.mapper.EducationInfoMapper;
import kg.attractor.job_search.entity.EducationInfo;
import kg.attractor.job_search.repository.EducationInfoRepository;
import kg.attractor.job_search.service.EducationInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EducationInfoServiceImpl implements EducationInfoService {
    private final EducationInfoMapper educationInfoMapper;
    private final EducationInfoRepository educationInfoRepository;

    @Override
    public Long createEducationInfo(EducationInfoDto educationInfoDto) {
        EducationInfo educationInfo = educationInfoMapper.toEntity(educationInfoDto);
        educationInfoRepository.save(educationInfo);
        log.info("Created education: {}", educationInfoDto);
        return educationInfo.getId();
    }

    @Override
    public List<EducationInfoDto> getEducationInfoByResumeId(Long resumeId) {
        List<EducationInfoDto> educationInfos =
                educationInfoRepository.findAllByResumeId(resumeId)
                        .stream()
                        .map(educationInfoMapper::toDto)
                        .toList();
        log.info("Retrieved education infos {}", educationInfos);
        return educationInfos;
    }

    @Override
    public Long updateEducationInfo(Long id, EducationInfoDto educationInfoDto) {
        if (!educationInfoDto.getResume().getId().equals(id)) {
            throw new EducationInfoNotFoundException();
        }
        getEducationInfoById(educationInfoDto.getId());
        log.info("Updating education: {}", educationInfoDto);
        EducationInfo educationInfo = educationInfoRepository.save(educationInfoMapper.toEntity(educationInfoDto));
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
