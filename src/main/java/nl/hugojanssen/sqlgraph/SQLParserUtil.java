package nl.hugojanssen.sqlgraph;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * @author hjanssen
 */
public class SQLParserUtil
{

	public final static String EXTENSION_SQL = "sql";

	public static boolean isSQLFile( final File aFile )
	{
		return SQLParserUtil.getFileExtension( aFile.getName() ).equalsIgnoreCase( EXTENSION_SQL );
	}

	public static void validateFileOrDirectory( final File aFile ) throws FileNotFoundException
	{
		if ( aFile == null )
		{
			throw new IllegalArgumentException( "File should not be null." );
		}
		if ( !aFile.exists() )
		{
			throw new FileNotFoundException( "File does not exist: " + aFile );
		}
		if ( !aFile.canRead() )
		{
			throw new IllegalArgumentException( "File cannot be read: " + aFile );
		}
	}

	public static void validateSQLFile( final File aFile ) throws IllegalArgumentException, FileNotFoundException
	{
		validateFileOrDirectory( aFile );

		if ( !aFile.isFile() || !SQLParserUtil.isSQLFile( aFile ) )
		{
			throw new IllegalArgumentException( "Not an SQL file: " + aFile );
		}
	}

	public static String getFileExtension( final String aFileName )
	{
		String result = "";
		if ( aFileName != null )
		{
			int i = aFileName.lastIndexOf( '.' );
			if ( i > 0 )
			{
				result = aFileName.substring( i + 1 );
			}
		}
		return result;
	}
}
