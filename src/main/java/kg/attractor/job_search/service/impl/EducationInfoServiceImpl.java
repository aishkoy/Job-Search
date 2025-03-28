package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dao.EducationInfoDao;
import kg.attractor.job_search.dto.EducationInfoDto;
import kg.attractor.job_search.exception.EducationInfoNotFoundException;
import kg.attractor.job_search.mapper.EducationInfoMapper;
import kg.attractor.job_search.model.EducationInfo;
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
    private final EducationInfoDao educationInfoDao;

    @Override
    public Long createEducationInfo(EducationInfoDto educationInfoDto){
        Long id = educationInfoDao.createEducationInfo(EducationInfoMapper.toEducationInfo(educationInfoDto));
        log.info("Created education: {}", educationInfoDto);
        return id;
    }

    @Override
    public List<EducationInfoDto> getEducationInfoByResumeId(Long resumeId) {
        List<EducationInfoDto> educationInfos =  educationInfoDao.getEducationInfosByResumeId(resumeId).stream()
                .map(EducationInfoMapper::toEducationInfoDto)
                .toList();
        log.info("Retrieved education infos {}", educationInfos);
        return educationInfos;
    }

    @Override
    public Long updateEducationInfo(Long id, EducationInfoDto educationInfoDto){
        if(!educationInfoDto.getResumeId().equals(id)){
            throw new EducationInfoNotFoundException();
        }
        getEducationInfoById(educationInfoDto.getId());
        log.info("Updating education: {}", educationInfoDto);
        return educationInfoDao.updateEducationInfo(EducationInfoMapper.toEducationInfo(educationInfoDto));
    }

    @Override
    public HttpStatus deleteEducationInfo(Long id){
        getEducationInfoById(id);
        educationInfoDao.deleteEducationInfo(id);
        log.info("Deleted education: {}", id);
        return HttpStatus.OK;
    }

    @Override
    public EducationInfoDto getEducationInfoById(Long id){
        EducationInfo educationInfo = educationInfoDao.getEducationInfoById(id).orElseThrow(EducationInfoNotFoundException::new);
        log.info("Retrieved education: {}", educationInfo);
        return EducationInfoMapper.toEducationInfoDto(educationInfo);
    }
}
