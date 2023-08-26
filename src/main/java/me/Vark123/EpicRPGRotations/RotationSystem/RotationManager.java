package me.Vark123.EpicRPGRotations.RotationSystem;

import java.util.Collection;
import java.util.HashSet;

import lombok.Getter;

@Getter
public final class RotationManager {

	private static final RotationManager inst = new RotationManager();
	
	private Collection<Rotation> rotations;
	
	private RotationManager() {
		rotations = new HashSet<>();
	}
	
	public static final RotationManager get() {
		return inst;
	}
	
	public void registerRotation(Rotation rotation) {
		rotations.add(rotation);
		rotation.addCalendarEvent();
	}
	
}
