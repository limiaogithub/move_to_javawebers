package com.github.yt.mybatis.handler;


public class SQLJoinHandler {
    public static enum JoinType {
        JOIN, INNER_JOIN, LEFT_OUTER_JOIN, RIGHT_OUTER_JOIN, OUTER_JOIN
    }

    /**
     * 查询的列
     */
    private String selectColumns;
    /**
     * 链接类型
     */
    private JoinType joinType;
    /**
     * join sql
     */
    private String joinSql;

    public SQLJoinHandler() {
    }

    public SQLJoinHandler(String selectColumns, JoinType joinType, String joinSql) {
        this.selectColumns = selectColumns;
        this.joinType = joinType;
        this.joinSql = joinSql;
    }

    public SQLJoinHandler(JoinType joinType, String joinSql) {
        this.joinType = joinType;
        this.joinSql = joinSql;
    }

    public String getSelectColumns() {
        return selectColumns;
    }

    public SQLJoinHandler setSelectColumns(String selectColumns) {
        this.selectColumns = selectColumns;
        return this;
    }

    public JoinType getJoinType() {
        return joinType;
    }

    public SQLJoinHandler setJoinType(JoinType joinType) {
        this.joinType = joinType;
        return this;
    }

    public String getJoinSql() {
        return joinSql;
    }

    public SQLJoinHandler setJoinSql(String joinSql) {
        this.joinSql = joinSql;
        return this;
    }
}
