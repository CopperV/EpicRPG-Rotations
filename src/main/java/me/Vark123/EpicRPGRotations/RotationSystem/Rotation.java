package me.Vark123.EpicRPGRotations.RotationSystem;

import java.util.List;

import org.bukkit.Bukkit;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import me.Vark123.EpicRPGRotations.FileManager;
import me.Vark123.EpicRPGRotations.Main;
import me.Vark123.EpicRPGRotations.ItemSystem.AItem;
import me.Vark123.EpicRPGRotations.ItemSystem.ItemManager;
import me.Vark123.EpicRPGRotations.ItemSystem.ItemType;
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
		List<AItem> itemsToRotate;
		switch(rotationType) {
			case NORMAL:
				itemsToRotate = ItemManager.get().getRandomItems(9, ItemType.NORMAL);
				FileManager.saveRotation(rotationFile, itemsToRotate);
				break;
			case VIP:
				itemsToRotate = ItemManager.get().getRandomItems(9, ItemType.NORMAL);
				FileManager.saveRotation(rotationFile, itemsToRotate, .8);
				break;
			case SPONSOR:
				itemsToRotate = ItemManager.get().getRandomItems(8, ItemType.NORMAL);
				itemsToRotate.addAll(ItemManager.get().getRandomItems(1, ItemType.SPONSOR));
				FileManager.saveRotation(rotationFile, itemsToRotate);
				break;
			case KLAN:
				itemsToRotate = ItemManager.get().getRandomItems(9, ItemType.KLAN);
				for(int i = 9; i > 0; --i) {
					FileManager.saveRotation("klan"+i+".yml", itemsToRotate);
					itemsToRotate.remove(itemsToRotate.size()-1);
				}
				break;
		}
	}
	
}
