package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dao.RespondedApplicantsDao;
import kg.attractor.job_search.dto.resume.ResumeDto;
import kg.attractor.job_search.dto.vacancy.VacancyDto;
import kg.attractor.job_search.exception.AlreadyRespondedException;
import kg.attractor.job_search.exception.IncorrectCategoryException;
import kg.attractor.job_search.service.ResponseService;
import kg.attractor.job_search.service.ResumeService;
import kg.attractor.job_search.service.VacancyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResponseServiceImpl implements ResponseService {
    private final RespondedApplicantsDao dao;
    private final VacancyService vacancyService;
    private final ResumeService resumeService;

    @Override
    public Long applyVacancy(Long vacancyId, Long resumeId, Long applicantId) {
        VacancyDto vacancy = vacancyService.getVacancyById(vacancyId);
        ResumeDto resume = resumeService.getResumeById(resumeId, applicantId);

        if(dao.hasResponded(resumeId, vacancyId)) {
           throw new AlreadyRespondedException("Вы уже откликались на эту вакансию!");
        }

        if(!vacancy.getCategoryId().equals(resume.getCategoryId())) {
            throw new IncorrectCategoryException("Вы не можете откликнуться на вакансию с другой категорией, чем ваше резюме");
        }

        return dao.respond(resumeId, vacancyId);
    }
}
