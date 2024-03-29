package cs4551.homework3.models.image.rgb;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;

/*******************************************************
 CS4551 Multimedia Software Systems
 @ Author: Elaine Kang

 This image class is for a 24bit RGB image only.
 *******************************************************/
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.imageio.stream.FileImageInputStream;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

// A wrapper class of BufferedImage
// Provide a couple of utility functions such as reading from and writing to PPM file

public abstract class Image
{
  protected BufferedImage img;
  protected String fileName;			// Input file name
  protected int pixelDepth=3;			// pixel depth in byte

  public Image(int w, int h)
  // create an empty image with w(idth) and h(eight)
  {
	fileName = "";
	img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
//	System.out.println("Created an empty image with size " + w + "x" + h);
  }

  public Image(String fn) throws IOException
  // Create an image and read the data from the file
  {
	  fileName = fn;
	  readPPM(fileName);
	  System.out.println("Created an image from " + fileName+ " with size "+getW()+"x"+getH());
  }

  public int getW()
  {
	return img.getWidth();
  }

  public int getH()
  {
	return img.getHeight();
  }

  public int getSize()
  // return the image size in byte
  {
	return getW()*getH()*pixelDepth;
  }

  public void setPixel(int x, int y, byte[] rgb)
  // set byte rgb values at (x,y)
  {
	int pix = 0xff000000 | ((rgb[0] & 0xff) << 16) | ((rgb[1] & 0xff) << 8) | (rgb[2] & 0xff);
	img.setRGB(x,y,pix);
  }

  public void setPixel(int x, int y, int[] irgb)
  // set int rgb values at (x,y)
  {
	byte[] rgb = new byte[3];

	for(int i=0;i<3;i++)
	  rgb[i] = (byte) irgb[i];

	setPixel(x,y,rgb);
  }

  public void getPixel(int x, int y, byte[] rgb)
  // retreive rgb values at (x,y) and store in the byte array
  {
  	int pix = img.getRGB(x,y);

  	rgb[2] = (byte) pix;
  	rgb[1] = (byte)(pix>>8);
  	rgb[0] = (byte)(pix>>16);
  }


  public void getPixel(int x, int y, int[] rgb)
  // retreive rgb values at (x,y) and store in the int array
  {
	int pix = img.getRGB(x,y);

	byte b = (byte) pix;
	byte g = (byte)(pix>>8);
	byte r = (byte)(pix>>16);

    // converts singed byte value (~128-127) to unsigned byte value (0~255)
	rgb[0]= (int) (0xFF & r);
	rgb[1]= (int) (0xFF & g);
	rgb[2]= (int) (0xFF & b);
  }

  public void displayPixelValue(int x, int y)
  // Display rgb pixel in unsigned byte value (0~255)
  {
	int pix = img.getRGB(x,y);

	byte b = (byte) pix;
	byte g = (byte)(pix>>8);
	byte r = (byte)(pix>>16);

    System.out.println("RGB Pixel value at ("+x+","+y+"):"+(0xFF & r)+","+(0xFF & g)+","+(0xFF & b));
   }

  public void readPPM(String fileName) throws IOException
  // read a data from a PPM file
  {
	File fIn = null;
	FileImageInputStream fis = null;

	fIn = new File(fileName);
	fis = new FileImageInputStream(fIn);

	System.out.println("Reading "+fileName+"...");

	// read Identifier
	if(!fis.readLine().equals("P6"))
	{
		fis.close();
		throw new IOException("This is NOT P6 PPM. Wrong Format.");
	}

	// read Comment line
	String commentString = fis.readLine();

	// read width & height
	String[] WidthHeight = fis.readLine().split(" ");
	int width = Integer.parseInt(WidthHeight[0]);
	int height = Integer.parseInt(WidthHeight[1]);

	// read maximum value
	int maxVal = Integer.parseInt(fis.readLine());

	if(maxVal != 255)
	{
		fis.close();
		throw new IOException("Max val is not 255");
	}

	// read binary data byte by byte and save it into BufferedImage object
	int x,y;
	byte[] rgb = new byte[3];
	img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

	for(y=0;y<getH();y++)
	{
  		for(x=0;x<getW();x++)
		{
			rgb[0] = fis.readByte();
			rgb[1] = fis.readByte();
			rgb[2] = fis.readByte();
			setPixel(x, y, rgb);
		}
	}

   	fis.close();

	System.out.println("Read "+fileName+" Successfully.");

  }

  public void write2PPM(String fileName)
  // wrrite the image data in img to a PPM file
  {
	FileOutputStream fos = null;
	PrintWriter dos = null;

	try{
		fos = new FileOutputStream(fileName);
		dos = new PrintWriter(fos);

		System.out.println("Writing the Image buffer into "+fileName+"...");

		// write header
		dos.print("P6"+"\n");
		dos.print("#CS451"+"\n");
		dos.print(getW() + " "+ getH() +"\n");
		dos.print(255+"\n");
		dos.flush();

		// write data
		int x, y;
		byte[] rgb = new byte[3];
		for(y=0;y<getH();y++)
		{
			for(x=0;x<getW();x++)
			{
				getPixel(x, y, rgb);
				fos.write(rgb[0]);
				fos.write(rgb[1]);
				fos.write(rgb[2]);

			}
			fos.flush();
		}
		dos.close();
		fos.close();

		System.out.println("Wrote into "+fileName+" Successfully.");

	} // try
	catch(Exception e)
	{
		System.err.println(e.getMessage());
	}
  }
  
  public void display() {
	  display(null);
  }

  public void display(String title)
  // display the image on the screen
  {
     // Use a label to display the image
      //String title = "Image Name - " + fileName;
      if (title == null) {
    	  title = fileName;
      }
      JFrame frame = new JFrame(title);
      JLabel label = new JLabel(new ImageIcon(img));
      frame.add(label, BorderLayout.CENTER);
      frame.pack();
      frame.setVisible(true);
      frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  }

} // Image class