/*
 * This class defines some static methods of image processing.
 */

package edu.cg;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class ImageProc {

	// Creates a reduced size image from the original image by taking every
	// <factor>-th pixel in x and y direction
	public static BufferedImage scaleDown(BufferedImage img, int factor) {
		if (factor <= 0)
			throw new IllegalArgumentException();
		int newHeight = img.getHeight() / factor;
		int newWidth = img.getWidth() / factor;
		BufferedImage out = new BufferedImage(newWidth, newHeight,
				img.getType());
		for (int x = 0; x < newWidth; x++)
			for (int y = 0; y < newHeight; y++)
				out.setRGB(x, y, img.getRGB(x * factor, y * factor));
		return out;
	}

	// BONUS - Antialiasing – add a smoothing operation before scaling down
	public static BufferedImage scaleDownBonus(BufferedImage img, int factor) {
		// Copy the image
		BufferedImage newImg = new BufferedImage(img.getWidth(),
				img.getHeight(), BufferedImage.TYPE_INT_RGB);

		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				newImg.setRGB(x, y, img.getRGB(x, y));
			}
		}
		// contain the sum of the current
		int sumOfBoxRed = 0, sumOfBoxGreen = 0, sumOfBoxBlue = 0;
		Color pixelColor;

		// The smoothing operation - this process sums the current box and
		// calculates the new pixel's value
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {

				// Check if we are at corners
				if ((x == 0 && y == 0)
						|| (x == 0 && y == img.getWidth() - 1)
						|| (x == img.getWidth() - 1 && y == 0)
						|| (x == img.getWidth() - 1 && y == img.getHeight() - 1)) {
					continue;
				}

				// Case top border
				if (y == 0) {
					for (int xBox = x - 1; xBox <= x + 1; xBox++) {
						for (int yBox = y; yBox <= y + 1; yBox++) {
							pixelColor = new Color(img.getRGB(xBox, yBox));
							sumOfBoxRed += pixelColor.getRed();
							sumOfBoxGreen += pixelColor.getGreen();
							sumOfBoxBlue += pixelColor.getBlue();
						}
					}
				} else if (x == 0) {
					// Case left border
					for (int xBox = x; xBox <= x + 1; xBox++) {
						for (int yBox = y - 1; yBox < y + 1; yBox++) {
							pixelColor = new Color(img.getRGB(xBox, yBox));
							sumOfBoxRed += pixelColor.getRed();
							sumOfBoxGreen += pixelColor.getGreen();
							sumOfBoxBlue += pixelColor.getBlue();
						}
					}
				} else if (y == img.getHeight() - 1) {
					// Case lower border
					for (int xBox = x - 1; xBox <= x + 1; xBox++) {
						for (int yBox = y - 1; yBox <= y; yBox++) {
							pixelColor = new Color(img.getRGB(xBox, yBox));
							sumOfBoxRed += pixelColor.getRed();
							sumOfBoxGreen += pixelColor.getGreen();
							sumOfBoxBlue += pixelColor.getBlue();
						}
					}
				} else if (x == img.getWidth() - 1) {
					// Case right border
					for (int xBox = x - 1; xBox <= x; xBox++) {
						for (int yBox = y - 1; yBox <= y + 1; yBox++) {
							pixelColor = new Color(img.getRGB(xBox, yBox));
							sumOfBoxRed += pixelColor.getRed();
							sumOfBoxGreen += pixelColor.getGreen();
							sumOfBoxBlue += pixelColor.getBlue();
						}
					}
				} else {
					// else, Calculate the sum of the current box
					for (int xBox = x - 1; xBox <= x + 1; xBox++) {
						for (int yBox = y - 1; yBox <= y + 1; yBox++) {
							pixelColor = new Color(img.getRGB(xBox, yBox));
							sumOfBoxRed += pixelColor.getRed();
							sumOfBoxGreen += pixelColor.getGreen();
							sumOfBoxBlue += pixelColor.getBlue();
						}
					}
				}

				sumOfBoxRed /= 9;
				sumOfBoxGreen /= 9;
				sumOfBoxBlue /= 9;

				newImg.setRGB(x, y, new Color(sumOfBoxRed, sumOfBoxGreen,
						sumOfBoxBlue).getRGB());
				sumOfBoxRed = 0;
				sumOfBoxBlue = 0;
				sumOfBoxGreen = 0;
			}
		}

		// Create the scaling down new image
		int newBoxHeight = img.getHeight() / factor;
		int newBoxWidth = img.getWidth() / factor;
		BufferedImage out = new BufferedImage(newBoxWidth, newBoxHeight,
				img.getType());
		for (int x = 0; x < newBoxWidth; x++) {
			for (int y = 0; y < newBoxHeight; y++) {
				out.setRGB(x, y, newImg.getRGB(x * factor, y * factor));
			}
		}

		return out;
	}

	// Converts an image to gray scale
	public static BufferedImage grayScale(BufferedImage img) {

		// contain the red, green and blue colors
		int grayScaleColor;

		// Contain pixel color
		Color px;

		// set the new image
		BufferedImage newImg = new BufferedImage(img.getWidth(),
				img.getHeight(), BufferedImage.TYPE_INT_RGB);

		// Move through image pixels and change them to gray scale
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {

				// get the current pixel color
				px = new Color(img.getRGB(x, y));

				// calculate the gray scale color
				grayScaleColor = (int) ((0.2989 * px.getRed())
						+ (0.5870 * px.getGreen()) + (0.1140 * px.getBlue()));

				// change the current pixel to gray scale
				newImg.setRGB(x, y, new Color(grayScaleColor, grayScaleColor,
						grayScaleColor).getRGB());
			}
		}
		return newImg;
	}

	// Calculates the magnitude of gradient at each pixel
	public static BufferedImage gradientMagnitude(BufferedImage img) {

		// Contain the difference between dx and dy, and the grey of this pixel
		int dx, dy, grayScaleColor, gradient;

		// Contain the color of the current pixel
		Color pixelColor;

		// Contain the grey color of each pixel
		int[][] grayOfPixel = new int[img.getWidth()][img.getHeight()];

		// The output image
		BufferedImage newImg = new BufferedImage(img.getWidth(),
				img.getHeight(), BufferedImage.TYPE_INT_RGB);

		// calculate each pixel grayness
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {

				// get the current pixel color
				pixelColor = new Color(img.getRGB(x, y));

				// calculate the gray scale color
				grayScaleColor = (int) ((0.2989 * pixelColor.getRed())
						+ (0.5870 * pixelColor.getGreen()) + (0.1140 * pixelColor
						.getBlue()));

				// save the gray of this pixel
				grayOfPixel[x][y] = grayScaleColor;
			}
		}

		// Move through the pixels and compute gradient
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {

				// Check borders
				if (x == 0 || y == 0 || x == img.getWidth() - 1
						|| y == img.getHeight() - 1) {
					gradient = 127;
				} else {

					// Start to calculate gradient
					dx = grayOfPixel[x][y] - grayOfPixel[x + 1][y];
					dy = grayOfPixel[x][y] - grayOfPixel[x][y + 1];
					dx = (int) Math.pow(dx, 2);
					dy = (int) Math.pow(dy, 2);
					gradient = (int) Math.sqrt(dx + dy);

					// Case we got a result out of the limit 255
					if (gradient > 255) {
						gradient = 255;
					}
				}

				// set the gradient
				newImg.setRGB(x, y,
						new Color(gradient, gradient, gradient).getRGB());
			}
		}
		return newImg;
	}

	// Runs the seam carving algorithm to resize an image horizontally (change
	// width)
	public static BufferedImage retargetHorizontal(BufferedImage img, int width) {
		// Contain the number of seams
		// int numberOfSeams = img.getWidth() - width;

		// Contain the table gradient
		// int[][] tableGradient = calculateGradient(img);

		return new Retargeter(img, Math.abs(img.getWidth() - width), false)
				.retarget(width);
	}

	// Runs the seam carving algorithm to resize an image vertically (change
	// height)
	public static BufferedImage retargetVertical(BufferedImage img, int height) {
		return new Retargeter(img, Math.abs(img.getHeight() - height), true)
				.retarget(height);
	}

	// Runs the horizontal seam carving algorithm to present the seams for
	// removal/duplication
	public static BufferedImage showSeamsHorizontal(BufferedImage img, int width) {
		return new Retargeter(img, Math.abs(img.getWidth() - width), false)
				.showSeams(width);
	}

	// Runs the vertical seam carving algorithm to present the seams for
	// removal/duplication
	public static BufferedImage showSeamsVertical(BufferedImage img, int height) {
		return new Retargeter(img, Math.abs(img.getHeight() - height), true)
				.showSeams(height);
	}

}
