package components;

import java.util.HashMap;

import utilities.Utils.ComponentType;

public class Card extends Component {

    //private int occurenceCount; //This was here once. Not sure why?

    public Card(){
        this.properties = new HashMap<>();
        super.type = ComponentType.CARD;
    }

    public Card copy(){
        Card copy = new Card();
        copy.type = type;

        copyComponentTo(copy);

        return copy;
    }

}