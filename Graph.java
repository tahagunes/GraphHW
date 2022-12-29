package assignment2v4;

import java.util.List;
import java.util.ArrayList;


public class Graph {
	private int vCount;
	private int[][] adj; 
	
	public int getvCount() { 
		return vCount; 
	}
	public int[][] getAdj(){
		return adj;
	}
	public Graph(int vCount) {
		this.vCount=vCount;
		adj=new int[vCount][vCount];//matrisimiz olusturuldu
		for (int i = 0; i < vCount; i++) {
			for (int j = 0; j < vCount; j++) {
				adj[j][i]=0;//tüm matrise 0 atandi
			}
		}
	}
	public void addEdge(String source,String destination,int weight) {
		int sourceindex=0,destinationindex=0;
		boolean sourceflag=true,destinationflag=true;
			for (int i = 0; i < Main.centers.size(); i++) {//daha once eklendiyse indexini buluyoruz
				if(Main.centers.get(i).equals(source)) {
					sourceindex=i;
					sourceflag=false;
					break;
				}			
			}	
			for (int i = 0; i < Main.centers.size(); i++) {//daha once eklendiyse indexini buluyoruz
				if(Main.centers.get(i).equals(destination)) {
					destinationindex=i;
					destinationflag=false;
					break;
				}
			}		
		if(sourceflag) {//daha once eklenmedigi icin ekliyoruz ve yeni indexini buluyoruz
			Main.centers.add(source);
			for (int i = 0; i < Main.centers.size(); i++) {				
					if(Main.centers.get(i).equals(source)) {
						sourceindex=i;
						break;					
				}
			}
		}
		if(destinationflag) {//daha once eklenmedigi icin ekliyoruz ve yeni indexini buluyoruz
			Main.centers.add(destination);
			for (int i = 0; i < Main.centers.size(); i++) {
				if(Main.centers.get(i).equals(destination)) {
					destinationindex=i;
					break;
				}
			}
		}		
		//System.out.println(Main.centers);
		adj[sourceindex][destinationindex]=weight;//matrisimize weight degerini atiyoruz
	}
	public void removeEdge(String source,String destination) {
		int sourceindex=0,destinationindex=0;
		for (int i = 0; i < Main.centers.size(); i++) {
			if(Main.centers.get(i).equals(source)) {
				sourceindex=i;
				break;
			}			
		}
		for (int i = 0; i < adj.length; i++) {
			if(Main.centers.get(i).equals(destination)) {
				destinationindex=i;
				break;
			}
		}		
		//silinecek yolun indexlerini bulup matrisimizden sildik
		adj[sourceindex][destinationindex]=0;
	}
	public boolean hasEdge(String source,String destination) {
		int sourceindex=0,destinationindex=0;
		for (int i = 0; i < Main.centers.size(); i++) {
			if(Main.centers.get(i).equals(source)) {
				sourceindex=i;
				break;
			}			
		}
		for (int i = 0; i < adj.length; i++) {
			if(Main.centers.get(i).equals(destination)) {
				destinationindex=i;
				break;
			}
		}	
		//indexlerimizi bulup matrisimizdeki weight degerine bakiyoruz 0dan farkli mi yani bir degeri var mi
		if(adj[sourceindex][destinationindex]!=0) return true;
		else return false;
	}
	public List<Integer> neighbours(String center){//indexe göre komsularini tutuyoruz
		List<Integer> edges= new ArrayList<Integer>();		
		for (int i = 0; i < vCount; i++) {	//var mi yok mu diye bakmak icin has edgei kullaniyoruz				
			if(hasEdge(center,Main.centers.get(i))) edges.add(i);//eger varsa gidilen yolun indexini atiyoruz
		}
		return edges;
	}
	public void printGraph() {
		for (int i = 0; i < vCount; i++) {
			List<Integer> edges= neighbours(Main.centers.get(i));//sirayla tum noktalarin komsularini aliyoruz
			System.out.print(Main.centers.get(i)+ ": ");//komsulari yazilacak olani yaziyoruz
			for (int j = 0; j < edges.size(); j++) {//daha sonra komsularini yaziyoruz
				System.out.print(Main.centers.get(edges.get(j))+" ");
			}
			System.out.println();
		}
	}
}
	