package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dao.ResumeDao;
import kg.attractor.job_search.dto.ResumeDto;
import kg.attractor.job_search.exceptions.*;
import kg.attractor.job_search.exceptions.ApplicantNotFoundException;
import kg.attractor.job_search.mapper.ResumeMapper;
import kg.attractor.job_search.models.Resume;
import kg.attractor.job_search.service.CategoryService;
import kg.attractor.job_search.service.ResumeService;
import kg.attractor.job_search.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService {
    private final ResumeDao resumeDao;
    private final UserService userService;
    private final CategoryService categoryService;

    @Override
    public List<ResumeDto> getResumes() {
        return resumeDao.getResumes()
                .stream()
                .map(ResumeMapper::toResumeDto)
                .toList();
    }

    @Override
    public List<ResumeDto> getActiveResumes() {
        return resumeDao.getActiveResumes()
                .stream()
                .map(ResumeMapper::toResumeDto)
                .toList();
    }

    @Override
    public List<ResumeDto> getResumesByApplicantId(Long userId) {
        userService.getApplicantById(userId);
        return resumeDao.getResumesByApplicantId(userId)
                .stream()
                .map(ResumeMapper::toResumeDto)
                .toList();

    }

    @Override
    public List<ResumeDto> getResumesByApplicantName(String name) {
        String userName = name.trim().toLowerCase();
        userName = StringUtils.capitalize(userName);

        var applicant = userService.getUsersByName(userName);
        if (applicant.isEmpty()) {
            throw new ApplicantNotFoundException("Applicant with this name not found");
        }
        return resumeDao.getResumesByApplicantName(userName)
                .stream()
                .map(ResumeMapper::toResumeDto)
                .toList();
    }

    @Override
    public Long createResume(ResumeDto resumeDto) {
        if (resumeDto.getApplicantId() == null) {
            throw new ApplicantNotFoundException();
        }

        userService.getApplicantById(resumeDto.getApplicantId());
        categoryService.getCategoryIdIfPresent(resumeDto.getCategoryId());

        return resumeDao.createResume(ResumeMapper.toResume(resumeDto));
    }

    @Override
    public ResumeDto getResumeById(Long resumeId) {
        Resume resume = resumeDao.getResumeById(resumeId)
                .orElseThrow(() -> new ResumeNotFoundException("Не существует резюме с таким id!"));
        return ResumeMapper.toResumeDto(resume);
    }

    @Override
    public Long updateResume(Long resumeId, ResumeDto resumeDto) {
        ResumeDto resume = getResumeById(resumeId);

        if (!resumeDto.getId().equals(resumeId)) {
            throw new ResumeNotFoundException("Неправильный id резюме в теле запроса!");
        }

        if (!resume.getApplicantId().equals(resumeDto.getApplicantId())) {
            throw new EmployerNotFoundException("Вы не можете изменить автора резюме!");
        }

        if (!resume.getCreatedDate().equals(resumeDto.getCreatedDate())) {
            throw new IncorrectDateException("Вы не можете изменить дату создания резюме!");
        }

        if (resume.getCreatedDate().toInstant().isAfter(resumeDto.getUpdateTime().toInstant())) {
            throw new IncorrectDateException("Время обновления вакансии не может быть раньше времени создания!");
        }

        if (resume.getUpdateTime().toInstant().isAfter(resumeDto.getUpdateTime().toInstant())) {
            throw new IncorrectDateException("Новое время обновления вакансии не может быть раньше прошлого!");
        }

        categoryService.getCategoryIdIfPresent((resumeDto.getCategoryId()));
        return resumeDao.updateResume(ResumeMapper.toResume(resumeDto));
    }

    @Override
    public HttpStatus deleteResume(Long resumeId) {
        ResumeDto resume = getResumeById(resumeId);
        resumeDao.deleteResume(resume.getId());
        return HttpStatus.OK;
    }

    @Override
    public List<ResumeDto> getResumesByCategoryId(Long categoryId) {
        Long category = categoryService.getCategoryIdIfPresent(categoryId);

        return resumeDao.getResumesByCategoryId(category)
                .stream()
                .map(ResumeMapper::toResumeDto)
                .toList();
    }
}
