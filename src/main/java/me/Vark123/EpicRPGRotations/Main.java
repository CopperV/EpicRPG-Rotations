package me.Vark123.EpicRPGRotations;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import me.nikl.calendarevents.CalendarEvents;
import me.nikl.calendarevents.CalendarEventsApi;

@Getter
public class Main extends JavaPlugin {
	
	@Getter
	private static Main inst;

	private CalendarEventsApi calendar;
	
	@Override
	public void onEnable() {
		inst = this;
		
		ListenerManager.registerListeners();
		CommandManager.setExecutors();
		FileManager.init();
		
		CalendarEvents calend = (CalendarEvents) Bukkit.getPluginManager().getPlugin("CalendarEvents");
		calendar = calend.getApi();
	}

	@Override
	public void onDisable() {
		
	}

	
	
}
