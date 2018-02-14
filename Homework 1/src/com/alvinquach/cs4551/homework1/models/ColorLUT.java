package com.alvinquach.cs4551.homework1.models;

import java.util.ArrayList;
import java.util.List;

import com.alvinquach.cs4551.homework1.utils.MathUtils;

/**
 * @author Alvin Quach
 */
public class ColorLUT {
	
	private List<ColorRGB> colors = new ArrayList<>();
	
	public void add(int red, int green, int blue) {
		red = MathUtils.clamp(red, 0, 255);
		green = MathUtils.clamp(green, 0, 255);
		blue = MathUtils.clamp(blue, 0, 255);
		colors.add(new ColorRGB(red, green, blue));
	}
	
	public ColorRGB get(int index) {
//		MathUtils.clamp(index, 0, colors.size());
		return colors.get(index);
	}

	@Override
	public String toString() {
		String result = "Index\t\tR\tG\tB\n";
		result += "________________________________________\n";
		int i = 0;
		for (ColorRGB color : colors) {
			result += i + "\t\t" + color.getRed() + "\t" + color.getGreen() + "\t" + color.getBlue() + "\n";
			i++;
		}
		return result;
	}
	
}
