package com.out.link.server.http.dao.impl;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.google.gson.Gson;
import com.out.link.server.http.dao.UserGeoDAO;
import com.out.link.server.http.service.model.UserGeo;
import com.out.link.server.http.util.UserGeoUtil;
@Repository
public class UserGeoDAOImpl implements UserGeoDAO {
	
	Gson gson = new Gson();
	
	@Resource
	private DataSource dataSource;
	
    /**
     * spring提供的jdbc操作辅助类
     */
    private JdbcTemplate jdbcTemplate;

    // 设置数据源
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    //根据用户当前位置，获取在一定范围内的用户id
	@Override
	public List<Long> getUserIdsForGeo(long userId,String country,double lat,double lon,int raidus,int start ,int end) { //lat 纬度 lon 经度
		double[] targetGeo = UserGeoUtil.getAround(lat, lon, raidus); //minLat, minLng, maxLat, maxLng
		String sql = "select user_id from db_user_geo where latitude <> 0 and longitude > ?and longitude < ? and latitude > ? and latitude < ? "
			+"and user_id <> ? and country <> ? order by ACOS(SIN((? * 3.1415) / 180 ) * SIN((latitude * 3.1415) / 180 ) + COS((? * 3.1415) / 180 ) * COS((latitude * 3.1415) / 180 ) "
			+"*COS((? * 3.1415) / 180 - (longitude * 3.1415) / 180 ) ) * 6380 asc limit ?,?";
		return jdbcTemplate.queryForList(sql, new Object[] {targetGeo[1],targetGeo[3],targetGeo[0],targetGeo[2],userId,country,lat,lat,lon,start,end}, Long.class);
	}

	@Override
	public UserGeo getUserGeoByUserId(long userId) {
		String sql = "select * from db_user_geo where user_id = ?";
		jdbcTemplate.queryForObject(sql, new Object[]{userId}, new UserGeoMapper());
		return null;
	}

	@Override
	public int updateGeoByUserId(long userId,double lat,double lon) {
		StringBuffer sql = new StringBuffer();
		sql.append("update db_user_geo set ")
		.append("latitude = ?,")
		.append("longitude = ?")
		.append("where user_id = ?");
		return jdbcTemplate.update(sql.toString(), new Object[]{lat,lon,userId});
	}

	@Override
	public int addUserGeo(UserGeo userGeo) {
		if(userGeo != null) {
			StringBuffer sql = new StringBuffer();
			sql.append("insert into db_user_geo (user_id,latitude,longitude,country,create_time) ")
			.append("values (?,?,?,?,?)");
			return jdbcTemplate.update(sql.toString(), new Object[]{userGeo.getUserId(),userGeo.getLatitude(),userGeo.getLongitude(),userGeo.getCountry(),new Date()});
		}
		return 0;
	}
	private static final class UserGeoMapper implements RowMapper<UserGeo> {
		public UserGeo mapRow(ResultSet rs, int rowNum) throws SQLException {
			UserGeo userGeo = new UserGeo();
			userGeo.setCountry(rs.getString("country"));
			userGeo.setId(rs.getLong("id"));
			userGeo.setUserId(rs.getLong("user_id"));
			userGeo.setLatitude(rs.getDouble("latitude"));
			userGeo.setLongitude(rs.getDouble("longitude"));
			userGeo.setCreateTime(rs.getDate("create_time"));
			userGeo.setUpdateeTime(rs.getDate("update_time"));
			return userGeo;
		}
	}
}
