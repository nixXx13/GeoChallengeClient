package Common;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Converter {
    // TODO - add UT
    private static Gson gson = new Gson();

    public static String toJson(GameData gameData){
        return gson.toJson(gameData);
    }

    public static String toJson(List<GameStage> gameStages) {
        GameData gameData = toGameData(gameStages);
        return toJson(gameData);
    }

    private static Map<String,String> toMap(List<GameStage> gameStages){
        Map<String,String> data = new HashMap<String, String>();
        int i = 1;
        for(GameStage gs : gameStages){

            data.put( "q"+i , gs.getQuestion());
            // TODO - strip answer from data sent to client. Send possible answers only.
            data.put( "a"+i,  gs.getAnswer());
            i++;
        }
        return data;
    }

    public static List<GameStage> toGameStageList(Map<String,String> map){
        return null;
    }

    public static GameData toGameData(List<GameStage> gameStages){
        return new GameData(GameData.GameDataType.DATA,toMap(gameStages));

    }
}
