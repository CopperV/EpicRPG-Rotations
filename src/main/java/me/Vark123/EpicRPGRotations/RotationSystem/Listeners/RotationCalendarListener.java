package me.Vark123.EpicRPGRotations.RotationSystem.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPGRotations.RotationSystem.RotationManager;
import me.nikl.calendarevents.CalendarEvent;

public class RotationCalendarListener implements Listener {

	@EventHandler
	public void onDate(CalendarEvent e) {
		if(e.isCancelled())
			return;
		
		RotationManager.get().getRotations().stream()
			.filter(rotation -> e.getLabels().contains(rotation.getRotationId()))
			.forEach(rotation -> {
				rotation.doRotation();
			});
	}
	
}
