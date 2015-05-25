package nl.cavero.calendar.endpoints;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import nl.cavero.calendar.entity.Event;

@Path("/event")
public class EventEndpoint {
	@PersistenceContext(unitName = "CaveroCalendarDS")
	EntityManager em;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Event> getAllEvents() {
		CriteriaQuery<Event> query = createCandidateQuery();
		query.from(Event.class);
		return em.createQuery(query).getResultList();
	}
	
	@Path("/id/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Event getSingleEvent(@PathParam("id") Long id) {
		return em.find(Event.class, id);
	}

	@POST
	public void createNewEvent(Event event) {
		em.persist(event);
	}
	
	private CriteriaQuery<Event> createCandidateQuery() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		return cb.createQuery(Event.class);
	}
}
