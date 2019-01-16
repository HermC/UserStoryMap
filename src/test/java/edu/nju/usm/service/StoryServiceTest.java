package edu.nju.usm.service;

import edu.nju.usm.mapper.MapMapper;
import edu.nju.usm.mapper.StoryMapper;
import edu.nju.usm.mapper.StoryMapperTest;
import edu.nju.usm.model.Map;
import edu.nju.usm.model.Story;
import edu.nju.usm.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StoryServiceTest {
    private final Logger log = LoggerFactory.getLogger(StoryServiceTest.class);
    @Autowired
    private StoryService storyService;
    @Autowired
    private StoryMapper storyMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private MapService mapService;

    @Test
    public void getStoryListTest() {
        List<Story> storyList_result=storyService.getStoryList(2);
        for(int i=0;i<storyList_result.size();i++) {
            assert storyList_result.get(i).getId()==1||storyList_result.get(i).getId()==2;
        }
    }
    @Test
    public void getStoryByIdTest() {
        Story story_result=storyService.getStoryById(2);
        log.info(story_result.toString());
        assert story_result.getStory_name().equals("test  2");
    }

    @Test
    @Transactional
    public void createStoryTest(){
        User user=userService.getUser("user_search_test");
        Story story=storyService.createStory("test 3","test story 3",2,2, "STORY","TODO",user);
        if(story!=null){
            log.info(story.toString());
        }
        assert story.getStory_status().equals(Story.StoryStatus.TODO);
    }

    @Test
    @Transactional
    public void modifyStoryTest(){
        Story story=storyService.modifyStory(2,"update","update","STREAM","DONE");
        if(story!=null){
            log.info(story.toString());
        }

        assert story.getStory_name().equals("update");
        assert story.getDescription().equals("update");
        assert story.getStory_status().equals(Story.StoryStatus.DONE);
        assert story.getStory_type().equals(Story.StoryType.STREAM);
    }

    @Test
    @Transactional
    public void modifyStoryTypeTest(){
        Story story=storyService.modifyStoryType(2,"STREAM");
        if(story!=null){
            log.info(story.toString());
        }
        assert story.getStory_type().equals(Story.StoryType.STREAM);
    }

    @Test
    @Transactional
    public void modifyStoryStatusTest(){
        Story story=storyService.modifyStoryStatus(2,"DONE");
        if(story!=null){
            log.info(story.toString());
        }
        assert story.getStory_status().equals(Story.StoryStatus.DONE);
    }

    @Test
    @Transactional
    public void modifyStoryReleaseTest(){
        Story story=storyService.modifyStoryRelease(2,1);
        if(story!=null){
            log.info(story.toString());
        }
        assert story.getRelease_id()==1;
    }

    @Test
    @Transactional
    public void modifyStoryParentTest(){
        Story story=storyService.modifyStoryParent(2,1);
        if(story!=null){
            log.info(story.toString());
        }
        assert story.getParent_story_id()==1;
    }

    @Test
    @Transactional
    public void deleteStoryTest(){
        storyService.deleteStory(2);
        assert storyMapper.findByStoryId(2)==null;
    }
}
