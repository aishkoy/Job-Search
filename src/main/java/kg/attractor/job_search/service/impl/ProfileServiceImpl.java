package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dto.user.UserDto;
import kg.attractor.job_search.service.ProfileService;
import kg.attractor.job_search.service.ResumeService;
import kg.attractor.job_search.service.UserService;
import kg.attractor.job_search.service.VacancyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.NoSuchElementException;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final UserService userService;
    private final ResumeService resumeService;
    private final VacancyService vacancyService;

    @Override
    public void prepareProfileModel(Model model) {
        UserDto user = userService.getAuthUser();
        model.addAttribute("user", user);

        Long roleId = user.getRoleId();
        Long authId = userService.getAuthId();

        if (roleId.equals(1L)) {
            try{
                model.addAttribute("vacancies", vacancyService.getVacanciesByEmployerId(authId));
            }catch (NoSuchElementException ignored){}
        } else if (roleId.equals(2L)) {
            try{
                model.addAttribute("resumes", resumeService.getResumesByApplicantId(authId));
            } catch (NoSuchElementException ignored){}
        }
    }
}

