package me.Vark123.EpicRPGRotations.ItemSystem;

import java.util.Random;

import org.bukkit.inventory.ItemStack;

import io.lumine.mythic.bukkit.MythicBukkit;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public abstract class AItem {

	private String mmId;
	private int price;
	private ItemType type;
	
	public ItemStack getItem() {
		ItemStack it = MythicBukkit.inst().getItemManager().getItemStack(mmId);
		return it;
	}
	
	public int getPrice() {
		Random rand = new Random();
		int toReturn;
		double tmp = (rand.nextInt((int)(price*1.2-price*0.8))+price*0.8)/10;
		toReturn = (int)tmp*10;
		return toReturn;
	}
	
	public int getPrice(double modifier) {
		Random rand = new Random();
		int toReturn;
		double tmpPrice = ((double)price)*modifier;
		double tmp = (rand.nextInt((int)(tmpPrice*1.2-tmpPrice*0.8))+tmpPrice*0.8)/10;
		toReturn = (int)tmp*10;
		return toReturn;
	}
	
}
