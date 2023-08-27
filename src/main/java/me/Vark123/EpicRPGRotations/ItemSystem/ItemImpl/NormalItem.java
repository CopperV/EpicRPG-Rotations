package me.Vark123.EpicRPGRotations.ItemSystem.ItemImpl;

import me.Vark123.EpicRPGRotations.ItemSystem.AItem;
import me.Vark123.EpicRPGRotations.ItemSystem.ItemType;

public class NormalItem extends AItem {

	public NormalItem(String mmId, int price) {
		super(mmId, price, ItemType.NORMAL);
	}

}
