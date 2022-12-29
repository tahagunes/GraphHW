package assignment2v4;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
//https://steemit.com/programming/@drifter1/programming-java-graph-maximum-flow-algorithm-ford-fulkerson
//buyuk graphde ya sonsuz dongude kaliyor ya da
//matrisden dolayi calismasi cok uzun suruyor
//elimdeki kucuk graphda deneyebildim onu da ekledim
public class Main {
	static List<String> centers=new ArrayList<String>();// indexlere ulasmak icin genel bir list tutuyoruz
	static int maxpack=0,savedpath=0;
	static List<String> needincreasepath=new ArrayList<String>();
	static ArrayList<Integer>[] pathindex;
	public static boolean bfs(Graph rg, String source, String dest, int parent[]) {
		boolean[] seen = new boolean[rg.getvCount()];// eleman sayisi kadarlik boolean array olusturuyoruz
		for (int i = 0; i < rg.getvCount(); i++) {
			seen[i] = false;// tüm degerleri falseladik
		}
		LinkedList<String> q = new LinkedList<String>();
		q.add(source);// cikis noktamizi ekledik
		int sourceindex = 0;
		for (int i = 0; i < centers.size(); i++) {
			if (centers.get(i).equals(source)) {
				sourceindex = i;
				break;
			}
		}
		seen[sourceindex] = true;// cikis noktamizi gidildi isaretledik
		parent[sourceindex] = -1;//cikis noktamizin parentini ekledik
		while (!q.isEmpty()) {//
			String i = q.poll();// qnun en ondeki elemanini cekip sildik LinkedListden
			for (Integer j : rg.neighbours(i)) {// komsularin indexlerini aliyoruz
				int iindex = 0;
				for (int a = 0; a < centers.size(); a++) {
					if (centers.get(a).equals(i)) {
						iindex = a;
						break;
					}
				}
				if ((!seen[j]) && (rg.getAdj()[iindex][j] > 0)) {// seenin J indexindekini falseladik ve 0dan
																			// buyuk mu diye baktik
					q.add(Main.centers.get(j));// q ya j indexindeki noktayi ekledik
					seen[j] = true;// gidildi isaretledik
					parent[j] = iindex;// komsu olarak qnun en basindan cikanin indexini ekledik
				}
			}
		}
		int destindex = 0;
		for (int i = 0; i < centers.size(); i++) {
			if (centers.get(i).equals(dest)) {
				destindex = i;
				break;
			}
		}
		return seen[destindex];// varilmasi gereken noktanin durumu gonderildi
	}
	public static void searchincrease(Graph rg, String source, String dest) {
		if (source.equals(dest)) {// baslangic ve bitis ayni mi baktik
			return;
		}
		int V = rg.getvCount();// eleman sayisini hafizaya aldik
		
		int parent[] = new int[V]; // yeni parentler olusturuldu
		int max_flow = 0;// baslangic degeri atandi
		while (bfs(rg, source, dest, parent)) {// varilmasi gereken noktanin durumu geldi
			String path=dest;
			int path_flow = 999999999;// baslangic degeri atandi
			int destindex = 0;
			for (int a = 0; a < centers.size(); a++) {
				if (centers.get(a).equals(dest)) {
					destindex = a;
					break;
				}
			}
			int sourceindex = 0;
			for (int a = 0; a < centers.size(); a++) {
				if (centers.get(a).equals(source)) {
					sourceindex = a;
					break;
				}
			} // indexler bulundu
			for (int i = destindex; i != sourceindex; i = parent[i]) {
				int j = parent[i];// parentin indexi alindi
				//System.out.println(path_flow);
				//System.out.print(centers.get(j)+"->");
				int first=0,second=0,increase=0;
				boolean flag=true;
				if(path_flow==999999999)flag=false;
				if(flag&&path_flow!=rg.getAdj()[j][i]) 
				{
					first=path_flow;
					path_flow = Math.min(path_flow, rg.getAdj()[j][i]);	
					second=path_flow;
					increase=first-second;
					boolean doorflag=true;
					for (int k = 0; k < needincreasepath.size(); k++) {
						String[] a=needincreasepath.get(k).split(",");
						if(a[0].equals(centers.get(j)+" to "+centers.get(i)))
							{
							needincreasepath.set(k, centers.get(j)+" to "+centers.get(i)+",needs increased of ,"+increase);
							doorflag=false;
							}
					}
					if(doorflag&&increase!=0)needincreasepath.add(centers.get(j)+" to "+centers.get(i)+",needs increased of ,"+increase);
					rg.getAdj()[j][i]=path_flow+increase;
					rg.getAdj()[i][j]-=increase;
				}
				else {
					path_flow = Math.min(path_flow, rg.getAdj()[j][i]);	
				}
			}
			//System.out.println();
			//System.out.print(source+"->>");
			for (int i = destindex; i != sourceindex; i = parent[i]) {
				int j = parent[i];// index degerini aldik
				rg.getAdj()[j][i] -= path_flow;//gidilen noktanin degeri dusuruluyor
				rg.getAdj()[i][j] += path_flow;//cikis merkezinin yolu ekleniyor
			}			
			//System.out.println(path+" sended pack is "+path_flow);
			max_flow += path_flow;
		}
		return;
	}
	public static int FordFulkerson(Graph g, String source, String dest) {
		int count=0;
		if (source.equals(dest)) {// baslangic ve bitis ayni mi baktik
			return 0;
		}
		int V = g.getvCount();// eleman sayisini hafizaya aldik
		Graph rg = new Graph(V);// yeni graph olusturduk
		for (int i = 0; i < V; i++) {
			for (int j = 0; j < V; j++) {
				rg.getAdj()[i][j] = g.getAdj()[i][j];// orijinal graphimizi kopyaliyoruz
			}
		}
		int parent[] = new int[V]; // yeni parentler olusturuldu
		int max_flow = 0;// baslangic degeri atandi
		while (bfs(rg, source, dest, parent)) {// varilmasi gereken noktanin durumu geldi
			String path=dest;
			int path_flow = 999999999;// baslangic degeri atandi
			int destindex = 0;
			for (int a = 0; a < centers.size(); a++) {
				if (centers.get(a).equals(dest)) {
					destindex = a;
					break;
				}
			}
			int sourceindex = 0;
			for (int a = 0; a < centers.size(); a++) {
				if (centers.get(a).equals(source)) {
					sourceindex = a;
					break;
				}
			} // indexler bulundu
			
			for (int i = destindex; i != sourceindex; i = parent[i]) {
				int j = parent[i];// parentin indexi alindi
				//System.out.println(path_flow);
				//System.out.print(centers.get(j)+"->");
				if(path_flow!=rg.getAdj()[j][i]) {path_flow = Math.min(path_flow, rg.getAdj()[j][i]);				
				}// varilmasi gereken noktanin tum yollarina bakiliyor
														// en yuksek yolu bulmaya calisiyoruz ????
			}
			//System.out.println();
			//System.out.print(source+"->>");
			for (int i = destindex; i != sourceindex; i = parent[i]) {
				int j = parent[i];// index degerini aldik
				rg.getAdj()[j][i] -= path_flow;//gidilen noktanin degeri dusuruluyor
				rg.getAdj()[i][j] += path_flow;//cikis merkezinin yolu ekleniyor
				
				path=path+"<-"+centers.get(j);
			}
			
			System.out.println(path+" sended pack is "+path_flow);
			max_flow += path_flow;
		}
		maxpack=max_flow;
		
		return max_flow;
	}
	public static void main(String[] args) throws NumberFormatException, IOException {
		// TODO Auto-generated method stub
		
		int V=14848 ;
		System.out.println(V);
		Graph g = new Graph(V);
//////////////////////////////////////////////////////////////////////////////7
		FileReader fileReader = new FileReader("graph.txt");
		String line;
		BufferedReader br = new BufferedReader(fileReader);
		while ((line = br.readLine()) != null) {
			String[] readsplit = line.split("\t");
			g.addEdge(readsplit[0], readsplit[1], Integer.parseInt(readsplit[2]));
		}
		br.close();		
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		Scanner userinputs = new Scanner(System.in);
		System.out.print("Enter source :");
		String inputsource = userinputs.next();
		inputsource = inputsource.toUpperCase();
		Scanner userinputd = new Scanner(System.in);
		System.out.print("Enter destination :");
		String inputdestination = userinputd.next();
		inputdestination = inputdestination.toUpperCase();
		System.out.println("Path(s) :");
		FordFulkerson(g, inputsource, inputdestination);
		System.out.println("Daily pack limit is :"+maxpack);
		//minCut(g, inputsource, inputdestination);

		Graph save = new Graph(V);// yeni graph olusturduk
		for (int i = 0; i < V; i++) {
			for (int j = 0; j < V; j++) {
				save.getAdj()[i][j] = g.getAdj()[i][j];// orijinal graphimizi kopyaliyoruz
			}
		}
		searchincrease(save, inputsource, inputdestination);
		for (int i = 0; i < needincreasepath.size(); i++) {
			System.out.println(needincreasepath.get(i));
		}

	}
}
