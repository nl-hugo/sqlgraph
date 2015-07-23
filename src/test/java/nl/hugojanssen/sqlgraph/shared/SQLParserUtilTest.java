package nl.hugojanssen.sqlgraph.shared;

import static org.fest.assertions.Assertions.assertThat;

import java.io.File;
import java.io.IOException;

import org.testng.annotations.Test;

public class SQLParserUtilTest
{
	@Test( description = "Tests file without gexf extension", expectedExceptions = java.lang.IllegalArgumentException.class )
	public void testDirectoryAsGEXFFile() throws IOException
	{
		File file = new File( this.getClass().getResource( "/scripts/invalid/" ).getFile() );
		assertThat( SQLParserUtil.getFileExtension( file.getName() ) ).isNull();
		SQLParserUtil.validateGEXFFile( file );
	}

	@Test( description = "Tests file without sql extension", expectedExceptions = java.lang.IllegalArgumentException.class )
	public void testDirectoryAsSQLFile() throws IOException
	{
		File file = new File( this.getClass().getResource( "/scripts/invalid/" ).getFile() );
		assertThat( SQLParserUtil.getFileExtension( file.getName() ) ).isNull();
		SQLParserUtil.validateSQLFile( file );
	}

	@Test( description = "Tests file without sql extension", expectedExceptions = java.io.FileNotFoundException.class )
	public void testFileDoesNotExists() throws IllegalArgumentException, IOException
	{
		File file = new File( "/scripts/invalid/IDoNotExist.txt" );
		assertThat( SQLParserUtil.getFileExtension( file.getName() ) ).isNull(); // txt is not defined
		SQLParserUtil.validateSQLFile( file );
	}

	@Test( description = "Tests file with null extension" )
	public void testFileWithNullExtension()
	{
		assertThat( SQLParserUtil.getFileExtension( null ) ).isNull();
	}

	@Test( description = "Tests file without extension" )
	public void testFileWithoutExtension()
	{
		assertThat( SQLParserUtil.getFileExtension( "thisisaveryfancystring" ) ).isNull();
	}

	@Test( description = "Tests invalid sql file" )
	public void testInvalidSQLFile() throws IllegalArgumentException, IOException
	{
		File file = new File( this.getClass().getResource( "/scripts/invalid/InvalidSQLFile.sql" ).getFile() );
		assertThat( SQLParserUtil.getFileExtension( file.getName() ) ).isEqualTo( EFileExtension.EXTENSION_SQL );
		SQLParserUtil.validateSQLFile( file );
	}

	@Test( description = "Tests file without sql extension", expectedExceptions = java.lang.IllegalArgumentException.class )
	public void testNotAnGEXFFile() throws IOException
	{
		File file = new File( this.getClass().getResource( "/scripts/invalid/NotAnSQLFile.txt" ).getFile() );
		assertThat( SQLParserUtil.getFileExtension( file.getName() ) ).isNull(); // text is not defined
		SQLParserUtil.validateGEXFFile( file );
	}

	@Test( description = "Tests file without sql extension", expectedExceptions = java.lang.IllegalArgumentException.class )
	public void testNotAnSQLFile() throws IOException
	{
		File file = new File( this.getClass().getResource( "/scripts/invalid/NotAnSQLFile.txt" ).getFile() );
		assertThat( SQLParserUtil.getFileExtension( file.getName() ) ).isNull(); // text is not defined
		SQLParserUtil.validateSQLFile( file );
	}

	@Test( description = "Tests null argument", expectedExceptions = java.lang.IllegalArgumentException.class )
	public void testNullFile() throws IOException
	{
		SQLParserUtil.validateSQLFile( null );
	}

	@Test( description = "Tests valid gexf file" )
	public void testValidGEXFFile() throws IllegalArgumentException, IOException
	{
		File file = new File( this.getClass().getResource( "/out/test.gexf" ).getFile() );
		assertThat( SQLParserUtil.isGEXFFile( file ) ).isTrue();
		SQLParserUtil.validateGEXFFile( file );
	}
}
