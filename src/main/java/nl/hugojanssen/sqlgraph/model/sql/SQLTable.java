package nl.hugojanssen.sqlgraph.model.sql;

import nl.hugojanssen.sqlgraph.shared.SQLConstants;
import nl.hugojanssen.sqlgraph.shared.SQLProperties;

import org.apache.log4j.Logger;

/**
 * A <code>SQLTable</code> object represents a database table. It has a name and a database schema. When no database
 * schema is provided, a default schema will be inferred from the properties file.
 * 
 * @author hjanssen
 */
public class SQLTable
{
	/** The logger */
	private final static Logger LOG = Logger.getLogger( SQLTable.class );

	private String schema;

	private final String name;

	/**
	 * Construct a new <code>SQLTable</code> object with the specified database schema and table name. A name must be
	 * provided, while a database schema is optional. When no schema is specified, the default database schema is read
	 * from the properties.xml file.
	 * 
	 * @param schema the database schema
	 * @param name the table name
	 */
	public SQLTable( String schema, String name )
	{
		LOG.debug( "Create new SQLTable " + name );
		this.schema = schema;
		this.name = name;

		this.validateState();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals( Object obj )
	{
		if ( this == obj )
		{
			return true;
		}
		if ( obj == null )
		{
			return false;
		}
		if ( this.getClass() != obj.getClass() )
		{
			return false;
		}
		SQLTable other = (SQLTable) obj;
		if ( this.name == null )
		{
			if ( other.name != null )
			{
				return false;
			}
		}
		else if ( !this.name.equals( other.name ) )
		{
			return false;
		}
		if ( this.schema == null )
		{
			if ( other.schema != null )
			{
				return false;
			}
		}
		else if ( !this.schema.equals( other.schema ) )
		{
			return false;
		}
		return true;
	}

	/**
	 * Returns the name for this table.
	 * 
	 * @return the name for this table
	 */
	public String getName()
	{
		return this.name;
	}

	/**
	 * Returns the database schema for this table.
	 * 
	 * @return the database schema for this table
	 */
	public String getSchema()
	{
		return this.schema;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ( ( this.name == null ) ? 0 : this.name.hashCode() );
		result = prime * result + ( ( this.schema == null ) ? 0 : this.schema.hashCode() );
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "[" + this.getSchema() + "].[" + this.getName() + "]";
	}

	private void validateState()
	{
		if ( this.name == null || this.name.isEmpty() )
		{
			throw new IllegalArgumentException( "Table name must not be null or empty" );
		}
		if ( this.schema == null || this.schema.isEmpty() )
		{
			this.schema = SQLProperties.getInstance().getProperty( SQLConstants.KEY_DB_DEFAULT_SCHEMA );
			LOG.debug( "No schema specified, assuming default " + this.schema );
		}
	}
}