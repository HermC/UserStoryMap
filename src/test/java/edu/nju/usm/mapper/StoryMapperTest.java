package edu.nju.usm.mapper;

import edu.nju.usm.model.Map;
import edu.nju.usm.model.Story;
import edu.nju.usm.model.UserMapRelation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StoryMapperTest {
    private final Logger log = LoggerFactory.getLogger(StoryMapperTest.class);
    @Autowired
    private StoryMapper storyMapper;

    @Test
    public void findByStoryIdTest() {
        Story result1=storyMapper.findByStoryId(1);
        if(result1!=null) {
            log.info(result1.toString());
        }
        assert result1.getStory_name().equals("test");
    }

    @Test
    public void findByMapIdTest() {
        List<Story> result1=storyMapper.findByMapId(2);
        assert result1.size()==2;
    }

    @Test
    @Transactional
    public void insertTest() {
        Story story=new Story();
        story.setMap_id(2);
        story.setCreated_user_id(2);
        story.setStory_type(Story.StoryType.GOAL);
        story.setStory_status(Story.StoryStatus.TODO);
        story.setDescription("test story 3");
        story.setStory_name("test3");
        story.setParent_story_id(-1);
        story.setRelease_id(-1);
        int result1=storyMapper.insert(story);
        assert result1==1;
    }

    @Test
    @Transactional
    public void updateTypeStatusNameDesTest() {
        Story story=new Story();
        story.setId(2);
        story.setStory_name("update");
        story.setDescription("update");
        story.setStory_status(Story.StoryStatus.DOING);
        story.setStory_type(Story.StoryType.STORY);
        int result1=storyMapper.updateTypeStatusNameDes(story);
        assert result1==1;
        assert storyMapper.findByStoryId(2).getStory_name().equals("update");
        assert storyMapper.findByStoryId(2).getDescription().equals("update");
        assert storyMapper.findByStoryId(2).getStory_status().equals(Story.StoryStatus.DOING);
        assert storyMapper.findByStoryId(2).getStory_type().equals(Story.StoryType.STORY);
    }

    @Test
    @Transactional
    public void updateStatusTest() {
        int result1=storyMapper.updateStatus(Story.StoryStatus.DONE,2);
        assert result1==1;
        assert storyMapper.findByStoryId(2).getStory_status().equals(Story.StoryStatus.DONE);
    }
    @Test
    @Transactional
    public void updateTypeTest() {
        int result1=storyMapper.updateType(Story.StoryType.STORY,2);
        assert result1==1;
        assert storyMapper.findByStoryId(2).getStory_type().equals(Story.StoryType.STORY);
    }
    @Test
    @Transactional
    public void updateParentTest() {
        int result1=storyMapper.updateParent(-1,2);
        assert result1==1;
        assert storyMapper.findByStoryId(2).getParent_story_id()==-1;
    }
    @Test
    @Transactional
    public void updateReleaseTest() {
        int result1=storyMapper.updateRelease(1,2);
        assert result1==1;
        assert storyMapper.findByStoryId(2).getRelease_id()==1;
    }
    @Test
    @Transactional
    public void deleteTest() {
        int result1=storyMapper.delete(2);
        assert result1==1;
        assert storyMapper.findByStoryId(2)==null;
    }
}
