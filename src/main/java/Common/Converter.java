package Common;

import com.google.gson.Gson;

import java.util.*;

public class Converter {

    private static final String KEY_QUESTIONS_NUMBER = "qNum";
    private static final String KEY_QUESTIONS_FORMAT = "q%s";
    private static final String KEY_POSSIBLE_ANSWER_FORMAT = "p%d_%d";
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
            data.put( String.format(KEY_QUESTIONS_FORMAT,i) , gs.getQuestion());

            List<String> pAnswers = gs.getPossibleAnswers();
            for( int j = 0; j<4 ; j++) {
                data.put(String.format(KEY_POSSIBLE_ANSWER_FORMAT,i,j+1) , pAnswers.get(j));
            }
            i++;
        }
        data.put(KEY_QUESTIONS_NUMBER, String.valueOf(i-1));
        return data;
    }

    public static List<GameStage> toGameStageList(Map<String,String> data){
        List<GameStage> gameStages = new ArrayList<GameStage>();
        int question_number = Integer.parseInt(data.get(KEY_QUESTIONS_NUMBER));
        for (int i = 0; i<question_number; i++ ){
            String question = data.get( String.format(KEY_QUESTIONS_FORMAT,i+1));

            List<String> possibleAnswers = new ArrayList<String>();
            for(int j = 0; j<4 ; j++){
                possibleAnswers.add(data.get(String.format(KEY_POSSIBLE_ANSWER_FORMAT,i+1,j+1)));
            }

            GameStage gs = new GameStage(question,possibleAnswers,"UNKNOWN");
            gameStages.add(gs);
        }
        return gameStages;
    }

    public static List<GameStage> toGameStageList(String s){

        List<GameStage> gameStages = new ArrayList<GameStage>();
        String[] questions = s.split(";");
        for (String q : questions) {
            String[] sp = q.split(":");
            String question = sp[0];

            String[] pp = sp[1].split(",");
            String answer = pp[0];
            List<String> possibleAnswers = Arrays.asList(pp);
            Collections.shuffle(possibleAnswers);
            GameStage gameStage = new GameStage(question, possibleAnswers, answer);

            gameStages.add(gameStage);
        }
        return gameStages;
    }

    public static GameData toGameData(List<GameStage> gameStages){
        return new GameData(GameData.GameDataType.DATA,toMap(gameStages));

    }
}
