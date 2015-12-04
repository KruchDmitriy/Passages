package Exceptions;

import java.rmi.RemoteException;

/**
 * Created by VladVin on 03.12.2015.
 */
public class ServerRemoteException extends RemoteException {
    public enum Code {NONE, ROOM_IS_FULL, BAD_PLAYER_COLOR,
        PLAYER_NOT_REGISTERED, GAME_NOT_STARTED, ROOM_NOT_CREATED}

    private Code code;

    public ServerRemoteException(Code code) {
        this.code = code;
    }

    public Code getCode() {
        return code;
    }
}
