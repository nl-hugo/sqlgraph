package nl.hugojanssen.sqlgraph.shared;

import static org.fest.assertions.Assertions.assertThat;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class SQLPropertiesTest
{
	private SQLProperties properties;

	@BeforeTest
	public void setup()
	{
		this.properties = SQLProperties.getInstance();
	}

	@Test( groups = "construct-properties", description = "Tests constructor" )
	public void testConstructor()
	{
		assertThat( this.properties ).isNotNull();
	}

	@Test( dependsOnGroups = "construct-properties", description = "Tests getProperty" )
	public void testGetProperty()
	{
		assertThat( this.properties.getProperty( null ) ).isNull();
		assertThat( this.properties.getProperty( "" ) ).isNull();
		assertThat( this.properties.getProperty( "idonotexist" ) ).isNull();
		assertThat( this.properties.getProperty( "db.vendor" ) ).isEqualTo( "dbvpostgresql" );
	}
}
