package nl.cavero.calendar.entity.converter;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class ZonedDateTimeConverter implements AttributeConverter<ZonedDateTime, Date> {

	@Override
	public Date convertToDatabaseColumn(ZonedDateTime attribute) {
		return Date.from(attribute.withZoneSameInstant(ZoneId.systemDefault()).toInstant());
	}

	@Override
	public ZonedDateTime convertToEntityAttribute(Date dbData) {
		return dbData.toInstant().atZone(ZoneId.systemDefault());
	}

}
