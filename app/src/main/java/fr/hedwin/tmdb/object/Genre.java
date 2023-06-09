/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Cinémathèque
 Author: Edwin HELET & Julien GUY
 Class: Genre
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.tmdb.object;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.hedwin.tmdb.model.NamedIdElement;

import java.util.List;

public class Genre extends NamedIdElement {

    public static class GenreList {

        @JsonProperty("genres")
        private List<Genre> genres;

        public List<Genre> getGenres() {
            return genres;
        }

    }

}

