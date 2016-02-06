package com.tipsycoder.helper;

import java.sql.*;
import java.util.Vector;

/**
 * Created by TipsyCoder on 2/7/15.
 */
public class DatabaseHelper {
    private ResultSet resultSet;
    private Statement statement;
    private ResultSetMetaData metaData;
    private Vector<Object> columnNames = new Vector<Object>();
    private Vector<Object> data = new Vector<Object>();

    public ResultSet getResultSet() {
        return resultSet;
    }

    public void setResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    public Statement getStatement() {
        return statement;
    }

    public void setStatement(Statement statement) {
        this.statement = statement;
    }

    public ResultSetMetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(ResultSetMetaData metaData) {
        this.metaData = metaData;
    }

    public Vector<Object> getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(Vector<Object> columnNames) {
        this.columnNames = columnNames;
    }

    public Vector<Object> getData() {
        return data;
    }

    public void setData(Vector<Object> data) {
        this.data = data;
    }

    public void destroySQL() {
        try {
            if(resultSet != null)
                resultSet.close();
            if(statement != null)
                statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
