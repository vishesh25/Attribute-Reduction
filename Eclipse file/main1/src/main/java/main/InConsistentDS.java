package main;


import java.util.Iterator;
import java.util.List;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

public class InConsistentDS {

	String[] row = new String[9];
	String[] resultClass = new String[1];
	int flag;
	FileReader fd = new FileReader();

	public void InconsistentDataSet() {

		fd.init();
		List<Row> data1 = fd.data.collectAsList();
		Iterator<Row> it = data1.iterator();
		System.out.println(data1.size() + "\t" + data1.get(0).getString(5));
		for (int i = 0; i < 9; i++) {
				System.out.println(data1.get(i));
			}
		
	}

}
