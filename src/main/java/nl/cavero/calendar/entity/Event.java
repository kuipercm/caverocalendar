package nl.cavero.calendar.entity;

import java.time.LocalDateTime;

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
	private LocalDateTime start;
	private LocalDateTime end;
	private boolean allDay = false;
	
	private String description;
	private String location;
	
}
