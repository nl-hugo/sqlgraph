package nl.hugojanssen.sqlgraph.shared;

/**
 * Contains constant values that are used throughout the application.
 * 
 * @author hjanssen
 */
public class SQLConstants
{
	/**
	 * The name of the database vendor key in the properties file
	 */
	public final static String KEY_DB_VENDOR = "db.vendor";

	/**
	 * The name of the database default schema key in the properties file
	 */
	public final static String KEY_DB_DEFAULT_SCHEMA = "db.schema";

	/**
	 * The file extension of SQL files
	 */
	public final static String EXTENSION_SQL = "sql";

	// Constructor must be private to prevent creating new object
	private SQLConstants()
	{
	}
}