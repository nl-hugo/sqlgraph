package nl.hugojanssen.sqlgraph.shared;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Utility class that provides functions for the validation of directories and (SQL) files.
 * 
 * @author hjanssen
 */
public class SQLParserUtil
{
	/**
	 * Returns the extension of the specified file name. The extension is specified by the characters after the last '.'
	 * in the file name. Returns <code>null</code> if no file name is specified or if no extension is found.
	 * 
	 * @param aFileName the file name to get the extension for
	 * @return the extension of the file if found, null otherwise.
	 */
	public static EFileExtension getFileExtension( final String aFileName )
	{
		EFileExtension result = null;
		if ( aFileName != null )
		{
			int i = aFileName.lastIndexOf( '.' );
			if ( i > 0 )
			{
				String extension = aFileName.substring( i + 1 );
				result = EFileExtension.find( extension );
			}
		}
		return result;
	}

	/**
	 * Checks if the specified file is an SQL file. A file is assumed to be an SQL file, if the file name ends with .sql
	 * (ignoring case).
	 * 
	 * @param aFile the file to check
	 * @return true if the file is an SQL file, false otherwise
	 */
	public static boolean isSQLFile( final File aFile )
	{
		return EFileExtension.EXTENSION_SQL.equals( SQLParserUtil.getFileExtension( aFile.getName() ) );
	}

	/**
	 * Checks if the specified file is an GEXF file. A file is assumed to be an GEXF file, if the file name ends with
	 * .gexf (ignoring case).
	 * 
	 * @param aFile the file to check
	 * @return true if the file is an GEXF file, false otherwise
	 */
	public static boolean isGEXFFile( final File aFile )
	{
		return EFileExtension.EXTENSION_GEXF.equals( SQLParserUtil.getFileExtension( aFile.getName() ) );
	}

	/**
	 * Validates the specified file or directory against the following conditions:<br/>
	 * <br/>
	 * - the file is defined (not null) <br/>
	 * - the file exists in the file system <br/>
	 * - the file is readable <br/>
	 * 
	 * @param aFile the file to validate
	 * @throws IllegalArgumentException when the file is undefined (null)
	 * @throws IOException when the file does not exists or is not readable
	 */
	public static void validateFileOrDirectory( final File aFile ) throws IOException
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
			throw new IOException( "File cannot be read: " + aFile );
		}
	}

	/**
	 * Validates the specified file as SQL file. In addition to the conditions in <code>validateFileOrDirectory</code>
	 * it also checks the following conditions: <br/>
	 * <br/>
	 * - the file is an actual file (and not a directory) <br/>
	 * - the file is an SQL file <br/>
	 * 
	 * @param aFile the file to validate
	 * @throws IllegalArgumentException when the file is undefined (null) or not an SQL file (but a directory or file
	 *             with an extension other than .sql)
	 * @throws IOException when the file does not exists or is not readable
	 * @see nl.hugojanssen.sqlgraph.shared.SQLParserUtil.validateFileOrDirectory
	 */
	public static void validateSQLFile( final File aFile ) throws IllegalArgumentException, IOException
	{
		validateFileOrDirectory( aFile );

		if ( !aFile.isFile() || !SQLParserUtil.isSQLFile( aFile ) )
		{
			throw new IllegalArgumentException( "Not an SQL file: " + aFile );
		}
	}

	/**
	 * Validates the specified file as GEXF file. In addition to the conditions in <code>validateFileOrDirectory</code>
	 * it also checks the following conditions: <br/>
	 * <br/>
	 * - the file is an actual file (and not a directory) <br/>
	 * - the file is an GEXF file <br/>
	 * 
	 * @param aFile the file to validate
	 * @throws IllegalArgumentException when the file is undefined (null) or not an GEXF file (but a directory or file
	 *             with an extension other than .gexf)
	 * @throws IOException when the file does not exists or is not readable
	 * @see nl.hugojanssen.sqlgraph.shared.SQLParserUtil.validateFileOrDirectory
	 */
	public static void validateGEXFFile( final File aFile ) throws IllegalArgumentException, IOException
	{
		validateFileOrDirectory( aFile );

		if ( !aFile.isFile() || !SQLParserUtil.isGEXFFile( aFile ) )
		{
			throw new IllegalArgumentException( "Not an GEXF file: " + aFile );
		}
	}
}
