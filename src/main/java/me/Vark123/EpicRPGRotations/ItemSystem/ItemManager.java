package me.Vark123.EpicRPGRotations.ItemSystem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import lombok.Getter;

@Getter
public final class ItemManager {

	private static final ItemManager inst = new ItemManager();
	
	private List<AItem> normalItems;
	private List<AItem> sponsorItems;
	private List<AItem> klanItems;
	
	private ItemManager() {
		normalItems = new ArrayList<>();
		sponsorItems = new ArrayList<>();
		klanItems = new ArrayList<>();
	}
	
	public static final ItemManager get() {
		return inst;
	}
	
	public void registerItem(AItem item) {
		switch(item.getType()) {
			case NORMAL:
				normalItems.add(item);
				break;
			case SPONSOR:
				sponsorItems.add(item);
				break;
			case KLAN:
				klanItems.add(item);
				break;
		}
	}
	
	public Collection<AItem> getRandomItems(int amount, ItemType type) {
		List<AItem> pick;
		switch(type) {
			case NORMAL:
				pick = normalItems;
				break;
			case SPONSOR:
				pick = sponsorItems;
				break;
			case KLAN:
				pick = klanItems;
				break;
			default:
				pick = normalItems;
				break;
		}
		
		if(pick.size() <= amount)
			return pick;
		
		Random rand = new Random();
		Collection<AItem> toReturn = new HashSet<>();
		while(amount > 0) {
			AItem random = pick.get(rand.nextInt(pick.size()));
			if(toReturn.contains(random))
				continue;
			toReturn.add(random);
			--amount;
		}
		return toReturn;
	}
	
}
