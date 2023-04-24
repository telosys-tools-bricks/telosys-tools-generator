package org.telosys.tools.generator.languages.types;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests with Java 8+ temporal types 
 *
 */
public class JavaTemporalTypesTest  {

	/*
	 * LocalDate (immutable) :
	 * Day, Month and Year 
	 */
	@Test
	public void testLocalDate() {
		
		LocalDate date ;
		date = LocalDate.parse("2017-11-26");
		assertEquals(26, date.getDayOfMonth() );
		assertEquals(11, date.getMonthValue() );
		assertEquals(2017, date.getYear() );
		assertEquals("2017-11-26", date.toString());
		assertFalse(date.isLeapYear());

		date = LocalDate.parse("1976-02-10");
		assertEquals(10, date.getDayOfMonth() );
		assertEquals(41, date.getDayOfYear() ); // 31 + 10
		assertTrue(date.isLeapYear());
		
		date = LocalDate.of(2021, 12, 4); 
		assertEquals(4, date.getDayOfMonth() );
		assertEquals(12, date.getMonthValue() );
		assertEquals(2021, date.getYear() );
		date = date.plusDays(2);
		assertEquals(6, date.getDayOfMonth() );
		date = date.minusDays(3);
		assertEquals(3, date.getDayOfMonth() );
		
		date = LocalDate.of(-588, 12, 4); // negative value accepted for year
		assertEquals(4, date.getDayOfMonth() );
		assertEquals(12, date.getMonthValue() );
		assertEquals(-588, date.getYear() );
		
		// comparison :
		assertTrue(LocalDate.of(2021, 12, 4).equals(  LocalDate.of(2021, 12,  4)) ); // equals OK
		assertTrue(LocalDate.of(2021, 12, 4).isAfter( LocalDate.of(2021, 12,  3)) );
		assertTrue(LocalDate.of(2021, 12, 4).isBefore(LocalDate.of(2021, 12, 12)) );
		assertTrue(LocalDate.of(2021, 12, 4).isEqual( LocalDate.of(2021, 12,  4)) );
	}	

	@Test(expected=java.time.DateTimeException.class)
	public void testLocalDateError() {
		LocalDate.of(2021, 02, 31); // Invalid date 'FEBRUARY 31' 
	}
	
	/*
	 * LocalTime (immutable) :
	 * Hour, Min, Seconds and Nanoseconds 
	 * eg : "13:45.30.123456789"
	 * This class does not store or represent a date or time-zone.
	 */
	@Test
	public void testLocalTime() {
		LocalTime time ;
		time = LocalTime.parse("15:30:45");
		assertEquals(15, time.getHour() );
		assertEquals(30, time.getMinute() );
		assertEquals(45, time.getSecond() );
		assertEquals( 0, time.getNano() );

		time = LocalTime.of(22, 12);
		assertEquals(22, time.getHour() );
		assertEquals(12, time.getMinute() );
		assertEquals( 0, time.getSecond() );
		assertEquals( 0, time.getNano() );

		time = LocalTime.of(18, 47, 24, 555);
		assertEquals( 18, time.getHour() );
		assertEquals( 47, time.getMinute() );
		assertEquals( 24, time.getSecond() );
		assertEquals(555, time.getNano() );

		// comparison with equals OK :
		assertTrue(LocalTime.of(22, 12          ).equals(  LocalTime.of(22, 12,  0       )) ); // equals OK
		assertTrue(LocalTime.of(22, 12, 56      ).equals(  LocalTime.of(22, 12, 56       )) ); // equals OK
		assertTrue(LocalTime.of(22, 12, 56, 6789).equals(  LocalTime.of(22, 12, 56, 6789 )) ); // equals OK
		
		assertTrue(LocalTime.of(18, 12, 4).isAfter( LocalTime.of(18, 12,  3)) );
		assertTrue(LocalTime.of(18, 12, 4).isBefore(LocalTime.of(18, 16, 12)) );
		//assertTrue(LocalTime.of(18, 12, 4).isEqual( LocalTime.of(18, 12,  4)) ); // no method
	}
	
	@Test(expected=java.time.DateTimeException.class)
	public void testLocalTimeError() {
		LocalTime.of(18, 67, 24 ); 
		// Invalid value for MinuteOfHour (valid values 0 - 59): 67
	}
	
