package main;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.hive.ql.parse.HiveParser_IdentifiersParser.sysFuncNames_return;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.functions;

import scala.Tuple10;
import scala.Tuple2;

public class EquivalenceClass {
	
	Dataset<Row> dataSet1;
	
	public void equivalenceClass() {
		FileReader fd = new FileReader();
		fd.init();
		
		dataSet1 = fd.data.groupBy("ClumpThickness","UniformityOfCellSize","UniformityOfCellShape",
				 "MarginalAdhesion","SingleEpithelialCellSize",
"BareNuclei","BlandChromatin","NormalNucleoli","Mitoses").agg(functions.max("Class").as("Class"),functions.collect_list("flag").as("flag"));  
		dataSet1.createOrReplaceGlobalTempView("data1");
		dataSet1.show(5000);
		
		fd.ss.sql("SELECT COUNT(Class) FROM global_temp.data1").show();
		 
		
	}
	
}
