package com.example.helloandroid;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.KeyEvent;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 * Database helper class used to manage the creation and upgrading of your database. This class also usually provides
 * the DAOs used by the other classes.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	// name of the database file for your application -- change to something appropriate for your app
	private static final String DATABASE_NAME = "helloAndroid.db";
	// any time you make changes to your database objects, you may have to increase the database version
	private static final int DATABASE_VERSION = 1;

	// the DAO object we use to access the SimpleData table
	private Dao<SimpleData, Integer> simpleDao = null;
	private RuntimeExceptionDao<SimpleData, Integer> simpleRuntimeDao = null;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
	}

	/**
	 * This is called when the database is first created. Usually you should call createTable statements here to create
	 * the tables that will store your data.
	 */
	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			Log.i(DatabaseHelper.class.getName(), "onCreate");
			TableUtils.createTable(connectionSource, SimpleData.class);
			TableUtils.createTable(connectionSource, Channel.class);
			TableUtils.createTable(connectionSource, Category.class);
			TableUtils.createTable(connectionSource, ChannelCatMap.class);

		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
			throw new RuntimeException(e);
		}

		// here we try inserting data in the on-create as a test
		RuntimeExceptionDao<SimpleData, Integer> dao = getSimpleDataDao();
		long millis = System.currentTimeMillis();
		// create some entries in the onCreate
		SimpleData simple = new SimpleData(millis);
		dao.create(simple);
		simple = new SimpleData(millis + 1);
		dao.create(simple);
		Log.i(DatabaseHelper.class.getName(), "created new entries in onCreate: " + millis);

		try {
			performOpr();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void performOpr() throws SQLException {
		RuntimeExceptionDao<Channel, Integer> channelDao = getGenericDao(Channel.class);
		RuntimeExceptionDao<Category, Integer> catDao = getGenericDao(Category.class);
		RuntimeExceptionDao<ChannelCatMap, Integer> mapDao = getGenericDao(ChannelCatMap.class);

		Channel ch = new Channel(1, "game1");
		Channel ch1 = new Channel(2, "game2");
		channelDao.create(ch);
		channelDao.create(ch1);
		Category cat = new Category("Game");
		Category cat1 = new Category("News");
		catDao.create(cat);
		catDao.create(cat1);
		Log.e("", "cat id - " + cat1.catId);
		ChannelCatMap map = new ChannelCatMap(1, cat.catId);
		ChannelCatMap map1 = new ChannelCatMap(2, cat.catId);
		ChannelCatMap map2 = new ChannelCatMap(1, cat1.catId);
		//map2.channel = ch;
		mapDao.create(map);
		mapDao.create(map1);
		mapDao.create(map2);

		GenericRawResults<String[]> res = channelDao.queryRaw("Select m.* from channel c, channel_category m where c.channelId = m.channelId and m.channelId = ?", "2");
		String[] res1 = res.getFirstResult();
		Log.d("", "  Row - " + res1.toString());

		/*QueryBuilder<ChannelCatMap, Integer> mapb = mapDao.queryBuilder();
		mapb.selectColumns("channelId").where().eq("catId", cat1.catId);
		QueryBuilder<Channel, Integer> chb = channelDao.queryBuilder();
		chb.where().in("channelId", mapb);
		List<Channel> query = chb.query();
		Log.d("", " res - " + query.size());*/

		/*QueryBuilder<ChannelCatMap, Integer> mapb = mapDao.queryBuilder();
		mapb.where().eq("catId", cat1.catId);
		QueryBuilder<Channel, Integer> chb = channelDao.queryBuilder();

		//chb.where().in("channelId", mapb);
		List<Channel> query = chb.join(mapb).query();
		Log.d("", " res - " + query.size());*/
	}

	/**
	 * This is called when your application is upgraded and it has a higher version number. This allows you to adjust
	 * the various data to match the new version number.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
		try {
			Log.i(DatabaseHelper.class.getName(), "onUpgrade");
			TableUtils.dropTable(connectionSource, SimpleData.class, true);
			// after we drop the old databases, we create the new ones
			onCreate(db, connectionSource);
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Returns the Database Access Object (DAO) for our SimpleData class. It will create it or just give the cached
	 * value.
	 */
	public Dao<SimpleData, Integer> getDao() throws SQLException {
		if (simpleDao == null) {
			simpleDao = getDao(SimpleData.class);
		}
		return simpleDao;
	}

	/**
	 * Returns the RuntimeExceptionDao (Database Access Object) version of a Dao for our SimpleData class. It will
	 * create it or just give the cached value. RuntimeExceptionDao only through RuntimeExceptions.
	 */
	public RuntimeExceptionDao<SimpleData, Integer> getSimpleDataDao() {
		if (simpleRuntimeDao == null) {
			simpleRuntimeDao = getRuntimeExceptionDao(SimpleData.class);
		}
		return simpleRuntimeDao;
	}

	public  <T extends TableBase> RuntimeExceptionDao<T, Integer> getGenericDao(Class<T> clazz) {
		/*if (simpleRuntimeDao == null) {
			simpleRuntimeDao = getRuntimeExceptionDao(clazz);
		}
		return simpleRuntimeDao;*/
		RuntimeExceptionDao<T, Integer> dao = getRuntimeExceptionDao(clazz);
		return dao;
	}
	/**
	 * Close the database connections and clear any cached DAOs.
	 */
	@Override
	public void close() {
		super.close();
		simpleDao = null;
		simpleRuntimeDao = null;
	}
}
