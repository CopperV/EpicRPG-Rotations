package me.Vark123.EpicRPGRotations;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.nbtapi.NBTCompoundList;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.NBTListCompound;
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
	
	private static final DecimalFormat formatter = new DecimalFormat("#,###.00");
	
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
	
	public static void saveRotation(String fileName, List<AItem> items) {
		saveRotation(fileName, items, 1);
	}
	
	public static void saveRotation(String fileName, List<AItem> items, double modifier) {
		File file = new File(Main.getInst().getChestCommands().getDataFolder().getAbsolutePath()+"/menu", fileName);
		if(!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		YamlConfiguration fYml = YamlConfiguration.loadConfiguration(file);
		
		Bukkit.getLogger().log(Level.INFO, "ROTACJA: "+fileName);
		int n = 1;
		for(AItem item : items) {
			String number = "num"+n;
			fYml.set(number, null);
			
			ItemStack it = item.getItem();
			if(it == null)
				continue;
			
			int price = item.getPrice(modifier);
			
			String command = "console: rpgmm give {player} "+item.getMmId()+" 1";
			
			fYml.set(number+".ACTIONS", Arrays.asList(command));
			fYml.set(number+".NAME", it.getItemMeta().getDisplayName());
			fYml.set(number+".MATERIAL", it.getType().name());
			
			NBTItem nbt = new NBTItem(it);
			NBTCompoundList list = nbt.getCompoundList("AttributeModifiers");
			Map<String, Map<String, Double>> slots = new ConcurrentHashMap<>();
			
			for(int i = 0; i < list.size(); ++i) {
				NBTListCompound lc = list.get(i);
				String slot = lc.getString("Slot");
				Map<String, Double> _slots = slots.containsKey(slot) ? slots.get(slot) : new ConcurrentHashMap<>();
				switch(lc.getString("Name").toLowerCase().replace("generic.", "")) {
					case "max_health":
						_slots.put("health", lc.getDouble("Amount"));
						break;
					case "knockback_resistance":
						_slots.put("knock", lc.getDouble("Amount"));
						break;
					case "movement_speed":
						_slots.put("speed", lc.getDouble("Amount"));
						break;
				}
				if(!_slots.isEmpty()) slots.put(slot, _slots);
			}
			
			List<String> lore = it.getItemMeta().getLore();
			lore.add(" ");
			lore.add("§7Cena: §e"+formatter.format(price)+"$");
			
			if(!slots.isEmpty()) lore.add(" ");
			
			for(String slot : slots.keySet()) {
				
				switch(slot.toLowerCase()) {
					case "mainhand":
						lore.add("§7Kiedy w glownej rece:");
						break;
					case "offhand":
						lore.add("§7Kiedy w drugiej rece:");
						break;
					case "feet":
						lore.add("§7Kiedy na stopach:");
						break;
					case "legs":
						lore.add("§7Kiedy na nogach:");
						break;
					case "chest":
						lore.add("§7Kiedy na torsie:");
						break;
					case "head":
						lore.add("§7Kiedy na glowie:");
						break;
				}
				
				
				for(String stat : slots.get(slot).keySet()) {
					String line;
					switch(stat.toLowerCase()) {
						case "health":
							double health = slots.get(slot).get(stat);
							if(health == 0) break;
							if(health < 0) line = "§c";
							else line = "§9+";
							line += (int)health+" do maksymalnego zdrowia";
							lore.add(line);
							break;
						case "knock":
							double knock = slots.get(slot).get(stat);
							if(knock == 0) break;
							if(knock < 0) line = "§c";
							else line = "§9+";
							line += (int)(knock*100)+"% do odpornosci na odrzuty";
							lore.add(line);
							break;
						case "speed":
							double speed = slots.get(slot).get(stat);
							if(speed == 0) break;
							if(speed < 0) line = "§c";
							else line = "§9+";
							line += (int)(speed*100)+"% do Szybkosc";
							lore.add(line);
							break;
					}
				}
				
			}
			
			fYml.set(number+".LORE", lore);
			
			fYml.set(number+".PRICE", price);
			fYml.set(number+".AMOUNT", 1);
			fYml.set(number+".POSITION-X", n);
			fYml.set(number+".POSITION-Y", 1);
			
			++n;
		}
		
		try {
			fYml.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Bukkit.getScheduler().runTaskLater(Main.getInst(), ()->{
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "chestcommands reload");
		}, 20);
	}
	
}
