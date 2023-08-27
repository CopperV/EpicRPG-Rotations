package me.Vark123.EpicRPGRotations;

import org.bukkit.Bukkit;

import me.Vark123.EpicRPGRotations.RotationSystem.Listeners.RotationCalendarListener;

public final class ListenerManager {

	private ListenerManager() { }
	
	public static void registerListeners() {
		Main inst = Main.getInst();
		
		Bukkit.getPluginManager().registerEvents(new RotationCalendarListener(), inst);
	}
	
}
