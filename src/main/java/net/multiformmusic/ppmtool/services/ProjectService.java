package net.multiformmusic.ppmtool.services;

import net.multiformmusic.ppmtool.domain.Backlog;
import net.multiformmusic.ppmtool.domain.Project;
import net.multiformmusic.ppmtool.domain.User;
import net.multiformmusic.ppmtool.exceptions.ProjectIdException;
import net.multiformmusic.ppmtool.exceptions.ProjectNotFoundException;
import net.multiformmusic.ppmtool.repositories.BacklogRepository;
import net.multiformmusic.ppmtool.repositories.ProjectRepository;
import net.multiformmusic.ppmtool.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    UserRepository userRepository;

    public Project saveOrUpdateProject(Project project, String username) {

        try {

            if (project.getId() != null) {
                Project existingProject = projectRepository.findByProjectIdentifier((project.getProjectIdentifier()));
                if (existingProject != null && !existingProject.getProjectLeader().equals(username)) {
                    throw new ProjectNotFoundException("Porject not found in your account");
                } else if (existingProject == null) {
                    throw new ProjectNotFoundException("Project with ID : '" + project.getProjectIdentifier() + "' cannot be updated because it does not exist.");
                }
            }

            User user = userRepository.findByUsername(username);
            project.setUser(user);
            project.setProjectLeader(user.getUsername());

            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());

            if (project.getId() == null) {

                Backlog backlog = new Backlog();
                project.setBacklog(backlog);
                backlog.setProject(project);
                backlog.setProjectIdentifier(project.getProjectIdentifier());

            } else {
                project.setBacklog(backlogRepository.findByProjectIdentifier(project.getProjectIdentifier().toUpperCase()));
            }

            return projectRepository.save(project);

        } catch (ProjectNotFoundException pne) {
            throw new ProjectNotFoundException(pne.getMessage());
        } catch (Exception ex) {
            throw new ProjectIdException("Project ID " + project.getProjectIdentifier() + " already exists");
        }

    }

    public Project findProjectByIdentifier(String projectId, String username) {

        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());

        if (project == null) {
            throw new ProjectIdException("Project ID " + projectId+ " does not exist");
        }

        if (!project.getProjectLeader().equals(username)) {
            throw  new ProjectNotFoundException(("Project not found in your account"));
        }

        return project;
    }

    public Iterable<Project> findAllProjects(String username) {
        return projectRepository.findAllByProjectLeader(username);
    }

    public void deleteProjectByIdentifier(String projectId, String username) {

        /*Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());

        if (project == null) {
            throw new ProjectIdException("Cannot delete Project with ID " + projectId);
        }*/

        projectRepository.delete(findProjectByIdentifier(projectId, username));
    }
}
