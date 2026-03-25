package com.juanestevez;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/api/contact")
@Produces("application/json")
@Consumes
public class ContactResource {

    @Inject EntityManager em;

    @GET
    public List<Contact> getAllContacts() {
        return em.createQuery("FROM Contact", Contact.class).getResultList();
    }

    @GET
    @Path("/{id}")
    public Response getContactById(@PathParam("id") Long id) {
        Contact contact = em.find(Contact.class, id);
        if (contact == null) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(contact).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateContact(@PathParam("id") Long id, Contact updatedContact) {
        Contact contact = em.find(Contact.class, id);
        if (contact == null) return Response.status(Response.Status.NOT_FOUND).build();
        contact.setName(updatedContact.getName());
        contact.setEmail(updatedContact.getEmail());
        em.merge(contact);
        return Response.ok(contact).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteContact(@PathParam("id") Long id) {
        Contact contact = em.find(Contact.class, id);
        if (contact == null) return Response.status(Response.Status.NOT_FOUND).build();
        em.remove(contact);
        return Response.noContent().build();
    }

    @PUT
    @Path("/email/{email}")
    @Transactional
    public Response updateContactByEmail(@PathParam("email") String email, Contact updatedContact) {
        Contact contact = em.createQuery("SELECT c FROM Contact c where c.email = :email", Contact.class)
                .setParameter("email", email)
                .getSingleResult();

        if (contact == null) return Response.status(Response.Status.NOT_FOUND).build();
        contact.setName(updatedContact.getName());
        contact.setEmail(updatedContact.getEmail());
        em.merge(contact);
        return Response.ok(contact).build();
    }

    @GET
    @Path("/email/{email}")
    public Response getContactByEmail(@PathParam("email") String email) {
        Contact contact = em.createQuery("SELECT c FROM Contact c where c.email = :email", Contact.class)
                .setParameter("email", email)
                .getSingleResult();

        if (contact == null) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(contact).build();
    }

    @POST
    @Transactional
    public Response createContact(Contact contact) {
        em.persist(contact);
        return Response.status(Response.Status.CREATED).entity(contact).build();
    }

}
