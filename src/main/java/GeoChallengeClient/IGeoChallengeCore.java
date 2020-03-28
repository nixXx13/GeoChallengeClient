package GeoChallengeClient;
import Common.GameData;

public interface IGeoChallengeCore extends Runnable {

    void send(GameData gameData);

    void registerHandler(IResponseHandler responseHandler);

    // TODO - add disconnect
}
