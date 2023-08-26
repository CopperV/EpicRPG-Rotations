package me.Vark123.EpicRPGRotations.RotationSystem;

import org.bukkit.Bukkit;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import me.Vark123.EpicRPGRotations.Main;
import me.nikl.calendarevents.CalendarEventsApi;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Rotation {

	private String rotationId;
	private String rotationDate;
	private String rotationHour;
	
	private RotationType rotationType;
	private String rotationFile;
	
	public void addCalendarEvent() {
		CalendarEventsApi calendar = Main.getInst().getCalendar();
		
		if(calendar.isRegisteredEvent(rotationId))
			calendar.removeEvent(rotationId);
		
		calendar.addEvent(rotationId, rotationDate, rotationHour);
	}
	
	public void doRotation() {
		Bukkit.broadcastMessage("Doing rotation");
	}
	
}
