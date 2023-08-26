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
import me.Vark123.EpicRPGRotations.RotationSystem.Rotation;
import me.Vark123.EpicRPGRotations.RotationSystem.RotationManager;
import me.Vark123.EpicRPGRotations.RotationSystem.RotationType;

public final class FileManager {

	private static final File config = new File(Main.getInst().getDataFolder(), "config.yml");
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
		
		loadRotations();
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
	
	private static void loadRotations() {
		YamlConfiguration fYml = YamlConfiguration.loadConfiguration(config);
		ConfigurationSection section = fYml.getConfigurationSection("rotations");
		if(section == null)
			return;
		section.getKeys(false).forEach(key -> {
			String rotationId = section.getString(key+".id");
			String rotationDate = section.getString(key+".date");
			String rotationHour = section.getString(key+".hour");
			RotationType rotationType = RotationType.valueOf(section.getString(key+".type","NORMAL").toUpperCase());
			String rotationFile = section.getString(key+".file");
			
			Rotation rotation = new Rotation(rotationId, rotationDate, rotationHour, rotationType, rotationFile);
			RotationManager.get().registerRotation(rotation);
		});
	}
	
}
