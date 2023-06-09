/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Cinémathèque
 Author: Edwin HELET & Julien GUY
 Class: NamedIdElement
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.tmdb.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NamedIdElement extends IdElement {

    @JsonProperty("name")
    private String name;

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName() + " [" + getId() + "]";
    }

}
