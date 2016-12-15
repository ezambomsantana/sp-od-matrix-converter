package com.santana.interscsimulator.db;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.santana.interscsimulator.entity.MapPoint;
import com.santana.interscsimulator.entity.Point;

public class Connector {

	public static Connection connection;

	static {
		try {
			Class.forName("org.postgresql.Driver");

			connection = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/eleicao", "postgres", "eduardo");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void insertPoints(List<MapPoint> points) {

		try {

			for (MapPoint point : points) {

				PreparedStatement ps = connection.prepareStatement("insert into point(id, id_link, geom) values(" + point.getId()
						+ "," + point.getIdLink() + ",ST_GEOMFROMTEXT(\'POINT(" + point.getLat() + " " + point.getLon() + ")\'))");
				ps.execute();

			}

		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
		}

	}

	public static long [] selectNearestPoint(double lat, double lon , int dist) {
		
		long [] result = new long[2];
		try {

			String sql = "SELECT id, id_link, ST_Distance(geom, poi)/1000 AS distance_km " + "FROM point, "
					+ "(select ST_MakePoint(" + lat + "," + lon
					+ ")::geography as poi) as poi " + "WHERE ST_DWithin(geom, poi," + dist + " ) "
					+ "ORDER BY ST_Distance(geom, poi) " + "LIMIT 1; ";

			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				result[0] = rs.getLong(1);
				result[1] = rs.getLong(2);
				return result;
			}

		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
		}
		return null;

	}

}