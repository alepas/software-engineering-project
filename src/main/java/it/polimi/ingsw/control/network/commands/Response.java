package it.polimi.ingsw.control.network.commands;

import java.io.Serializable;

public interface Response extends Serializable {
    void handle(ResponseHandler handler) throws Exception;
}
