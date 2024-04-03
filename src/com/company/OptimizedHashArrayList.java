package com.company;

import java.util.ArrayList;

public class OptimizedHashArrayList<E> extends ArrayList<E> {
    @Override
    public int hashCode() {
        int hash = 1;
        for (E i : this) {
            hash = 31 * hash + ((int)i + 1);
        }
        return hash;
    }
}

