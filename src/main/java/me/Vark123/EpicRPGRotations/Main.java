package me.Vark123.EpicRPGRotations;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import me.nikl.calendarevents.CalendarEvents;
import me.nikl.calendarevents.CalendarEventsApi;

@Getter
public class Main extends JavaPlugin {
	
	@Getter
	private static Main inst;

	private CalendarEventsApi calendar;
	private Plugin chestCommands;
	
	@Override
	public void onEnable() {
		inst = this;
		
		CalendarEvents calend = (CalendarEvents) Bukkit.getPluginManager().getPlugin("CalendarEvents");
		calendar = calend.getApi();
		
		chestCommands = Bukkit.getPluginManager().getPlugin("ChestCommands");
		
		ListenerManager.registerListeners();
		CommandManager.setExecutors();
		FileManager.init();
	}

	@Override
	public void onDisable() {
		
	}

	
	
}
