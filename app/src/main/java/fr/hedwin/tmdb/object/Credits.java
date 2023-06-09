/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Cinémathèque
 Author: Edwin HELET & Julien GUY
 Class: Credits
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.tmdb.object;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.hedwin.tmdb.model.IdElement;

import java.util.List;

public class Credits extends IdElement {

    @JsonProperty("cast")
    private List<Cast> cast;
    @JsonProperty("crew")
    private List<Crew> crew;

    public List<Cast> getCast() {
        return cast;
    }

    public List<Crew> getCrew() {
        return crew;
    }
}
