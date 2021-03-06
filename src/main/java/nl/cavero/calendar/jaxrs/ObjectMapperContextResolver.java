package nl.cavero.calendar.jaxrs;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.PackageVersion;
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;

@Provider
public class ObjectMapperContextResolver implements ContextResolver<ObjectMapper> {
	private final ObjectMapper mapper;
	
	public ObjectMapperContextResolver() {
		mapper = new ObjectMapper();
		mapper.registerModule(new CustomDateParsingModule());
	}
	
	
	@Override
	public ObjectMapper getContext(Class<?> type) {
		return mapper;
	}

	private static class CustomDateParsingModule extends SimpleModule {
		private static final long serialVersionUID = 1L;

		public CustomDateParsingModule() {
			super(PackageVersion.VERSION);
			
			addDeserializer(ZonedDateTime.class, InstantDeserializer.ZONED_DATE_TIME);
			addSerializer(ZonedDateTime.class, ZonedDateTimeSerializer.INSTANCE);
		}
		
		
	}
	
	/**
	 * The JSR310 serializer (counterpart to the deserializer mentioned above) does a poor job
	 * when writing a date to the front-end: the timezone data is messed up and not the standard json format.
	 */
	private static class ZonedDateTimeSerializer extends JsonSerializer<ZonedDateTime> {
		public static final ZonedDateTimeSerializer INSTANCE = new ZonedDateTimeSerializer();
		
		@Override
		public void serialize(ZonedDateTime value, JsonGenerator generator, SerializerProvider serializers) throws IOException, JsonProcessingException {
			generator.writeString(DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(value));
		}
		
	}
	
}
