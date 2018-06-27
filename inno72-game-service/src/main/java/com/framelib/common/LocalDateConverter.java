package com.framelib.common;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Converter(autoApply = true)
public class LocalDateConverter implements AttributeConverter<LocalDate, Date> {

	@Override
	public Date convertToDatabaseColumn(LocalDate date) {
		if (date == null) {
			return null;
		}

		ZoneId zone = ZoneId.systemDefault();
		Instant instant = date.atStartOfDay().atZone(zone).toInstant();
		return Date.from(instant);
	}

	@Override
	public LocalDate convertToEntityAttribute(Date date) {
		if (date == null) {
			return null;
		}
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}
}