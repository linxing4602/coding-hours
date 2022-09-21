package com.example.codinghours;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class CodingHoursApplication implements CommandLineRunner{

	@Autowired
	private JdbcTemplate jdbcTemp;

	public static void main(String[] args) {
		SpringApplication.run(CodingHoursApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		String sql = "IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'CodingHours')\n"
					+"	BEGIN\n"
					+"		CREATE DATABASE CodingHours;\n"
					+"	END";
		jdbcTemp.execute(sql);
		sql = "USE CodingHours\n"
				+"IF NOT EXISTS (SELECT * FROM sys.Tables WHERE name = 't')\n"
				+"	BEGIN\n"
				+"		CREATE TABLE t (\n"
				+"			Date varchar(50),\n"
				+"			Hours int\n"
				+"			);\n"
				+"	END";
		jdbcTemp.execute(sql);

		Scanner in = new Scanner(System.in);

		int option=99;

		while (option!=0) {
			System.out.println("Enter a number to choose one of the following numbers.\n"
								+"\t1. Insert an entry\n"
								+"\t2. Delete an entry\n"
								+"\t3. Update an entry\n"
								+"\t4. View logged entries\n"
								+"\t0. Exit\n"
								);
			try {
				option = Integer.parseInt(in.nextLine());
				switch (option) {
					case 1:
						try {
							Scanner date = new Scanner(System.in);
							System.out.println("Enter the date for this entry. (MM/DD/YYYY)");
							SimpleDateFormat sdf = new SimpleDateFormat("mm/dd/yyyy");
							Date d = sdf.parse(date.nextLine());
							String dateToDb = sdf.format(d);
							Scanner hours = new Scanner(System.in);
							System.out.println("Enter the hours for this entry.");
							try {
								int h = Integer.parseInt(hours.nextLine());
								sql = "USE CodingHours\n"
									+ "INSERT INTO t (Date,Hours)\n"
									+ "VALUES (\'" + dateToDb + "\'," + h + ")";
								jdbcTemp.execute(sql);
								System.out.println("Successfully added entry.\n");

							} catch (NumberFormatException e) {
								System.out.println("Invalid input.\n");
							}	
						} catch (ParseException e){
							System.out.println("Invalid date.");
						}
						break;
					case 2:
						try {
							Scanner date = new Scanner(System.in);
							System.out.println("Enter the date for this entry. (MM/DD/YYYY)");
							SimpleDateFormat sdf = new SimpleDateFormat("mm/dd/yyyy");
							Date d = sdf.parse(date.nextLine());
							String dateToDb = sdf.format(d);
							sql = "SELECT * FROM t WHERE Date=CAST(\'" + dateToDb + "\' AS varchar)\n";
							List<Map<String,Object>> a = jdbcTemp.queryForList(sql);
							if (a.isEmpty()) {
								System.out.println("Entry not found\n");
							}
							else {
								sql = "USE CodingHours\n"
										+ "IF EXISTS (SELECT * FROM t WHERE Date=CAST(\'" + dateToDb + "\' AS varchar))\n" 
										+ "	BEGIN\n"
										+ "		DELETE FROM t WHERE Date=CAST(\'" + dateToDb + "\' AS varchar)\n"
										+ "	END";
								jdbcTemp.execute(sql);
								System.out.println("Successfully deleted entry.");
							}
						} catch (ParseException e) {
							System.out.println("Invalid date.");
						}
						break;
					case 3:
					try {
						Scanner date = new Scanner(System.in);
						System.out.println("Enter the date for the entry that you want to update. (MM/DD/YYYY)");
						SimpleDateFormat sdf = new SimpleDateFormat("mm/dd/yyyy");
						Date d = sdf.parse(date.nextLine());
						String dateToDb = sdf.format(d);
						sql = "SELECT * FROM t WHERE Date=CAST(\'" + dateToDb + "\' AS varchar)\n";
						List<Map<String,Object>> a = jdbcTemp.queryForList(sql);
						if (a.isEmpty()) {
							System.out.println("Entry not found\n");
						}
						else {
							System.out.println("Enter the hours to be updated for this entry.");
							try {
								Scanner hours = new Scanner(System.in);
								int h = Integer.parseInt(hours.nextLine());
								sql = "USE CodingHours\n"
									+ "IF EXISTS (SELECT * FROM t WHERE Date=CAST(\'" + dateToDb + "\' AS varchar))\n" 
									+ "	BEGIN\n"
									+ "		UPDATE t\n"
									+ "		SET Hours=" + h +"\n"
									+ "		WHERE Date=CAST(\'" + dateToDb + "\' AS varchar)\n"
									+ "	END";
									jdbcTemp.execute(sql);
									System.out.println("Successfully updated entry.");
							} 
							catch (NumberFormatException e) {
								System.out.println("Invalid input.\n");
							}
						}
					} catch (ParseException e) {
						System.out.println("Invalid date.");
					}
						break;
					case 4:
						sql = "USE CodingHours\n"
						+ "SELECT * FROM t";
						List<Map<String,Object>> a = jdbcTemp.queryForList(sql);
						if (a.isEmpty()){
							System.out.println("No entries logged.\n");
						}
						else {
							System.out.println("\tDate\t\t| Hours");
							for(Map<String,Object> row : a){
								String x = row.get("Date").toString();
								String y = row.get("Hours").toString();
								System.out.println("\t"+ x + "\t| " + y );
							}
							System.out.println();
						}
						break;
					case 0:
						break;
					default:
						System.out.println("Invalid option.\n");
						break;
				}
			} catch (NumberFormatException e) {
				System.out.println("Invalid input.\n");
			}
		}
		System.out.println("Exiting Program.\n");
		System.exit(0);
	}

}
