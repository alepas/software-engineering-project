package it.polimi.ingsw.model.cards.concretePublicObjectiveCards;

import it.polimi.ingsw.model.WPC.WPC;
import it.polimi.ingsw.model.cards.PublicObjectiveCard;

public class PublicObjectiveCard8  extends PublicObjectiveCard {

    public PublicObjectiveCard8(){
        this.id = "8";
    }

    @Override
    public int calculateScore(WPC wpc) {
        return 0;
    }
}
