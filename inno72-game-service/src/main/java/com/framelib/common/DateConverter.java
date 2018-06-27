package com.framelib.common;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Converter(autoApply = true)
public class DateConverter implements AttributeConverter<LocalDateTime, Date> {

	@Override
	public Date convertToDatabaseColumn(LocalDateTime date) {
		if (date == null) {
			return null;
		}
		Instant instant = date.atZone(ZoneId.systemDefault()).toInstant();
		return Date.from(instant);
	}

	@Override
	public LocalDateTime convertToEntityAttribute(Date date) {
		if (date == null) {
			return null;
		}
		Instant instant = Instant.ofEpochMilli(date.getTime());

		return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
	}
}