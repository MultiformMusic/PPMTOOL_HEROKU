package net.multiformmusic.ppmtool.repositories;

import net.multiformmusic.ppmtool.domain.Project;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Long> {

    Project findByProjectIdentifier(String projectId);

    Iterable<Project> findAllByProjectLeader(String username);
}

