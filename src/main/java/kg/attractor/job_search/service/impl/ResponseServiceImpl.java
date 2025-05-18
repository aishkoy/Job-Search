package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.entity.RespondedApplicant;
import kg.attractor.job_search.entity.Resume;
import kg.attractor.job_search.entity.Vacancy;
import kg.attractor.job_search.exception.AlreadyRespondedException;
import kg.attractor.job_search.exception.IncorrectCategoryException;
import kg.attractor.job_search.repository.RespondedApplicantRepository;
import kg.attractor.job_search.service.ResponseService;
import kg.attractor.job_search.service.ResumeService;
import kg.attractor.job_search.service.VacancyService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class ResponseServiceImpl implements ResponseService {
    private final VacancyService vacancyService;
    private final ResumeService resumeService;
    private final RespondedApplicantRepository responseRepository;
    private final MessageSource messageSource;

    @Override
    public Long applyVacancy(Long vacancyId, Long resumeId, Long applicantId) {
        Vacancy vacancy = vacancyService.getVacancyById(vacancyId);
        Resume resume = resumeService.getResumeById(resumeId, applicantId);


        Locale locale = LocaleContextHolder.getLocale();

        if (responseRepository.existsByResumeIdAndVacancyId(resumeId, vacancyId)) {
            throw new AlreadyRespondedException(messageSource.getMessage("response.already.error", null, locale));
        }

        if (!vacancy.getCategory().getId().equals(resume.getCategory().getId())) {
            throw new IncorrectCategoryException(messageSource.getMessage("response.category.error", null, locale));
        }

        RespondedApplicant respondedApplicant = RespondedApplicant.builder()
                .vacancy(vacancy)
                .resume(resume)
                .isConfirmed(false)
                .build();

        responseRepository.save(respondedApplicant);
        return respondedApplicant.getId();
    }
}
