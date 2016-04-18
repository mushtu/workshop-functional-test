package com.mammutgroup.workshop.test;

import com.google.gson.Gson;

/**
 * @author mushtu
 * @since 4/17/16.
 */
public class Util {

    public static String toJson(Object object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }


}
