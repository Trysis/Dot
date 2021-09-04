import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Main {

	public static BufferedImage generate(int width,int height) {//Fond Gradient Noir -> Blanc
		BufferedImage bufferedImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
		Graphics2D g = bufferedImage.createGraphics();
		for(int i=0;i<width;i++) {
			for(int j=0;j<height;j++) {
				int gradient = (int) (Math.random()*255);
				bufferedImage.setRGB(i, j, new Color(gradient,gradient,gradient).getRGB());
			}
		}
		g.dispose();
		return bufferedImage;
	}
	public static BufferedImage generateDot(Color color, int number, int longueur,int hauteur) {
		BufferedImage bufferedImage = new BufferedImage(2000,2000,BufferedImage.TYPE_INT_RGB);
		Graphics2D g = bufferedImage.createGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
		
		ArrayList<Ellipse> array_list=new ArrayList<>();
		int tentatives_max = 10;
		int t=0;
		for(int i=0;i<number;i++) {
			Ellipse e = new Ellipse(color,Math.random()*bufferedImage.getWidth()-1,Math.random()*bufferedImage.getHeight()-1,longueur,hauteur);
			array_list.add(e);
			for(int j=0;j<array_list.size();j++) {
				if(array_list.get(j).distanceToCenterOf(e)<=longueur+hauteur) 
					while(array_list.get(j).distanceToCenterOf(e)<=longueur+hauteur && t<tentatives_max){//A résoudre à l'aide d'Arbre ? pour plus d'efficacité et une meilleur sélection de l'emplacement ?
						e
						.setX(Math.random()*bufferedImage.getWidth())
						.setY(Math.random()*bufferedImage.getHeight());
						t++;
					}
				t=0;
			}
			g.setColor(e.getCouleur());
			g.fill(e);
		}
		
		g.dispose();
		return bufferedImage;
	}
	public static LinkedList<Ellipse> generateDotOnImage(Color color, int number, int longueur,int hauteur) {//Genere une image et renvoi la liste des Ellipses crées
		BufferedImage bufferedImage = generate(2048,2048);
		Graphics2D g = bufferedImage.createGraphics();
		
		LinkedList<Ellipse> array_list=new LinkedList<>();
		int tentatives_max = 10;
		int t=0;
		for(int i=0;i<number;i++) {
			Ellipse e = new Ellipse(color,Math.random()*bufferedImage.getWidth()-1,Math.random()*bufferedImage.getHeight()-1,Math.random()*longueur+longueur,Math.random()*hauteur+hauteur);
			array_list.add(e);
			for(int j=0;j<array_list.size();j++) {
				if(array_list.get(j).distanceToCenterOf(e)<=longueur+hauteur) 
					while(array_list.get(j).distanceToCenterOf(e)<=longueur+hauteur && t<tentatives_max){//A résoudre à l'aide d'Arbre ? pour plus d'efficacité et une meilleur sélection de l'emplacement ?
						e
						.setX(Math.random()*bufferedImage.getWidth())
						.setY(Math.random()*bufferedImage.getHeight());
						t++;
					}
				t=0;
			}
			g.setColor(e.getCouleur());
			g.fill(e);
		}
		
		g.dispose();

		NewImageFile(bufferedImage);//Crée une nouvelle image à partir d'un BufferedImage
		return array_list;
	}
	public static BufferedImage generateDotOnImage(BufferedImage bufferedImage,Color color, int number, int longueur,int hauteur) {
		Graphics2D g = bufferedImage.createGraphics();
		
		ArrayList<Ellipse> array_list=new ArrayList<>();
		int tentatives_max = 10;
		int t=0;
		for(int i=0;i<number;i++) {
			Ellipse e = new Ellipse(color,Math.random()*bufferedImage.getWidth()-1,Math.random()*bufferedImage.getHeight()-1,longueur,hauteur);
			array_list.add(e);
			for(int j=0;j<array_list.size();j++) {
				if(array_list.get(j).distanceToCenterOf(e)<=longueur+hauteur) 
					while(array_list.get(j).distanceToCenterOf(e)<=longueur+hauteur && t<tentatives_max){//A résoudre à l'aide d'Arbre ? pour plus d'efficacité et une meilleur sélection de l'emplacement ?
						e
						.setX(Math.random()*bufferedImage.getWidth())
						.setY(Math.random()*bufferedImage.getHeight());
						t++;
					}
				t=0;
			}
			g.setColor(e.getCouleur());
			g.fill(e);
		}
		g.dispose();
		return bufferedImage;
	}
	public static void NewImageFile(BufferedImage bufferedImage) {//Permet de créer un nouveau fichier à partir d'un BufferedImage
		String PATH ="/D:/RandomImageFileDot/";
		String NAME = "test";
		String FORMAT = ".png";
		
		int id=0;
		if(!new File(PATH).mkdir())while(new File(PATH+NAME+id+FORMAT).exists())id++;

		File file = new File(PATH+NAME+id+FORMAT);
		try {
			ImageIO.write(bufferedImage, "png", file);
			System.out.println("Image successfully created");
			System.out.println("Created at "+file.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void convert(int index, LinkedList<Ellipse> dots){
        String xmlFilePath = "\\D:\\RandomImageFileDot\\test"+index+".xml";
        try {

            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();

            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

            Document document = documentBuilder.newDocument();

                // root element
            Element root = document.createElement("annotation");
            document.appendChild(root);

                // Truc avant
            Element folder = document.createElement("folder");
            folder.appendChild(document.createTextNode("RandomImageFileDot"));
            root.appendChild(folder);

            Element filename = document.createElement("filename");
            filename.appendChild(document.createTextNode("test"+index+".png"));
            root.appendChild(filename);

            Element path = document.createElement("path");
            path.appendChild(document.createTextNode("D:\\RandomImageFileDot\\test"+index+".png"));
            root.appendChild(path);

            Element source = document.createElement("sources");
            Element database = document.createElement("database");
            database.appendChild(document.createTextNode("Unknown"));
            source.appendChild(database);
            root.appendChild(source);

            Element size = document.createElement("size");
            Element width = document.createElement("width");
            width.appendChild(document.createTextNode("2048"));
            size.appendChild(width);
            Element height = document.createElement("height");
            height.appendChild(document.createTextNode("2048"));
            size.appendChild(height);
            Element depth = document.createElement("depth");
            depth.appendChild(document.createTextNode("3"));
            size.appendChild(depth);
            root.appendChild(source);

            Element segmented = document.createElement("segmented");
            segmented.appendChild(document.createTextNode("0"));
            root.appendChild(segmented);

            for(int i=0;i<dots.size();i++) {
                Ellipse d=dots.get(i);
                Element object = document.createElement("object");
                Element name = document.createElement("name");
                name.appendChild(document.createTextNode("Dot"));
                object.appendChild(name);
                Element pose = document.createElement("pose");
                pose.appendChild(document.createTextNode("Unspecified"));
                object.appendChild(pose);
                Element truncated = document.createElement("truncated");
                truncated.appendChild(document.createTextNode("0"));
                object.appendChild(truncated);
                Element difficult = document.createElement("difficult");
                difficult.appendChild(document.createTextNode("0"));
                object.appendChild(difficult);
                Element bndbox = document.createElement("bndbox");
                object.appendChild(bndbox);
                Element xmin = document.createElement("xmin");
                xmin.appendChild(document.createTextNode(String.valueOf((int)d.getMinX())));
                bndbox.appendChild(xmin);
                Element ymin = document.createElement("ymin");
                ymin.appendChild(document.createTextNode(String.valueOf((int)d.getMinY())));
                bndbox.appendChild(ymin);
                Element xmax = document.createElement("xmax");
                xmax.appendChild(document.createTextNode(String.valueOf((int)d.getMaxX())));
                bndbox.appendChild(xmax);
                Element ymax = document.createElement("ymax");
                ymax.appendChild(document.createTextNode(String.valueOf((int)d.getMaxY())));
                bndbox.appendChild(ymax);

                root.appendChild(object);
                System.out.println(d);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(xmlFilePath));

                transformer.transform(domSource, streamResult);
            } catch (ParserConfigurationException pce) {
                pce.printStackTrace();
            } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }
    }

	public static void main(String[] args) {
		//for(int i=0;i<250;i++)convert(i,generateDotOnImage(Color.GREEN,45,10,10));
		//for(int i=0;i<1000;i++)NewImageFile(generateDotOnImage(generate(2000,2000),Color.GREEN,300,(int)Math.random()*15+10,(int)Math.random()*15+10));
		new DataManager(2048,2048).generate().setColor(Color.GREEN).setMinMaxHeight(10, 30).setMinMaxWidth(15, 29).generateDot(50).buildFile();
	}
}
