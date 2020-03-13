package Common;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ConverterTest {

    @Test
    void toGameStageListFromStringTest(){

        String q = "question";
        String p1 = "p1";
        String p2 = "p2";
        String p3 = "p3";
        String p4 = "p4";

        String Q1 = String.format("%s:%s,%s,%s,%s",q+"1",p1,p2,p3,p4);
        String Q2 = String.format("%s:%s,%s,%s,%s",q+"2",p4,p3,p2,p1);

        String QS = String.format("%s;%s",Q1,Q2);

        List<GameStage> questions = Converter.toGameStageList(QS);

        assertEquals(2,questions.size());
        GameStage gameStage1 = questions.get(0);
        assertEquals(q+"1",gameStage1.getQuestion());
        assertEquals(p1,gameStage1.getPossibleAnswers().get(0));
        assertEquals(p2,gameStage1.getPossibleAnswers().get(1));
        assertEquals(p3,gameStage1.getPossibleAnswers().get(2));
        assertEquals(p4,gameStage1.getPossibleAnswers().get(3));

        GameStage gameStage2 = questions.get(1);
        assertEquals(q+"2",gameStage2.getQuestion());
        assertEquals(p4,gameStage2.getPossibleAnswers().get(0));
        assertEquals(p3,gameStage2.getPossibleAnswers().get(1));
        assertEquals(p2,gameStage2.getPossibleAnswers().get(2));
        assertEquals(p1,gameStage2.getPossibleAnswers().get(3));
    }

    @Test
    void toGameStageListFromMapTest() {

        final String KEY_QUESTIONS_NUMBER = "qNum";
        final String KEY_QUESTIONS_FORMAT = "q%s";
        final String KEY_POSSIBLE_ANSWER_FORMAT = "p%d_%d";

        Map<String,String> map = new HashMap<String, String>();
        map.put(KEY_QUESTIONS_NUMBER,"1");
        map.put(String.format(KEY_QUESTIONS_FORMAT, 1),"q");
        map.put(String.format(KEY_POSSIBLE_ANSWER_FORMAT,1, 1),"p1");
        map.put(String.format(KEY_POSSIBLE_ANSWER_FORMAT,1, 2),"p2");
        map.put(String.format(KEY_POSSIBLE_ANSWER_FORMAT,1, 3),"p3");
        map.put(String.format(KEY_POSSIBLE_ANSWER_FORMAT,1, 4),"p4");

        List<GameStage> gameStages = Converter.toGameStageList(map);

        assertEquals(1,gameStages.size());
        assertEquals("q",gameStages.get(0).getQuestion());
        assertEquals("p1",gameStages.get(0).getPossibleAnswers().get(0));
        assertEquals("p2",gameStages.get(0).getPossibleAnswers().get(1));
        assertEquals("p3",gameStages.get(0).getPossibleAnswers().get(2));
        assertEquals("p4",gameStages.get(0).getPossibleAnswers().get(3));

        // converting back to map and comparing
        Map<String,String> returnedMap = Converter.toGameData(gameStages).getContent();
        assertEquals(map.keySet().size(),returnedMap.keySet().size());
        for(String key:map.keySet()){
            assertEquals(map.get(key),returnedMap.get(key));
        }
    }
}