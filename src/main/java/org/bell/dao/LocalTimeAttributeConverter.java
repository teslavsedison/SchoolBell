package org.bell.dao;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Time;
import java.time.LocalTime;

@Converter(autoApply = true)
public class LocalTimeAttributeConverter implements AttributeConverter<LocalTime, Time> {

    public Time convertToDatabaseColumn(LocalTime localTime) {
        return (localTime == null ? null : Time.valueOf(localTime));
    }


    public LocalTime convertToEntityAttribute(Time sqlTime) {
        return (sqlTime == null ? null : sqlTime.toLocalTime());
    }
}
