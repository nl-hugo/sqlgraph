package nl.hugojanssen.sqlgraph.shared;

import static org.fest.assertions.Assertions.assertThat;

import org.testng.annotations.Test;

public class EFileExtensionTest
{
	@Test( description = "Tests EFileExtension values" )
	public void testValues()
	{
		assertThat( EFileExtension.EXTENSION_GEXF ).isNotNull();
		assertThat( EFileExtension.EXTENSION_PDF ).isNotNull();
		assertThat( EFileExtension.EXTENSION_SQL ).isNotNull();

		assertThat( EFileExtension.EXTENSION_SQL.extension() ).isEqualTo( "sql" );
		assertThat( EFileExtension.find( "sql" ) ).isEqualTo( EFileExtension.EXTENSION_SQL );
		assertThat( EFileExtension.find( "bla" ) ).isNull();
	}
}
