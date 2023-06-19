package com.sip.store.repositories;

import org.springframework.data.repository.CrudRepository;

import com.sip.store.entities.Contact;

public interface ContactRepository  extends CrudRepository<Contact, Long> {

}
