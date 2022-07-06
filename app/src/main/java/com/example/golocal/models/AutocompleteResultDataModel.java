package com.example.golocal.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.List;

@ParseClassName("AutocompleteResults")
public class AutocompleteResultDataModel extends ParseObject {

    public static final String KEY_BUSINESS_ARRAY = "resultBusinesses";
    public static final String KEY_QUERY_TEXT = "queryText";

    public AutocompleteResultDataModel() {}

    public List<BusinessDataModel> getResultBusinesses() {
        return getList(KEY_BUSINESS_ARRAY);
    }

    public void setResultBusinesses(List<BusinessDataModel> resultBusinesses) {
        put(KEY_BUSINESS_ARRAY, resultBusinesses);
    }

    public String getQueryText() {
        return getString(KEY_QUERY_TEXT);
    }

    public void setQueryText(String query) {
        put(KEY_QUERY_TEXT, query);
    }
}