	/*
	 * LocalDateTime (immutable) :
	 * Day, Month, Year, Hour, Min, Sec and NanoSec
	 * This class does not store or represent a time-zone.
	 */
	@Test
	public void testLocalDateTime() {
		
		LocalDateTime  dt ;
		dt = LocalDateTime.parse("2017-11-15T08:22:12");
		assertEquals(2017, dt.getYear() );
		assertEquals(  11, dt.getMonthValue() );
		assertEquals(  15, dt.getDayOfMonth() );
		assertEquals(   8, dt.getHour() );
		assertEquals(  22, dt.getMinute() );
		assertEquals(  12, dt.getSecond() );
		assertEquals(   0, dt.getNano() );
		
		dt = LocalDateTime.parse("2017-11-15T08:22:12.000001234"); // nano with 9 digits
		assertEquals(2017, dt.getYear() );
		assertEquals(  11, dt.getMonthValue() );
		assertEquals(  15, dt.getDayOfMonth() );
		assertEquals(   8, dt.getHour() );
		assertEquals(  22, dt.getMinute() );
		assertEquals(  12, dt.getSecond() );
		assertEquals(1234, dt.getNano() );
		
		dt = LocalDateTime.of(2019, 7, 14, 7, 56, 45, 123456);
		assertEquals(  2019, dt.getYear() );
		assertEquals(     7, dt.getMonthValue() );
		assertEquals(    14, dt.getDayOfMonth() );
		assertEquals(     7, dt.getHour() );
		assertEquals(    56, dt.getMinute() );
		assertEquals(    45, dt.getSecond() );
		assertEquals(123456, dt.getNano() );
	}
	
	/*
	 * Instant : instantaneous point on the time-line.
	 * the class stores a
	 *  - a long representing epoch-seconds 
	 *  - an int representing nanosecond-of-second, which will always be between 0 and 999,999,999.
	 * The epoch-seconds are measured from the standard Java epoch of "1970-01-01T00:00:00Z"
	 * where instants after the epoch have positive values, and earlier instants have negative values.
	 */
	@Test
	public void testInstant() {
		
		Instant  i = Instant.now();

		// Gets the number of seconds from the Java epoch of 1970-01-01T00:00:00Z. 
		long epochSec = i.getEpochSecond(); 
		
		// Gets the number of nanoseconds, later along the time-line, from the startof the second. 
		int nano = i.getNano();
		
	}
	
	//------------------------------------------------------------------------------------
	// WITH "OFFSET" ( Time Zone )
	//------------------------------------------------------------------------------------
	@Test
	public void testOffsetDateTime() {
		
		OffsetDateTime  dt ;
		//dt = OffsetDateTime.parse("2017-11-15T08:22:12"); // parsing error
		dt = OffsetDateTime.parse("2017-11-15T08:22:12+01:00"); // with offset : OK
		assertEquals(2017, dt.getYear() );
		assertEquals(  11, dt.getMonthValue() );
		assertEquals(  15, dt.getDayOfMonth() );
		assertEquals(   8, dt.getHour() );
		assertEquals(  22, dt.getMinute() );
		assertEquals(  12, dt.getSecond() );
		assertEquals(   0, dt.getNano() );
		ZoneOffset offset = dt.getOffset() ;
		assertEquals(3600, offset.getTotalSeconds()); // Offset : +1:00 = 3600 sec
		
		OffsetDateTime dt5 = OffsetDateTime.parse("2017-11-15T08:22:12+05:00");
		
		assertFalse( dt5.equals(dt) ); // equals if same datetime and same offset => use isEqual()
		
		// Same instant on timeline :
		// 2008-12-03T09:00-01:00  (  9h UTC-1 )
		// 2008-12-03T10:00+00:00  ( 10h UTC+0 )
		// 2008-12-03T11:00+01:00  ( 11h UTC+1 )
		// 2008-12-03T12:00+02:00  ( 12h UTC+2 )
		// see https://www.timeanddate.com/time/map/
		OffsetDateTime dt10 = OffsetDateTime.parse("2008-12-03T10:00+00:00");
		OffsetDateTime dt11 = OffsetDateTime.parse("2008-12-03T11:00+01:00");
		OffsetDateTime dt12 = OffsetDateTime.parse("2008-12-03T12:00+02:00");
		OffsetDateTime dt09 = OffsetDateTime.parse("2008-12-03T09:00-01:00");

		assertFalse( dt10.compareTo(dt11) == 0 );
		assertFalse( dt10.compareTo(dt09) == 0 );
		
		assertTrue( dt10.isEqual(dt11) ); // same instant
		assertTrue( dt10.isEqual(dt12) ); // same instant
		assertTrue( dt10.isEqual(dt09) ); // same instant
		//dt1.isAfter(other)
	}
}
