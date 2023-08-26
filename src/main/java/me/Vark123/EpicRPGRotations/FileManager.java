package me.Vark123.EpicRPGRotations;

import java.io.File;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import me.Vark123.EpicRPGRotations.ItemSystem.AItem;
import me.Vark123.EpicRPGRotations.ItemSystem.ItemManager;
import me.Vark123.EpicRPGRotations.ItemSystem.ItemType;
import me.Vark123.EpicRPGRotations.ItemSystem.ItemImpl.KlanItem;
import me.Vark123.EpicRPGRotations.ItemSystem.ItemImpl.NormalItem;
import me.Vark123.EpicRPGRotations.ItemSystem.ItemImpl.SponsorItem;

public final class FileManager {

	private static final File normalRotations = new File(Main.getInst().getDataFolder(), "rotations-normal.yml");
	private static final File sponsorRotations = new File(Main.getInst().getDataFolder(), "rotations-sponsor.yml");
	private static final File klanRotations = new File(Main.getInst().getDataFolder(), "rotations-klan.yml");
	
	private FileManager() { }
	
	public static void init() {
		if(!Main.getInst().getDataFolder().exists())
			Main.getInst().getDataFolder().mkdir();
		
		Main.getInst().saveResource("config.yml", false);
		Main.getInst().saveResource("rotations-normal.yml", false);
		Main.getInst().saveResource("rotations-sponsor.yml", false);
		Main.getInst().saveResource("rotations-klan.yml", false);
		
		loadItems(normalRotations);
		loadItems(sponsorRotations);
		loadItems(klanRotations);
	}
	
	private static void loadItems(File file) {
		YamlConfiguration fYml = YamlConfiguration.loadConfiguration(file);
		ConfigurationSection section = fYml.getConfigurationSection("items");
		if(section == null)
			return;
		ItemType type = ItemType.valueOf(fYml.getString("type", "NORMAL").toUpperCase());
		section.getKeys(false).forEach(key -> {
			String mmId = section.getString(key+".id");
			int price = section.getInt(key+".price");
			AItem item;
			switch(type) {
				case NORMAL:
					item = new NormalItem(mmId, price);
					break;
				case SPONSOR:
					item = new SponsorItem(mmId, price);
					break;
				case KLAN:
					item = new KlanItem(mmId, price);
					break;
				default:
					return;
			}
			ItemManager.get().registerItem(item);
		});
	}
	
}
