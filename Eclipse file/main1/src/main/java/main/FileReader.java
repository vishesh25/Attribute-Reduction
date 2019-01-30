package main;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.StructType;


public class FileReader {

	Dataset<Row> data;
	SparkSession ss;
	StructType schema;
	
	public void init() {

		ss = SparkSession.builder()
				.master("local[4]")
				.appName("Main")
				.config("spark.some.config.option", "some-value")
				.getOrCreate();
		
		

		schema = new StructType()
				.add("ClumpThickness", "string")
				.add("UniformityOfCellSize", "string")
				.add("UniformityOfCellShape", "string")
				.add("MarginalAdhesion", "string")
				.add("SingleEpithelialCellSize", "string")
				.add("BareNuclei", "string")
				.add("BlandChromatin", "string")
				.add("NormalNucleoli", "string")
				.add("Mitoses", "string")
				.add("Class", "string")
				.add("flag", "string");

		String path = "C:\\spark\\in_ConsDS1.csv";

		data = ss.read().schema(schema).csv(path);
		data.createOrReplaceGlobalTempView("data");
		data.show(5000);
		ss.sql("SELECT COUNT(Class) FROM global_temp.data").show();
		
	}

}
