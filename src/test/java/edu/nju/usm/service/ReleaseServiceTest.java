package edu.nju.usm.service;

import edu.nju.usm.command.ReleaseCommand;
import edu.nju.usm.model.Release;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReleaseServiceTest {

    @Autowired
    private ReleaseService releaseService;

    Release release = null;

    @Before
    public void prepare() {
        release = releaseService.createRelease(-1, "test", new Date());
    }

    @Test
    @Transactional
    public void testFind() {
        Release release1 = releaseService.getReleaseById(release.getId());
        assert release1.getRelease_name().equals(release.getRelease_name());

        List<Release> releaseList = releaseService.getReleaseList(-1);
        assert releaseList.size() == 1;
    }

    @Test
    @Transactional
    public void testUpdate() {
        release.setRelease_name("test_update");

        ReleaseCommand command = new ReleaseCommand();
        command.setId(release.getId());
        command.setRelease_name(release.getRelease_name());
        command.setMap_id(release.getMap_id());
        command.setDeadline(release.getDeadline());

        assert releaseService.modify(command) != null;

        Release release1 = releaseService.getReleaseById(release.getId());
        assert release1.getRelease_name().equals(command.getRelease_name());
    }

}
