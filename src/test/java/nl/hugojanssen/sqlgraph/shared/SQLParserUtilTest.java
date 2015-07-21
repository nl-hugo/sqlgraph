package nl.hugojanssen.sqlgraph.shared;

import static org.fest.assertions.Assertions.assertThat;

import java.io.File;
import java.io.IOException;

import org.testng.annotations.Test;

public class SQLParserUtilTest
{
	@Test( description = "Tests invalid sql file" )
	public void testInvalidSQLFile() throws IllegalArgumentException, IOException
	{
		File file = new File( this.getClass().getResource( "/scripts/invalid/InvalidSQLFile.sql" ).getFile() );
		SQLParserUtil.validateSQLFile( file );
	}

	@Test( description = "Tests file without sql extension", expectedExceptions = java.io.FileNotFoundException.class )
	public void testFileDoesNotExists() throws IllegalArgumentException, IOException
	{
		File file = new File( "/scripts/invalid/IDoNotExist.txt" );
		SQLParserUtil.validateSQLFile( file );
	}

	@Test( description = "Tests file without sql extension", expectedExceptions = java.lang.IllegalArgumentException.class )
	public void testDirectoryAsSQLFile() throws IOException
	{
		File file = new File( this.getClass().getResource( "/scripts/invalid/" ).getFile() );
		SQLParserUtil.validateSQLFile( file );
	}

	@Test( description = "Tests file without sql extension", expectedExceptions = java.lang.IllegalArgumentException.class )
	public void testNotAnSQLFile() throws IOException
	{
		File file = new File( this.getClass().getResource( "/scripts/invalid/NotAnSQLFile.txt" ).getFile() );
		SQLParserUtil.validateSQLFile( file );
	}

	@Test( description = "Tests null argument", expectedExceptions = java.lang.IllegalArgumentException.class )
	public void testNullFile() throws IOException
	{
		SQLParserUtil.validateSQLFile( null );
	}

	@Test( description = "Tests file with null extension" )
	public void testFileWithNullExtension()
	{
		assertThat( SQLParserUtil.getFileExtension( null ) ).isEqualTo( "" );
	}

	@Test( description = "Tests file without extension" )
	public void testFileWithoutExtension()
	{
		assertThat( SQLParserUtil.getFileExtension( "thisisaveryfancystring" ) ).isEqualTo( "" );
	}
}
