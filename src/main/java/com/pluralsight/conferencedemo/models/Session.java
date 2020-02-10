package com.pluralsight.conferencedemo.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.List;

/*
Class annotation identify it as JPA entity
Sessions (plural) name of database table
Session (singular) class name - one instance or row of that data
 */

@Entity(name = "sessions")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Session {

    /*
    Name, spelling and format of these java attributes is the same as the database columns
    by doing this, JPA will auto-bind to those columns, do not need to annotate

    @Id annotation specifies which attribute is the primary key
    @GeneratedValue annotation specifies how the primary key gets populated on a new record insert
    By using the IDENTITY strategy, JPA will utilize the Postgres created sequence for primary key values
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long session_id;

    private String session_name;
    private String session_description;
    private Integer session_length;

    /*
    Pick one side of relationship to be the owner or main definition point of the relationship
    Here the Session class was chosen
    Add list of associated speakers by adding a new attribute below - add getter and setter
    Define the relationship by telling JPA how this works, by adding the following annotations
    @ManyToMany annotation - indicates a many to many relationship, and that you have a mapping jointable in your database
    @JoinTable - defines the join table, and the foreign key columns
    JPA will setup the SQL join automatically, when you make a call to the speakers attribute

     */
    @ManyToMany
    @JoinTable(
            name = "session_speakers",
            joinColumns = @JoinColumn(name = "session_id"),
            inverseJoinColumns = @JoinColumn(name = "speaker_id"))
    private List<Speaker> speakers;


    //basic default constructor to all entities - helps with serialization and deserialization
    public Session() {

    }

    public Long getSession_id() {
        return session_id;
    }

    public List<Speaker> getSpeakers() {
        return speakers;
    }

    public void setSpeakers(List<Speaker> speakers) {
        this.speakers = speakers;
    }

    public void setSession_id(Long session_id) {
        this.session_id = session_id;
    }

    public String getSession_name() {
        return session_name;
    }

    public void setSession_name(String session_name) {
        this.session_name = session_name;
    }

    public String getSession_description() {
        return session_description;
    }

    public void setSession_description(String session_description) {
        this.session_description = session_description;
    }

    public Integer getSession_length() {
        return session_length;
    }

    public void setSession_length(Integer session_length) {
        this.session_length = session_length;
    }
}
