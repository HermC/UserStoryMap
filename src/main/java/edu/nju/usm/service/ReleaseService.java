package edu.nju.usm.service;

import edu.nju.usm.command.ReleaseCommand;
import edu.nju.usm.mapper.ReleaseMapper;
import edu.nju.usm.model.Release;
import edu.nju.usm.model.Story;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class ReleaseService {

    @Autowired
    private ReleaseMapper releaseMapper;
    @Autowired
    private StoryService storyService;

    /**
     * 通过地图id查找release列表
     *
     * @param mapId 地图id
     * @return release列表
     */
    public List<Release> getReleaseList(long mapId) {
        List<Release> releaseList = releaseMapper.findByMapId(mapId);
        if (releaseList == null || releaseList.isEmpty()) {
            return null;
        }
        return releaseList;
    }

    /**
     * 根据release id查找release
     *
     * @param releaseId id
     * @return release
     */
    public Release getReleaseById(long releaseId) {
        return releaseMapper.findByReleaseId(releaseId);
    }

    /**
     * 创建新release
     *
     * @param mapId 所属地图id
     * @param releaseName release名
     * @param deadline 截止日期
     * @return 插入的release
     * */
    public Release createRelease(long mapId, String releaseName, Date deadline) {
        Release release = new Release();
        release.setMap_id(mapId);
        release.setRelease_name(releaseName);
        release.setDeadline(deadline);
        int result = releaseMapper.insert(release);
        if (result != 1) {
            return null;
        } else {
            return release;
        }
    }

    /**
     * 修改release
     *
     * @param releaseCommand 具体信息
     * @return 新release
     * */
    @Transactional
    public Release modify(ReleaseCommand releaseCommand) {
        Release release = releaseMapper.findByReleaseId(releaseCommand.getId());
        release.setRelease_name(releaseCommand.getRelease_name());
        release.setDeadline(releaseCommand.getDeadline());
        int result = releaseMapper.update(release);
        if (result != 1) {
            return null;
        } else {
            return release;
        }
    }

    /**
     * 删除release 在删除前检查是否还有故事处在该release上
     *
     * @param releaseId id
     * @return 删除行数
     * */
    @Transactional
    public int deleteRelease(long releaseId) {
        Release release = releaseMapper.findByReleaseId(releaseId);
        List<Story> storylist = storyService.getStoryList(release.getMap_id());
        for (Story s: storylist) {
            if (s.getRelease_id() == release.getId()) {
                return -1;
            }
        }
        return releaseMapper.delete(releaseId);
    }

}
