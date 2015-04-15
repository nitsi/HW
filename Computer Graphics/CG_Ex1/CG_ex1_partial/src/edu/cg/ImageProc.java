/*
 * This class defines some static methods of image processing.
 */

package edu.cg;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class ImageProc {

	public static BufferedImage scaleDown(BufferedImage img, int factor) 
	{
		if (factor <= 0)
			throw new IllegalArgumentException();
		int newHeight = img.getHeight() / factor;
		int newWidth = img.getWidth() / factor;
		BufferedImage out = new BufferedImage(newWidth, newHeight, img.getType());
		for (int x = 0; x < newWidth; x++)
			for (int y = 0; y < newHeight; y++)
				out.setRGB(x, y, img.getRGB(x * factor, y * factor));
		return out;
	}

	public static BufferedImage grayScale(BufferedImage img) 
	{
		BufferedImage out = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
		for (int x = 0; x < img.getWidth(); x++) 
		{
			for (int y = 0; y < img.getHeight(); y++) 
			{
				int rgb = img.getRGB(x, y);
				int r = (rgb >> 16) & 0xFF;
				int g = (rgb >> 8) & 0xFF;
				int b = (rgb & 0xFF);

				int grayLevel = (r + g + b) / 3;
				int gray = (grayLevel << 16) + (grayLevel << 8) + grayLevel;
				out.setRGB(x, y, gray);
			}
		}
		return out;
	}

	public static BufferedImage horizontalDerivative(BufferedImage img) 
	{
		BufferedImage out = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
		BufferedImage grayed = grayScale(img);
		for (int x = 0; x < img.getWidth() - 1; x++) 
		{
			for (int y = 0; y < img.getHeight(); y++) 
			{
				if (x == 0 || y == 0 || x == img.getWidth() - 1 || y == img.getHeight() - 1) 
				{
					out.setRGB(x, y, 0);
				} 
				else 
				{
					int xDeriv = ((grayed.getRGB(x + 1, y) & 0xFF) - (grayed.getRGB(x, y) & 0xFF) + 0xFF) >> 1;
					out.setRGB(x, y, new Color(xDeriv, xDeriv, xDeriv).getRGB());
				}
			}
		}
		return out;
	}

	public static BufferedImage verticalDerivative(BufferedImage img) 
	{
		BufferedImage out = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
		BufferedImage grayed = grayScale(img);
		for (int x = 0; x < img.getWidth() - 1; x++) 
		{
			for (int y = 0; y < img.getHeight(); y++) 
			{
				if (x == 0 || y == 0 || x == img.getWidth() - 1 || y == img.getHeight() - 1) 
				{
					out.setRGB(x, y, 0);
				} 
				else 
				{
					int yDeriv = ((grayed.getRGB(x, y) & 0xFF) - (grayed.getRGB(x, y - 1) & 0xFF) + 0xFF) >> 1;
					out.setRGB(x, y, new Color(yDeriv, yDeriv, yDeriv).getRGB());
				}

			}
		}

		return out;
	}

	public static BufferedImage gradientMagnitude(BufferedImage img) 
	{
		BufferedImage out = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
		BufferedImage xDeriv = horizontalDerivative(img);
		BufferedImage yDeriv = verticalDerivative(img);
		for (int x = 0; x < img.getWidth() - 1; x++) 
		{
			for (int y = 0; y < img.getHeight(); y++) 
			{
				if (x == 0 || y == 0 || x == img.getWidth() - 1 || y == img.getHeight() - 1) {
					out.setRGB(x, y, 0x80);
				} else {
					// x Deriv calc below this
					int xDerivVal = xDeriv.getRGB(x, y) & 0xFF;
					// y Deriv calc below this
					int yDerivVal = yDeriv.getRGB(x, y) & 0xFF;

					int gradMagnitude = (int) Math.sqrt(yDerivVal * yDerivVal - xDerivVal * xDerivVal);
					out.setRGB(x, y, new Color(gradMagnitude, gradMagnitude, gradMagnitude).getRGB());
				}
			}
		}

		return out;
	}
	
	public static BufferedImage retargetHorizontal(BufferedImage img, int width) 
	{
		return new Retargeter(img, Math.abs(img.getWidth() - width), false)
				.retarget(width);
	}

	// Runs the seam carving algorithm to resize an image vertically (change height)
	public static BufferedImage retargetVertical(BufferedImage img, int height) 
	{
		return new Retargeter(img, Math.abs(img.getHeight() - height), true)
				.retarget(height);
	}
	
	// Runs the horizontal seam carving algorithm to present the seams for removal/duplication
	public static BufferedImage showSeamsHorizontal(BufferedImage img, int width) 
	{
		return new Retargeter(img, Math.abs(img.getWidth() - width), false)
				.showSeams(width);
	}

	// Runs the vertical seam carving algorithm to present the seams for removal/duplication
	public static BufferedImage showSeamsVertical(BufferedImage img, int height) 
	{
		return new Retargeter(img, Math.abs(img.getHeight() - height), true)
				.showSeams(height);
	}
}
