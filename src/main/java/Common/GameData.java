package Common;

import java.util.HashMap;
import java.util.Map;

public class GameData {

    private Map<String,String> content;
    private GameDataType type;

    public enum GameDataType{
        ACK,
        DATA,
        UPDATE,
        GRADE,
        END,
        ERROR

    }

    public GameData(GameDataType type, String content){
        this.type = type;
        this.content = new HashMap<String, String>();
        this.content.put("msg",content);
    }

    public GameData(GameDataType type, Map<String,String> content){
        this.type = type;
        this.content = content;
    }

    public GameDataType getType() {
        return type;
    }

    public Map<String,String> getContent() {
        return content;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
