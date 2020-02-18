package GeoChallengeClient;
public interface IGeoChallengeCore extends Runnable {

    void send(String s);

    void registerHandler(IResponseHandler responseHandler);

}
