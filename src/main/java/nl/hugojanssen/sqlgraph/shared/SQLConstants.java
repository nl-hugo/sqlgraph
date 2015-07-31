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
	 * The name of the temporary schema in the properties file. Note that this schema does not actually exists in the
	 * database and is used for display purposes only.
	 */
	public static final String KEY_DB_TEMP_SCHEMA = "db.temp.schema";

	// Constructor must be private to prevent creating new object
	private SQLConstants()
	{
	}
}