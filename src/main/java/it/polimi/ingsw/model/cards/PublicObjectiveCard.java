package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.clientModel.ClientPoc;
import it.polimi.ingsw.model.wpc.Wpc;

import java.util.ArrayList;

public abstract class PublicObjectiveCard {
    protected String id;
    protected String name;
    protected String description;

    public static ArrayList<PublicObjectiveCard> publicObjectiveCards = new ArrayList<>();

    public String getID(){ return id; }

    public String getName() { return name; }

    public String getDescription() { return description; }

    public abstract int calculateScore(Wpc wpc);

    public ClientPoc getClientPoc(){
        return new ClientPoc(id, name, description);
    }
}
