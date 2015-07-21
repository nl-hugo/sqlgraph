package nl.hugojanssen.sqlgraph.model.sql;

import static org.fest.assertions.Assertions.assertThat;
import nl.hugojanssen.sqlgraph.shared.SQLConstants;
import nl.hugojanssen.sqlgraph.shared.SQLProperties;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class SQLTableTest
{
	private String name = "table1", schema = "schema1";

	private SQLTable table1, table2;

	private String defaultSchema;

	@BeforeTest
	public void setup()
	{
		this.table1 = new SQLTable( this.schema, this.name );
		this.table2 = new SQLTable( null, this.name );
		this.defaultSchema = SQLProperties.getInstance().getProperty( SQLConstants.KEY_DB_DEFAULT_SCHEMA );
	}

	@Test( dependsOnGroups = "construct-table", description = "Test equals" )
	public void testEquals()
	{
		assertThat( this.table1.equals( null ) ).isFalse();
		assertThat( this.table1.equals( "table1" ) ).isFalse();
		assertThat( this.table1.equals( 1.0f ) ).isFalse();
		assertThat( this.table1.equals( Boolean.TRUE ) ).isFalse();
		assertThat( this.table1.equals( this.table2 ) ).isFalse();

		// true
		assertThat( this.table1.equals( this.table1 ) ).isTrue();
		assertThat( this.table1.equals( new SQLTable( this.schema, this.name ) ) ).isTrue();
	}

	@Test( dependsOnGroups = "construct-table", description = "Test hashCode" )
	public void testHashCode()
	{
		assertThat( this.table1.hashCode() ).isEqualTo( 364251310 );
		assertThat( this.table2.hashCode() ).isEqualTo( -1549349232 ); // 'test'
	}

	@Test( groups = "construct-table", description = "Test constructor" )
	public void testSQLTable()
	{
		assertThat( this.table1 ).isNotNull();
		assertThat( this.table1.getName() ).isEqualTo( this.name );
		assertThat( this.table1.getSchema() ).isEqualTo( this.schema );
	}

	@Test( dependsOnGroups = "construct-table", description = "Test toString" )
	public void testToString()
	{
		assertThat( this.table1.toString() ).isEqualTo( "[" + this.schema + "].[" + this.name + "]" );
	}

	@Test( dependsOnGroups = "construct-table", description = "Test validateState" )
	public void testValidateState()
	{
		assertThat( this.table2.getSchema() ).isEqualTo( this.defaultSchema );

		SQLTable table3 = new SQLTable( "", this.name );
		assertThat( table3.getSchema() ).isEqualTo( this.defaultSchema );

		try
		{
			new SQLTable( "", null );
		}
		catch ( IllegalArgumentException e )
		{
			assertThat( e.getMessage() ).isEqualTo( "Table name must not be null or empty" );
		}
	}
}
