package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.hive.ql.parse.HiveParser_IdentifiersParser.sysFuncNames_return;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;




public class AttributeReduction {
	
	
    Parameter parameter;
	ArrayList<Reduct> reductArrayList = new ArrayList<>();
	ArrayList<Integer> core = new ArrayList<>();
    Iterator<Integer> innerIterator=null;
    ArrayList<Integer> skippedColumnList;
	ArrayList<Reduct> tempReductSet=null;
	
	 int decision1 = 0, decision2 = 0 ;
     String line;
     String[] arr;
     int i,j,k;
     boolean flag;
	
	public void Reduction() {
		
		AttributeReduction attributeReduction=new AttributeReduction();
		FileReader fd = new FileReader();
		EquivalenceClass ec = new EquivalenceClass();
		fd.init();
		ec.equivalenceClass();
		Dataset<Row> data2 = fd.ss.sql("SELECT ClumpThickness,UniformityOfCellSize,"
				+ "UniformityOfCellShape,MarginalAdhesion,"
				+ "SingleEpithelialCellSize,BareNuclei,"
				+ "BlandChromatin,NormalNucleoli,"
				+ "Mitoses,Class,flag FROM global_temp.data1");
		List<Row> consistentDataSet =  data2.collectAsList();
		int counterr = (int) data2.count();
		
		
	    System.out.println("\n\n");
	    
	    AttributeSkip attributeSkip = new AttributeSkip(9);
	    	    
	    ArrayList<ArrayList<Integer>> lists = attributeSkip.combine();
	    Iterator<ArrayList<Integer>> iterator=lists.iterator();
        
        while(iterator.hasNext()){
		//list of elements to visit except the skipped one
		skippedColumnList=(ArrayList<Integer>)iterator.next();
        innerIterator=skippedColumnList.iterator();
		tempReductSet=attributeReduction.getReductSet(consistentDataSet,skippedColumnList);//getReductSet is function to check the B to I existence
            }   
	    
  // ============================ creating core===================================
	    
	    
	   
        HashMap<Integer,Integer> counter=new HashMap<>();
	   
	    int val;
        for(k=0;k<tempReductSet.size();k++) {
	    	
	    	for(j=0;j<8;j++) {
	    		val=Integer.parseInt(tempReductSet.get(k).colData[j]);
	    	
	    		if(counter.containsKey(val)) {
	    			counter.put(val, counter.get(val)+1);
	    	
	    		}else {
	    	
	    			counter.put(val, 1);
	    		}
	    		//System.out.println("\n\n");
	    	}
	    }
	    int i=0;
	    float h[] = new float[11];
	    float sign[] = new float[11];
	    float sign1[] = new float[11];
	    float avg = 0,avg1=0;
	    System.out.println("--------------------------");
	    
	    System.out.println("Column Size : " + (counter.size()-1));
	 
	    for(Map.Entry<Integer, Integer> m : counter.entrySet()) {
	    	
	    			h[i] = consistentDataSet.size()-m.getValue();
	    			i=i+1;
	    }
	  
	    System.out.println("--------------------------");
	    for(int q=0;q<i-1;q++) {
	    	System.out.println("Affected number of rows : "+h[q]+" after removing column " + q);
	    }
	    	
	   
	   for(int q=0;q<i-1;q++)
	   {
		   sign[q]=(float)(h[q]/counterr)*100;
	   }
	 
	   for(int q=0;q<i-1;q++)
	   {
		   avg += sign[q];
		   
	   }
	   
	   System.out.println("--------------------------");
	   avg = avg / (i-1);
	
	   int flag = 0;
	   int flag1[] = new int[2];
	   int cnt[] = new int[11];
	   ArrayList<Float> pos = new ArrayList<>();
	   for(int q=0;q<i-1;q++)
	   {
		   if(sign[q]>avg)
		   {
			   sign1[flag] = sign[q];
			   cnt[flag] = q;
			   pos.add(sign[q]);
			   System.out.println("Column "+ cnt[flag] + " is Reduct.");
			   avg1 += sign1[flag];
			   flag++;
		   }
		
	   }
	   
	   avg1 = avg1 / flag;
	   System.out.println("--------------------------");
	   
	   for(int q=0; q<flag;q++)
	   {
		  if(sign1[q]>avg1 && flag1[0]== 0) {
			  System.out.println("Column " + cnt[q] + " is Core.");
			  flag1[0]++;
		  }
		  else if(sign1[q]<avg1 || flag1[0] != 0) {
			  System.out.println("Column " + cnt[q] + " is Minimal Reduct. ");
			  flag1[1]++;
		  }
	   }
	   System.out.println("--------------------------");
		
	   System.out.println("Core size: "+flag1[0]+"\nMinimal Reduct size : "+flag1[1]);
	   
	   System.out.println("--------------------------");
	}
    
  
    public ArrayList<Reduct> getReductSet(List<Row> consistentDataSet, ArrayList<Integer> skippedColumnList)
    {	// i-> consistentDataSetEntry, j->consistentDataSetEntryAttribute
        System.out.println("----------Entering getReductSet()------------");
        String tempList[]=null;
        String tempDecision=null;
        Row tempParameter=null;
        ArrayList<Reduct> reductArrayList=new ArrayList<Reduct>();
        Reduct tempReductArray=null,tempReductArray2=null;
        ArrayList<String> row1 = new ArrayList<>();
        
        // used for testing
        System.out.println("printing skipped column list");
        for(int i:skippedColumnList){
            System.out.println("Skipped List "+i);
        }
        //System.out.println();
        
        
        int k=0;
        for (int i = 0; i < consistentDataSet.size(); i++) {
            tempParameter = consistentDataSet.get(i);
          
            tempList=new String[8];
            k=0;
        
            for(int j = 0 ; j < 10 ; j++){
                if(skippedColumnList.contains(j)){
                    if(j!=9){
                        tempList[k]=(String) tempParameter.getString(j); 
                        k=k+1;
                    }
                }else if(j==9){
                       tempDecision=tempParameter.getString(j);                        
                }
            }
            tempReductArray = new Reduct();
                        
            tempReductArray.colData=tempList;
            tempReductArray.decision=tempDecision;
            
            if((tempReductArray2=reductArrayExists(tempReductArray, reductArrayList))!=null){
                
              
                if(!tempReductArray.decision.equals(tempReductArray2.decision)){
                
                    reductArrayList.remove(tempReductArray2);
                 
                }else{
                    //testing part only
                  //  System.out.println("decision and reduct equal. do nothing");
                }
            }else{
                reductArrayList.add(tempReductArray);
            }
    
        }
       
        return reductArrayList; 
    }
    
    public Reduct reductArrayExists(Reduct tempReductArray, ArrayList<Reduct> reductArrayList){
        String[] tempReductColData=tempReductArray.colData;
        Reduct temp=new Reduct();
        Iterator iterator=reductArrayList.iterator();
        
        while(iterator.hasNext()){
            temp=(Reduct)iterator.next();
           
            if(areArrayEqual(temp.colData,tempReductColData)){
               return temp; 
            }
        }
        return null;
    }
    public boolean areArrayEqual(String arr1[], String arr2[]){
       
        for(int i=0;i<arr1.length;i++){
          //  System.out.println(arr1[i]+"  ");
        }
        //System.out.println();
        for(int i=0;i<arr1.length;i++){
            //System.out.println(arr2[i]+"  ");
        }
       // System.out.println();
        for(int i=0;i<arr1.length;i++){
            if(!arr1[i].equals(arr2[i])){
                return false;
            }
        }
        return true;
    }
	

}
