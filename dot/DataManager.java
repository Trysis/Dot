import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.RectangularShape;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DataManager {
	private static int index_in_file;//Correspond à l'index disponible pour la création de son fichier 
	private static int index_in_instance;//Renvoi l'index du dernier objet DataManager créé
	private int index = -1;//Correspond à l'index de l'objet
	
	private String PATH ="/D:/RandomImageFileDot/";
	private String NAME = "test";
	private String FORMAT = ".png";
	private String FORMAT_D = ".xml";

	private BufferedImage bufferedImage;
	private Graphics2D g;
	private ArrayList<Data> data_list=new ArrayList<>();
	
	private Color default_color=Color.black;
	
	private int min_width,max_width;
	private int min_height,max_height;
	private int default_numberofdot_to_generate;
	
	private boolean isMinMaxWidthDefined,isMinMaxHeightDefined;
	private boolean isDefaultNumberofDotDefined;

	DataManager(int width,int height){
		bufferedImage=new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
		g = bufferedImage.createGraphics();
	}
	{
		while(new File(PATH+NAME+index_in_file+FORMAT).exists())index_in_file++;
		this.index=index_in_file+index_in_instance++;
	}
	//Getter
	public int getNumberofData() {//Renvoi le nombre de Data possedé
		return data_list.size();
	}
	public Data getDataAt(int index) {//Renvoi la Data à l'indice index
		return data_list.get(index);
	}
	public ArrayList<Data> getData(){//Renvoi une liste de Data
		return data_list;
	}
	//ArrayList
	public void addData(Data e) {//Ajoute une Data à la liste
		data_list.add(e);
	}
	public void addData(ArrayList<Data> data_list) {//Ajoute une ou plusieurs Data à la Liste
		for(Data e: data_list)addData(e);
	}
	public boolean removeData(Data e) {//Supprime une Data
		return data_list.remove(e);
	}
	public boolean removeData(ArrayList<Data> data_list) {//Supprime une ou plusieurs Data
		boolean b=true;
		for(Data e: data_list)if(!removeData(e))b=false;
		return true;
	}
	//Setter
	public DataManager setColor(Color color) {
		this.default_color=color;
		return this;
	}
	public DataManager setBufferedImage(BufferedImage bufferedImage) {//Initialise le BufferedImage
		this.bufferedImage=bufferedImage;
		return this;
	}
	public DataManager setData(ArrayList<Data> data_list) {
		this.data_list=data_list;
		return this;
	}
	public DataManager setMinMaxWidth(int min,int max) {//Taille min et max d'un Dot en width (pour generate())
		this.min_width=min;
		this.max_width=max;
		return ((min>0) && (min<max)) ? MinMaxWDefined():MinMaxWNotDefined();
	}
	public DataManager setMinMaxHeight(int min,int max) {//taille min et max d'un Dot en height (pour generate())
		this.min_height=min;
		this.max_height=max;
		return ((min>0) && (min<max)) ? MinMaxHDefined():MinMaxHNotDefined();
	}
	public DataManager setDefaultNumberOfDot(int number) {
		return (default_numberofdot_to_generate=number)>0 ? NumberOfDotDefined():NumberOfDotNotDefined();
	}
	//Data gesture
	private DataManager MinMaxWDefined() {//Modifie la valeur boolean de isMinMaxWidthDefined informant sur les variables min_width et max_width
		this.isMinMaxWidthDefined=true;
		return this;
	}
	private DataManager MinMaxWNotDefined() {
		isMinMaxWidthDefined=false;
		return null;
	}
	//
	private DataManager MinMaxHDefined() {
		this.isMinMaxHeightDefined=true;
		return this;
	}
	private DataManager MinMaxHNotDefined() {
		isMinMaxHeightDefined=false;
		return null;
	}
	//
	private boolean MinMaxDefined() {//Renvoi true si les valeurs min et max de la width et height sont définis pour les dimensions des Dot
		return isMinMaxWidthDefined && isMinMaxHeightDefined;
	}
	//
	private DataManager NumberOfDotDefined() {
		isDefaultNumberofDotDefined=true;
		return this;
	}
	private DataManager NumberOfDotNotDefined() {
		isDefaultNumberofDotDefined=false;
		return null;
	}
	//BufferedImage
	public DataManager generate() {//Fond Gradient Noir -> Blanc
		for(int i=0;i<bufferedImage.getWidth();i++) {
			for(int j=0;j<bufferedImage.getHeight();j++) {
				int gradient = (int) (Math.random()*255);
				bufferedImage.setRGB(i, j, new Color(gradient,gradient,gradient).getRGB());
			}
		}
		return this;
	}
	public DataManager generateDot() {
		try {
			if(isDefaultNumberofDotDefined)return generateDot(default_numberofdot_to_generate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}
	public DataManager generateDot(int number) {
		if(!MinMaxDefined()) return null;

		ArrayList<Ellipse> array_list=new ArrayList<>();
		int tentatives_max = 10;
		int t=0;
		
		for(int i=0;i<number;i++) {
			Ellipse e = new Ellipse(default_color,Math.random()*bufferedImage.getWidth()-1,Math.random()*bufferedImage.getHeight()-1,min_width+Math.random()*(max_width-min_width),min_height+Math.random()*(max_height-min_height));
			data_list.add(e);//Met l'ellipse dans la liste des Data
			for(int j=0;j<array_list.size();j++) {
				if(array_list.get(j).distanceToCenterOf(e)<=array_list.get(j).getWidth()+array_list.get(j).getHeight()) 
					while(array_list.get(j).distanceToCenterOf(e)<=array_list.get(j).getWidth()+array_list.get(j).getHeight() && t<tentatives_max){//A résoudre à l'aide d'Arbre ? pour plus d'efficacité et une meilleur sélection de l'emplacement ?
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
		return this;
	}
	public DataManager generateDot(Color color, int number, int[] width,int[] height) {		
		ArrayList<Ellipse> array_list=new ArrayList<>();
		int tentatives_max = 10;
		int t=0;
		for(int i=0;i<number;i++) {
			Ellipse e = new Ellipse(color,Math.random()*bufferedImage.getWidth()-1,Math.random()*bufferedImage.getHeight()-1,width[(int)Math.random()*width.length],height[(int)Math.random()*height.length]);
			data_list.add(e);
			for(int j=0;j<array_list.size();j++) {
				if(array_list.get(j).distanceToCenterOf(e)<=array_list.get(j).getWidth()+array_list.get(j).getHeight()) 
					while(array_list.get(j).distanceToCenterOf(e)<=array_list.get(j).getWidth()+array_list.get(j).getHeight() && t<tentatives_max){//A résoudre à l'aide d'Arbre ? pour plus d'efficacité et une meilleur sélection de l'emplacement ?
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
		return this;
	}
	private boolean NewImageFile() {//Permet de créer un nouveau fichier à partir d'un BufferedImage	
		File file = new File(PATH+NAME+index+FORMAT);
		try {
			if(ImageIO.write(bufferedImage, "png", file)) {
				System.out.println("Image successfully created and XML successfully created");
				System.out.println("Created at "+file.getAbsolutePath());
				return true;
			}
	
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	private boolean convert(){
		String xmlFilePath = PATH+NAME+index+FORMAT_D;
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
	
	        for(int i=0;i<data_list.size();i++) {
	        	RectangularShape d=data_list.get(i).getRectangle();
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
	            
	        }
	
	        TransformerFactory transformerFactory = TransformerFactory.newInstance();
	        Transformer transformer = transformerFactory.newTransformer();
	        DOMSource domSource = new DOMSource(document);
	        StreamResult streamResult = new StreamResult(new File(xmlFilePath));
	        
	        transformer.transform(domSource, streamResult);
	        return true;
	    } catch (ParserConfigurationException pce) {
	    	pce.printStackTrace();
	    } catch (TransformerException tfe) {
	    	tfe.printStackTrace();
	    }
	    return false;
	}
	public boolean buildFile() {
		 g.dispose();
		return NewImageFile() && convert();
	}
	
}
