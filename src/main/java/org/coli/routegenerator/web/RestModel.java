package org.coli.routegenerator.web;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;

@Data
public class RestModel extends ArrayList<String> {

    public RestModel(Collection<? extends String> c) {
        super(c);
    }
}
