package Common;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ConverterTest {

    @Test
    void toMapTest() throws InterruptedException {
        List<GameStage> gameStageList = new ArrayList<GameStage>();

        int i =3;
        while(i>0) {
            List<String> pAnswers = new ArrayList<String>();
            pAnswers.add(String.format("%d",10*i+1));
            pAnswers.add(String.format("%d",10*i+2));
            pAnswers.add(String.format("%d",10*i+3));
            pAnswers.add(String.format("%d",10*i+4));
            GameStage gameStage = new GameStage(String.format("%d",10*i), pAnswers, String.format("%d",10*i));
            i--;
            gameStageList.add(gameStage);
        }

//        Converter.t

    }
}