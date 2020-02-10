package com.pluralsight.conferencedemo.controllers;


/*
Controllers will handle our API endpoints, REST based
Add @RestController and @RequestMapping annotation so that spring MVC knows that this is a controller
@RestController - will respond to payloads incoming and outgoing as JSON REST endpoints
@RequestMapping - tells the router what the mapping URL will look like
 */

import com.pluralsight.conferencedemo.models.Session;
import com.pluralsight.conferencedemo.repository.SessionRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sessions")
public class SessionsController {

    /*
    Use Spring to inject or autowire the SessionRepository when SessionController is built
    It will create an instance of the SessionRepository and put it onto our class
     */
    @Autowired
    private SessionRepository sessionRepository;

    /*
    If you were to make a call to the api/v1/sessions, it will route to this method, the annotation GetMapping tells
    which HTTP verb to use, which will be a GET verb to call this endpoint
    Here we are using the sessionRepository and calling the findAll() on it. The JPA repositories built this method for us
    and it's going to query all of the sessions in the database and return them as a list of Session objects. Our data type
    is returning that List of Sessions and Spring MVC will pass that over to Jackson (which is a serialization library),
    which will turn those sessions into JSON and return them back to the caller
     */
    @GetMapping
    public List<Session> list() {
        return sessionRepository.findAll();
    }

    /*
    REST controller in Spring MVC return types: Default returns 200s as response status for all calls
    Even though @PostMapping is added to endpoint, it's not going to infer anything from that. Because when you Post something
    you typically get a 201 back, but Spring REST controller will just return a 200.  To override this, you can add
    @ResponseStatus annotation, you can specify the exact response that you want to occur when the method executes and finishes
    Ex:@ResponseStatus(HttpStatus.CREATED) which map to 201 in HTTP world
    Here the @RequestMapping is an addition to the class RequestMapping ("/api/v1/sessions/{id}"}
    The parameter on the GET is pulling it off of the URL and injecting it into the method automatically, handled by
    Spring MVC.  PathVariable data type must match incoming data type parameter
     */
    @GetMapping
    @RequestMapping("{id}")
    public Session get(@PathVariable Long id) {
        return sessionRepository.getOne(id);
    }

    @PostMapping
    public Session create(@RequestBody final Session session) {
        return sessionRepository.saveAndFlush(session);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable Long id) {
        //Also need to check for children records before deleting
        sessionRepository.deleteById(id);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public Session update(@PathVariable Long id, @RequestBody Session session) {
        //because this is a PUT, we expect all attributes to be passed in. A PATCH would only need portion of the attribute
        //TODO: Add validation that all attributes are passed in, otherwise return a 400 bad payload
        Session existingSession = sessionRepository.getOne(id);
        BeanUtils.copyProperties(session, existingSession, "session_id");
        return sessionRepository.saveAndFlush(existingSession);
    }

}
