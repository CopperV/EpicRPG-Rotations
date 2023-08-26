package me.Vark123.EpicRPGRotations.ItemSystem.ItemImpl;

import me.Vark123.EpicRPGRotations.ItemSystem.AItem;
import me.Vark123.EpicRPGRotations.ItemSystem.ItemType;

public class SponsorItem extends AItem {

	public SponsorItem(String mmId, int price) {
		super(mmId, price, ItemType.SPONSOR);
	}

}
