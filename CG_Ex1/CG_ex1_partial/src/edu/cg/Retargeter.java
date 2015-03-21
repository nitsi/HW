package edu.cg;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class Retargeter {

	/* Contains the given image */
	private BufferedImage givenImage;

	/* Contains the amount of seams to preprocess */
	private int amountOfSeams;

	/* Contains a flag whether the resizing in is vertical or horizontal */
	private boolean isVertical;

	/* Contains the gray gradient values of each pixel */
	private int[][] grayOfPixelValues = null;

	/* Contains the Height and Width of the given image */
	private int imageWidth, imageHeight;

	/* Contains the cost table of this image */
	int[][] costImageTable;

	/* Contains the gradient matrix table */
	int[][] gradientOfPixelValue;

	/* Contains a copy of the matrix table */
	int[][] imageCopyTable;

	/* Contains the k-ordered seams */
	int[][] seamsOrderTable;

	/* Contains the coordinates of the original image */
	int[][] xOriginal;

	// Does all necessary preprocessing, including the calculation of the seam
	// order matrix.
	// k is the amount of seams to preprocess
	// isVertical tells whether the resizing is vertical or horizontal
	public Retargeter(BufferedImage img, int k, boolean isVertical) {
		givenImage = img;
		amountOfSeams = k;
		this.isVertical = isVertical;

		// Check if resizing is vertical or horizontal and define height and
		// width
		if (isVertical) {
			imageHeight = img.getWidth();
			imageWidth = img.getHeight();
		} else {
			imageHeight = img.getHeight();
			imageWidth = img.getWidth();
		}
		grayOfPixelValues = new int[imageWidth][imageHeight];
		gradientOfPixelValue = new int[imageWidth][imageHeight];

		// Contain the current pixel color
		Color pixelColor;
		int grayScaleColor;

		// Start to calculate the gray scale of the image
		// calculate each pixel grayness
		for (int y = 0; y < imageHeight; y++) {
			for (int x = 0; x < imageWidth; x++) {

				// get the current pixel color
				if (isVertical) {
					pixelColor = new Color(img.getRGB(y, x));
				} else {
					pixelColor = new Color(img.getRGB(x, y));
				}

				// calculate the gray scale color
				grayScaleColor = (int) ((0.2989 * pixelColor.getRed()) + 
										(0.5870 * pixelColor.getGreen()) + 
										(0.1140 * pixelColor.getBlue()));

				// save the gray of this pixel
				grayOfPixelValues[x][y] = grayScaleColor;
			}
		}
		calculateSeamsOrderMatrix(k);
	}

	// Does the actual resizing of the image
	public BufferedImage retarget(int newSize) {
		
		// Return original image if size was not changed
		if (amountOfSeams == 0) {
			return givenImage;
		}
		
		// Save the amount of seems that need to resize
		int numOfSeams = Math.abs(imageWidth - newSize);
		BufferedImage newImg;

		// Check if the resize is vertical or horizontal
		if (isVertical) {
			
			int xPosition;
			newImg = new BufferedImage(imageHeight, newSize, BufferedImage.TYPE_INT_RGB);

			// Case in which image is reduced
			if (newSize < imageWidth) {
				for (int y = 0; y < imageHeight; y++) {
					xPosition = 0;
					for (int x = 0; x < imageWidth; x++) {
						// Check if we got over 'k' needed seams
						if (seamsOrderTable[x][y] > numOfSeams) {
							newImg.setRGB(y, xPosition, givenImage.getRGB(y, x));
							xPosition++;
						}
					}
				}
				
			// Case in which image is enlarged
			} else {
				for (int y = 0; y < imageHeight; y++) {
					xPosition = 0;
					for (int x = 0; x < imageWidth; x++) {
						newImg.setRGB(y, xPosition, givenImage.getRGB(y, x));
						xPosition++;
						if (seamsOrderTable[x][y] <= numOfSeams) {
							newImg.setRGB(y, xPosition, givenImage.getRGB(y, x));
							xPosition++;
						}	
					}
				}
			}
		} else {
			int xPosition;
			newImg = new BufferedImage(newSize, imageHeight,
					BufferedImage.TYPE_INT_RGB);
			
			// Case in which image is reduced
			if (newSize < imageWidth) {
				for (int y = 0; y < imageHeight; y++) {
					xPosition = 0;
					for (int x = 0; x < imageWidth; x++) {
						
						// Ignore pixel if it is part of a seam
						if (seamsOrderTable[x][y] > numOfSeams) {
							newImg.setRGB(xPosition, y, givenImage.getRGB(x, y));
							xPosition++;
						}
					}
				}
			
			// Case in which image is enlarged
			} else {
				for (int y = 0; y < imageHeight; y++) {
					xPosition = 0;
					for (int x = 0; x < imageWidth; x++) {
						newImg.setRGB(xPosition, y, givenImage.getRGB(x, y));
						xPosition++;
						
						// Duplicate pixel if it is part of a seam
						if (seamsOrderTable[x][y] < numOfSeams) {
							newImg.setRGB(xPosition, y, givenImage.getRGB(x, y));
							xPosition++;
						}	
					}
				}
			}
		}
		return newImg;
	}

	// Colors the seams pending for removal/duplication
	public BufferedImage showSeams(int newSize) {
		
		// Return original image if size was not changed
		if (amountOfSeams == 0) {
			return givenImage;
		}
		
		// Make a copy of the original image
		BufferedImage seamsToPrint = new BufferedImage(givenImage.getWidth(),
				givenImage.getHeight(), BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < givenImage.getWidth(); x++) {
			for (int y = 0; y < givenImage.getHeight(); y++) {
				seamsToPrint.setRGB(x, y, givenImage.getRGB(x, y));
			}
		}

		// Calculate the number of  seams needed and color them
		int numOfSeams = Math.abs(imageWidth - amountOfSeams);
		for (int y = 0; y < imageHeight; y++) {
			for (int x = 0; x < imageWidth; x++) {
				
				// Color red only the pixels which are part of a seam
				if (seamsOrderTable[x][y] < numOfSeams) {
					if (isVertical) {
						seamsToPrint.setRGB(y, x, new Color(0, 255, 0).getRGB());
					} else {
						seamsToPrint.setRGB(x, y, new Color(255, 0, 0).getRGB());
					}
				}
			}
		}
		return seamsToPrint;
	}

	/*
	 * This helper function takes the minimum cost coordinate and finds the
	 * whole seam by starting from the given pixel
	 */
	private void findSeam(int xCoordinate, int imageWidthMinusSeams,
			int numOfSeams) {
		
		// First coordinate is always at the bottom of the table
		int minCost, originalXCoordinate;
		for (int y = imageHeight - 1; y > 0; y--) {
			originalXCoordinate = xOriginal[xCoordinate][y];
			seamsOrderTable[originalXCoordinate][y] = numOfSeams;

			// Shift tables left
			shiftPixelsLeft(xOriginal, xCoordinate, y);
			shiftPixelsLeft(costImageTable, xCoordinate, y);
			shiftPixelsLeft(grayOfPixelValues, xCoordinate, y);

			// Case in which we're at the left border
			if (xCoordinate == 0) {
				if (costImageTable[xCoordinate][y - 1] > costImageTable[xCoordinate + 1][y - 1]) {
					xCoordinate += 1;
				}

				// Case in which we're at the right border
			} else if (xCoordinate == imageWidthMinusSeams - 1) {
				if (costImageTable[xCoordinate][y - 1] > costImageTable[xCoordinate - 1][y - 1]) {
					xCoordinate -= 1;
				}

			// Case in which we're somewhere in the middle
			} else {
				minCost = Math.min(costImageTable[xCoordinate - 1][y - 1], Math
						.min(costImageTable[xCoordinate][y - 1],
								costImageTable[xCoordinate + 1][y - 1]));

				if (minCost == costImageTable[xCoordinate - 1][y - 1]) {
					xCoordinate -= 1;
				} else if (minCost == costImageTable[xCoordinate + 1][y - 1]) {
					xCoordinate += 1;
				}
			}
		}
		
		// Shift left one last time
		originalXCoordinate = xOriginal[xCoordinate][0];
		seamsOrderTable[originalXCoordinate][0] = numOfSeams;
		shiftPixelsLeft(xOriginal, xCoordinate, 0);
		shiftPixelsLeft(costImageTable, xCoordinate, 0);
		shiftPixelsLeft(grayOfPixelValues, xCoordinate, 0);
	}

	// Calculates the order in which seams are extracted
	private void calculateSeamsOrderMatrix(int k) 
	{
		// Return original image if size was not changed
		if (k == 0) 
		{
			return;
		}

		// Save the original x coordinates of the image
		xOriginal = new int[imageWidth][imageHeight];
		for (int y = 0; y < imageHeight; y++) 
		{
			for (int x = 0; x < imageWidth; x++) 
			{
				xOriginal[x][y] = x;
			}
		}

		// Define the seams order table to be size width * height
		seamsOrderTable = new int[imageWidth][imageHeight];

		// Initialize the "seamsOrderTable" to be the highest value
		for (int y = 0; y < imageHeight; y++) 
		{
			for (int x = 0; x < imageWidth; x++) 
			{
				seamsOrderTable[x][y] = Integer.MAX_VALUE;
			}
		}

		int minCost, imageWidthMinusSeams, xMinimumCoordinate;

		// Calculate every seam, and shift all pixels to the left of the seam
		for (int numberOfSeams = 0; numberOfSeams < k; numberOfSeams++) 
		{
			imageWidthMinusSeams = imageWidth - numberOfSeams;
			calculateCostsMatrix(imageWidthMinusSeams);
			xMinimumCoordinate = 0;

			// Find the minimum pixel at the bottom of the image
			minCost = costImageTable[0][imageHeight - 1];
			xMinimumCoordinate = 0;
			for (int minX = 0; minX < imageWidthMinusSeams; minX++) 
			{
				if (costImageTable[minX][imageHeight - 1] < minCost) 
				{
					minCost = costImageTable[minX][imageHeight - 1];
					xMinimumCoordinate = minX;
				}
			}

			// Calculate the rest of the seam in a helper array
			findSeam(xMinimumCoordinate, imageWidthMinusSeams, numberOfSeams);
		}
	}

	/*
	 * This helper function shifts all pixels one pixel left starting from index i
	 */
	private void shiftPixelsLeft(int[][] tableToShift, int i, int y) 
	{
		for (int x = i; x < tableToShift.length - 1; x++) 
		{
			tableToShift[x][y] = tableToShift[x + 1][y];
		}
	}

	// Calculates the cost matrix for a given image width (w).
	// To be used inside calculateSeamsOrderMatrix().
	private void calculateCostsMatrix(int w) 
	{
		costImageTable = new int[w][imageHeight];

		// Put 1000 at the two upper corner pixels
		costImageTable[0][0] = 1000;
		costImageTable[w - 1][0] = 1000;

		// Fill the top border
		for (int x = 1; x < w - 1; x++) 
		{
			costImageTable[x][0] = Math.abs(grayOfPixelValues[x + 1][0] - grayOfPixelValues[x - 1][0]);
		}
		int vCost, rCost, lCost;

		// Move through the image and calculate the minimum cost of all pixels
		for (int y = 1; y < imageHeight; y++) 
		{
			for (int x = 1; x <= w; x++) 
			{

				// Check if we are at the left border
				if (x == w) {
					rCost = Math.abs(grayOfPixelValues[1][y] - grayOfPixelValues[0][y - 1]);
					costImageTable[0][y] = costImageTable[1][y]	+ 
										Math.min(costImageTable[0][y - 1],
												 costImageTable[1][y - 1] + rCost);
					
				// Check if we are at the right border
				} 
				else if (x == w - 1) 
				{
					lCost = Math.abs(grayOfPixelValues[w - 2][y] - grayOfPixelValues[w - 1][y - 1]);
					costImageTable[x][y] = costImageTable[w - 2][y]	+ 
										   Math.min(costImageTable[w - 1][y - 1], 
												    costImageTable[w - 2][y - 1] + lCost);
				} 
				else 
				{
					vCost = Math.abs(grayOfPixelValues[x + 1][y] - grayOfPixelValues[x - 1][y]);

					rCost = Math.abs(grayOfPixelValues[x + 1][y] - grayOfPixelValues[x - 1][y])	+ 
							Math.abs(grayOfPixelValues[x + 1][y] - grayOfPixelValues[x][y - 1]);

					lCost = Math.abs(grayOfPixelValues[x + 1][y] - grayOfPixelValues[x - 1][y])	+ 
							Math.abs(grayOfPixelValues[x - 1][y] - grayOfPixelValues[x][y - 1]);

					costImageTable[x][y] = Math.min(costImageTable[x - 1][y - 1] + lCost, 
										   Math.min(costImageTable[x][y - 1] + vCost,
												   	costImageTable[x + 1][y - 1] + rCost));
				}
			}
		}
	}
}