package nl.hugojanssen.sqlgraph.shared;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * This class reads properties that are stored in the <code>properties.xml</code> file.
 * 
 * @author hjanssen
 */
public class SQLProperties
{
	/** The logger */
	private final static Logger LOG = Logger.getLogger( SQLProperties.class );

	private Properties properties = new Properties();

	private static String PROPERTIES = "/properties.xml";

	private static SQLProperties instance;

	/**
	 * Returns an instance of the SQLProperties object.
	 * 
	 * @return the SQLProperties object
	 */
	public static SQLProperties getInstance()
	{
		if ( instance == null )
		{
			instance = new SQLProperties();
		}
		return instance;
	}

	// Constructor must be protected or private to prevent creating new object
	protected SQLProperties()
	{
		this.setUp();
	}

	/**
	 * Returns the value that is stored with the specified key in the properties file, or null if the key was not found.
	 * 
	 * @param key the key to lookup
	 * @return the value that is found for the key, null otherwise
	 */
	public String getProperty( String key )
	{
		String result = null;
		if ( key != null && !key.isEmpty() )
		{
			result = this.properties.getProperty( key );
		}
		return result;
	}

	private void setUp()
	{
		File propertiesFile = new File( this.getClass().getResource( PROPERTIES ).getFile() );
		try
		{
			InputStream is = new FileInputStream( propertiesFile );

			// load the xml file into properties format
			this.properties.loadFromXML( is );
		}
		catch ( IOException e )
		{
			LOG.warn( "Unable to load " + PROPERTIES + ": " + e.getMessage() );
			e.printStackTrace();
		}
	}
}