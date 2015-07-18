package nl.hugojanssen.sqlgraph.model.sql;

import static org.fest.assertions.Assertions.assertThat;
import nl.hugojanssen.sqlgraph.model.sql.EClauseType;

import org.testng.annotations.Test;

public class EClauseTypeTest
{
	@Test( description = "Tests EClauseType values" )
	public void testValues()
	{
		assertThat( EClauseType.SOURCE ).isNotNull();
		assertThat( EClauseType.TARGET ).isNotNull();
		assertThat( EClauseType.UNKNOWN ).isNotNull();
	}
}
