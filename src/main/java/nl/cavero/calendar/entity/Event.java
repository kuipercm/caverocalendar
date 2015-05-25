package nl.cavero.calendar.entity;

import java.time.ZonedDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import lombok.Data;

@Entity
@Data
@SequenceGenerator(name="eventSeq", sequenceName="eventsequence", allocationSize=1)
public class Event {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="eventSeq")
	private long id;
	
	private String title;
	private ZonedDateTime start;
	private ZonedDateTime end;
	private boolean allDay = false;
	private String url;
	
	private String description;
	private String location;
	
}
