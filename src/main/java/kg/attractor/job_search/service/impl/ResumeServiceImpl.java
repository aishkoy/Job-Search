package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dao.ResumeDao;
import kg.attractor.job_search.dto.ResumeDto;
import kg.attractor.job_search.exceptions.*;
import kg.attractor.job_search.exceptions.ApplicantNotFoundException;
import kg.attractor.job_search.exceptions.UserNotFoundException;
import kg.attractor.job_search.mapper.ResumeMapper;
import kg.attractor.job_search.service.CategoryService;
import kg.attractor.job_search.service.ResumeService;
import kg.attractor.job_search.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    public List<ResumeDto> getActiveResumes(){
        return resumeDao.getActiveResumes()
                .stream()
                .map(ResumeMapper::toResumeDto)
                .toList();
    }

    @Override
    public List<ResumeDto> getResumesByApplicantId(Long userId){
        var applicant = userService.getApplicantById(userId);
        if(applicant.isPresent()){
            return resumeDao.getResumesByApplicantId(userId)
                    .stream()
                    .map(ResumeMapper::toResumeDto)
                    .toList();
        } else{
            throw new UserNotFoundException("User not found");
        }
    }

    @Override
    public List<ResumeDto> getResumesByApplicantName(String name){
        String userName = name.trim().toLowerCase();
        userName = StringUtils.capitalize(userName);

        var applicant = userService.getUsersByName(userName);
        if(applicant.isEmpty()){
            throw new ApplicantNotFoundException("Applicant with this name not found");
        }
        return resumeDao.getResumesByApplicantName(userName)
                .stream()
                .map(ResumeMapper::toResumeDto)
                .toList();
    }
    @Override
    public Long createResume(ResumeDto resumeDto) {
        if(resumeDto.getApplicantId() == null){
            throw new ApplicantNotFoundException();
        }

        if(userService.getApplicantById(resumeDto.getApplicantId()).isEmpty()){
            throw new ApplicantNotFoundException("Не существует такого соискателя!");
        }

        if(categoryService.getCategoryIdIfPresent(resumeDto.getCategoryId()).isEmpty()){
            throw new CategoryNotFoundException("Не существует такой категории!");
        }

        return resumeDao.createResume(ResumeMapper.toResume(resumeDto));
    }

    @Override
    public Optional<ResumeDto> getResumeById(Long resumeId){
        return resumeDao.getResumeById(resumeId)
                .map(ResumeMapper::toResumeDto);
    }

    @Override
    public Long updateResume(Long resumeId, ResumeDto resumeDto) {
        Optional<ResumeDto> resume = getResumeById(resumeId);
        if(resume.isEmpty()) {
            throw new ResumeNotFoundException("Не существует резюме с таким id!");
        }

        if(!resumeDto.getId().equals(resumeId)){
            throw new ResumeNotFoundException("Неправильный id резюме в теле запроса!");
        }

        resume.ifPresent(r ->{
            if(!r.getApplicantId().equals(resumeDto.getApplicantId())){
                throw new EmployerNotFoundException("Вы не можете изменить автора резюме!");
            }

            if(!r.getCreatedDate().equals(resumeDto.getCreatedDate())){
                throw new IncorrectDateException("Вы не можете изменить дату создания резюме!");
            }

            if(r.getCreatedDate().toInstant().isAfter(resumeDto.getUpdateTime().toInstant())){
                throw new IncorrectDateException("Время обновления вакансии не может быть раньше времени создания!");
            }

            if(r.getUpdateTime().toInstant().isAfter(resumeDto.getUpdateTime().toInstant())){
                throw new IncorrectDateException("Новое время обновления вакансии не может быть раньше прошлого!");
            }
        });

        Optional<Long> categoryId = categoryService.getCategoryIdIfPresent((resumeDto.getCategoryId()));
        if(categoryId.isEmpty()){
            throw new CategoryNotFoundException("Не существует категории с таким id!");
        }

        return resumeDao.updateResume(ResumeMapper.toResume(resumeDto));
    }

    @Override
    public HttpStatus deleteResume(Long resumeId) {
        if(getResumeById(resumeId).isPresent()){
            resumeDao.deleteResume(resumeId);
            return HttpStatus.OK;
        }
        throw new ResumeNotFoundException();
    }

    @Override
    public List<ResumeDto> getResumesByCategoryId(Long categoryId) {
        if(categoryService.getCategoryIdIfPresent(categoryId).isEmpty()){
            throw new CategoryNotFoundException();
        }

        return resumeDao.getResumesByCategoryId(categoryId)
                .stream()
                .map(ResumeMapper::toResumeDto)
                .toList();
    }
}
