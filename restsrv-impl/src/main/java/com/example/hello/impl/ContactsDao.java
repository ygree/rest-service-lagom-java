package com.example.hello.impl;

import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface ContactsDao {

    @SqlUpdate("insert into contacts(name) values(:name)")
    void insertContact(String name);

}
