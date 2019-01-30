package main;

import java.util.ArrayList;

public class AttributeSkip {
    int n;

    AttributeSkip(int n){
        this.n=n;
    }

    public ArrayList<ArrayList<Integer>> combine(){
        int i,j;
                
	ArrayList<Integer> arrayList=new ArrayList<>();
	ArrayList<Integer> tempList;

	ArrayList<ArrayList<Integer>> lists=new ArrayList<>();
		
        for(i=0;i<n;i++){
            arrayList.add(i);
        }

	for(i=0;i<n;i++){
            tempList=new ArrayList<>(arrayList);
            tempList.remove(i);
            lists.add(tempList);
        }

	return lists;
    }

    public static void main(String args[]){
	AttributeSkip attributeSkip=new AttributeSkip(10);
	attributeSkip.combine();
    }
    
}
