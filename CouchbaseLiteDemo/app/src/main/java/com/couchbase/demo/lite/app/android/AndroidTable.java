package com.couchbase.demo.lite.app.android;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Defines how a table structure is looking like to use with E.G. the simple adapter
 *
 * Created by david on 23.05.14.
 */
public class AndroidTable {

    /**
     * The rows of the table
     */
    private List<Map<String, Object>> rows;


    /**
     * The default constructor
     */
    public AndroidTable()
    {
        rows = new ArrayList<Map<String, Object>>();
    }

    /**
     * To add a row
     * @param row
     */
    public void addRow(Map<String, Object> row)
    {
        rows.add(row);
    }

    /**
     * To get the rows
     * @return
     */
    public List<Map<String, Object>> getRows() {
        return rows;
    }

}
