package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dto.ResumeDto;
import kg.attractor.job_search.service.ResumeService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResumeServiceImpl implements ResumeService {

    @Override
    public List<ResumeDto> getResumes(Long employerId) {
        //TODO получение всех резюме для работодателя
        return List.of();
    }

    @Override
    public Long createResume(ResumeDto resumeDto, Long applicantId) {
        //TODO создание резюме для соискателя
        return null;
    }

    @Override
    public Long updateResume(Long resumeId, ResumeDto resumeDto, Long applicantId) {
        //TODO обновление резюме для соискателя
        return null;
    }

    @Override
    public HttpStatus deleteResume(Long resumeId, Long applicantId) {
        //TODO удаление резюме для соискателя
        return HttpStatus.OK;
    }

    @Override
    public List<ResumeDto> getResumesByCategoryId(Long categoryId, Long employerId) {
        //TODO получение всех резюме по категории для работодателя
        return List.of();
    }
}
