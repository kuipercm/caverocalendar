package nl.cavero.calendar.endpoints;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import nl.cavero.calendar.entity.Event;

@Path("/event")
@Stateless
public class EventEndpoint {
	@PersistenceContext(unitName = "CaveroCalendarDS")
	EntityManager em;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Event> getAllEvents(@QueryParam("start") String start, @QueryParam("end") String end) {
		CriteriaQuery<Event> query = createCandidateQuery();
		Root<Event> root = query.from(Event.class);
		
		List<Predicate> predicates = new ArrayList<Predicate>();
		//Please note that to avoid issues with timezone differences between server and client,
		//the dates are retrieved for a wider range than given by the client.
		if (start != null) {
			predicates.add(em.getCriteriaBuilder().greaterThanOrEqualTo(
					root.<ZonedDateTime>get("start"), 
					LocalDate.parse(start, DateTimeFormatter.ISO_LOCAL_DATE).atStartOfDay(ZoneId.systemDefault()).minusDays(1)));
		}
		if (end != null) {
			predicates.add(em.getCriteriaBuilder().lessThanOrEqualTo(
					root.<ZonedDateTime>get("end"), 
					LocalDate.parse(end, DateTimeFormatter.ISO_LOCAL_DATE).atStartOfDay(ZoneId.systemDefault()).plusDays(2)));
		}
		
		if (!predicates.isEmpty()) {
			query.where(predicates.toArray(new Predicate[predicates.size()]));
		}
		
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
	
	@Path("/id/{id}")
	@PUT
	public void updateOrCreateSingleEvent(@PathParam("id") Long id, Event updated) {
		Event event = em.find(Event.class, id);
		if (event != null) {
			event.setTitle(updated.getTitle());
			event.setStart(updated.getStart());
			event.setEnd(updated.getEnd());
			event.setDescription(updated.getDescription());
			event.setLocation(updated.getLocation());
			em.merge(event);
		}
		else {
			//create new if doesn't exist
			em.persist(event);
		}
	}
	
	@Path("/id/{id}")
	@DELETE
	public void deleteSingleEvent(@PathParam("id") Long id) {
		Event event = em.find(Event.class, id);
		em.remove(event);
	}
	
	private CriteriaQuery<Event> createCandidateQuery() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		return cb.createQuery(Event.class);
	}
}
