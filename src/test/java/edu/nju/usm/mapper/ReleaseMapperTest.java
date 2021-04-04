package edu.nju.usm.mapper;

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
public class ReleaseMapperTest {

    @Autowired
    private ReleaseMapper releaseMapper;

    Release release = null;

    @Before
    public void prepare() {
        release = new Release();
        release.setMap_id(0);
        release.setRelease_name("test_re");
        release.setDeadline(new Date());

        releaseMapper.insert(release);
    }

    @Test
    @Transactional
    public void findTest() {
        Release release1 = releaseMapper.findByReleaseId(release.getId());
        assert release1 != null;

        List<Release> releaseList = releaseMapper.findByMapId(0);
        assert releaseList.size() == 1;
    }

    @Test
    @Transactional
    public void updateTest() {
        release.setRelease_name("test_update");
        releaseMapper.update(release);

        Release release1 = releaseMapper.findByReleaseId(release.getId());
        assert release1.getRelease_name().equals("test_update");
    }
}
